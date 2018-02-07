package ref_humbold.fita_view.viewer.automaton;

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

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.AcceptanceConditions;
import ref_humbold.fita_view.automaton.AutomatonRunningModeSender;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.automaton.transition.TransitionsSender;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.messaging.SignalReceiver;

public class AutomatonTreeView
    extends JTree
    implements MessageReceiver<Triple<Variable, String, String>>, SignalReceiver
{
    static final String EMPTY_ROOT_TEXT = "No automaton specified...";
    private static final long serialVersionUID = 5636100205267426054L;

    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(EMPTY_ROOT_TEXT);
    Map<Variable, String> lastTransitions = new HashMap<>();
    private Pointer<TreeAutomaton> automatonPointer;
    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    private AutomatonTreeViewRenderer renderer = new AutomatonTreeViewRenderer();

    public AutomatonTreeView(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        TransitionsSender.getInstance().addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        this.setModel(treeModel);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setShowsRootHandles(true);
        this.setCellRenderer(renderer);
        this.setBorder(BorderFactory.createLoweredBevelBorder());

        this.initializeTree();
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
            initializeTree();
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance()
            && !automatonPointer.isEmpty())
            switch(automatonPointer.get().getRunningMode())
            {
                case STOPPED:
                    lastTransitions.clear();
                    break;

                default:
                    break;
            }

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
        return key + " -> " + value;
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
        AcceptanceConditions accepting = automaton.getAcceptanceConditions();
        DefaultMutableTreeNode acceptNode = new DefaultMutableTreeNode("Acceptance conditions");

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
                                                                  key.getSecond(), value))));
        varNodes.values().forEach(transitionNode::add);
        rootNode.add(transitionNode);
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

    private class VariableTreeViewNode
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

    private class TransitionTreeViewNode
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
}
