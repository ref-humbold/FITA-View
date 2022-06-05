package fitaview.viewer.automaton;

import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import fitaview.automaton.*;
import fitaview.automaton.transition.TransitionsSender;
import fitaview.messaging.Message;
import fitaview.messaging.MessageReceiver;
import fitaview.messaging.SignalReceiver;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.utils.Triple;

public class AutomatonTreeView
        extends JTree
        implements MessageReceiver<Triple<Variable, String, String>>, SignalReceiver
{
    static final String EMPTY_ROOT_TEXT = "No automaton specified...";
    private static final long serialVersionUID = 5636100205267426054L;
    private final Pointer<TreeAutomaton> automatonPointer;
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(EMPTY_ROOT_TEXT);
    private final DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    Map<Variable, String> lastTransitions = new HashMap<>();

    public AutomatonTreeView(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        automatonPointer.addReceiver(this);
        TransitionsSender.getInstance().addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        setModel(treeModel);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        setShowsRootHandles(true);
        setCellRenderer(new AutomatonTreeViewRenderer());
        setBorder(BorderFactory.createLoweredBevelBorder());
        initializeTree();
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
            initializeTree();
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance()
                && !automatonPointer.isEmpty()
                && automatonPointer.get().getRunningMode() == AutomatonRunningMode.STOPPED)
            lastTransitions.clear();

        treeModel.reload();
    }

    @Override
    public void receiveMessage(Message<Triple<Variable, String, String>> message)
    {
        Variable var = message.getParam().getFirst();
        String keyString = message.getParam().getSecond();
        String valueString = message.getParam().getThird();

        lastTransitions.put(var, getTransitionEntryString(keyString, valueString));
        treeModel.reload();
    }

    String getTransitionEntryString(String key, String value)
    {
        return String.format("%s -> %s", key, value);
    }

    private void initializeTree()
    {
        TreeAutomaton automaton = automatonPointer.get();

        rootNode.removeAllChildren();
        lastTransitions.clear();

        if(automaton != null)
        {
            rootNode.setUserObject(automaton.getTypeName());
            loadAlphabet(automaton);
            loadVariables(automaton);
            loadAccepting(automaton);

            if(automaton instanceof InfiniteTreeAutomaton)
                loadBuchiAccepting((InfiniteTreeAutomaton)automaton);

            loadTransitions(automaton);
        }
        else
            rootNode.setUserObject(EMPTY_ROOT_TEXT);
    }

    private void loadAlphabet(TreeAutomaton automaton)
    {
        Collection<String> alphabet = automaton.getAlphabet();
        DefaultMutableTreeNode alphabetNode = new DefaultMutableTreeNode("Alphabet");

        alphabet.stream().map(DefaultMutableTreeNode::new).forEach(alphabetNode::add);
        rootNode.add(alphabetNode);
    }

    private void loadVariables(TreeAutomaton automaton)
    {
        Collection<Variable> variables = automaton.getVariables();
        DefaultMutableTreeNode variablesNode = new DefaultMutableTreeNode("Variables");

        variables.forEach(var -> {
            DefaultMutableTreeNode varNode = new DefaultMutableTreeNode(var.getVarName());

            var.forEach(value -> varNode.add(new VariableTreeViewNode(var, value)));
            variablesNode.add(varNode);
        });

        rootNode.add(variablesNode);
    }

    private void loadAccepting(TreeAutomaton automaton)
    {
        loadAccepting(automaton.getAcceptanceConditions(), "Acceptance conditions");
    }

    private void loadBuchiAccepting(InfiniteTreeAutomaton automaton)
    {
        loadAccepting(automaton.getBuchiAcceptanceConditions(), "Buchi acceptance conditions");
    }

    private void loadAccepting(AcceptanceConditions accepting, String text)
    {
        DefaultMutableTreeNode acceptNode = new DefaultMutableTreeNode(text);

        accepting.getStatesConditions().forEach(mapping -> {
            DefaultMutableTreeNode conditionNode = new DefaultMutableTreeNode("condition");
            mapping.entrySet()
                   .stream()
                   .map(entry -> new DefaultMutableTreeNode(
                           AcceptanceConditions.getEntryString(entry)))
                   .forEach(conditionNode::add);
            acceptNode.add(conditionNode);
        });

        rootNode.add(acceptNode);
    }

    private void loadTransitions(TreeAutomaton automaton)
    {
        Map<Pair<Variable, String>, String> stringTransitions = automaton.getTransitionAsStrings();
        DefaultMutableTreeNode transitionNode = new DefaultMutableTreeNode("Transition relation");
        Map<Variable, DefaultMutableTreeNode> varNodes = automaton.getVariables()
                                                                  .stream()
                                                                  .collect(Collectors.toMap(
                                                                          Function.identity(),
                                                                          var -> new DefaultMutableTreeNode(
                                                                                  var.getVarName()),
                                                                          (a, b) -> b));

        stringTransitions.forEach((key, value) -> varNodes.get(key.getFirst())
                                                          .add(new TransitionTreeViewNode(
                                                                  key.getFirst(),
                                                                  getTransitionEntryString(
                                                                          key.getSecond(),
                                                                          value))));
        varNodes.values().forEach(transitionNode::add);
        rootNode.add(transitionNode);
    }

    private static final class VariableTreeViewNode
            extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = -2455947033876221381L;

        private Variable variable;
        private String value;

        private VariableTreeViewNode(Variable variable, String value)
        {
            super(Objects.equals(variable.getInitValue(), value) ? value + " [init value]" : value);
            this.variable = variable;
            this.value = value;
        }

        public boolean hasInitValue()
        {
            return Objects.equals(variable.getInitValue(), value);
        }

        @Override
        public VariableTreeViewNode clone()
        {
            return (VariableTreeViewNode)super.clone();
        }
    }

    private static class TransitionTreeViewNode
            extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = -5087307349812311759L;

        private Variable variable;
        private String value;

        public TransitionTreeViewNode(Variable variable, String value)
        {
            super(value);
            this.variable = variable;
            this.value = value;
        }

        @Override
        public TransitionTreeViewNode clone()
        {
            return (TransitionTreeViewNode)super.clone();
        }
    }

    private class AutomatonTreeViewRenderer
            extends DefaultTreeCellRenderer
    {
        private static final long serialVersionUID = 7327449947415447547L;

        private final Color defaultBackgroundColor = super.getBackgroundNonSelectionColor();
        private final Color defaultBackgroundSelectionColor = super.getBackgroundSelectionColor();

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                      boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus)
        {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row,
                                               hasFocus);

            setBackgroundNonSelectionColor(defaultBackgroundColor);
            setBackgroundSelectionColor(defaultBackgroundSelectionColor);

            if(value instanceof VariableTreeViewNode)
            {
                VariableTreeViewNode node = (VariableTreeViewNode)value;

                if(node.hasInitValue())
                {
                    setBackgroundNonSelectionColor(Color.LIGHT_GRAY);
                    setBackgroundSelectionColor(Color.CYAN);
                }
            }
            else if(value instanceof TransitionTreeViewNode)
            {
                TransitionTreeViewNode node = (TransitionTreeViewNode)value;

                if(Objects.equals(lastTransitions.get(node.variable), node.value))
                {
                    setBackgroundNonSelectionColor(Color.GREEN);
                    setBackgroundSelectionColor(Color.GREEN);
                }
            }

            return this;
        }
    }
}
