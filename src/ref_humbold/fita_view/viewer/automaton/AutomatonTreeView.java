package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;

public class AutomatonTreeView
    extends JTree
    implements MessageReceiver
{
    private static final long serialVersionUID = 5636100205267426054L;

    private Pointer<TreeAutomaton> automatonPointer;
    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    private AutomatonTreeViewRenderer renderer = new AutomatonTreeViewRenderer();

    public AutomatonTreeView(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        this.initializeTree();
        this.setModel(treeModel);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setShowsRootHandles(true);
        this.setCellRenderer(renderer);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override
    public void receive(Message<Void> message)
    {
        if(message.getSource() == automatonPointer)
            initializeTree();
    }

    private void initializeTree()
    {
        rootNode.removeAllChildren();
        rootNode.setUserObject(automatonPointer.get().getTypeName());
        loadAlphabet();
        loadVariables();
        treeModel.reload();
    }

    private void loadAlphabet()
    {
        Set<String> alphabet = automatonPointer.get().getAlphabet();
        DefaultMutableTreeNode alphabetNode = new DefaultMutableTreeNode("Alphabet");

        for(String word : alphabet)
        {
            DefaultMutableTreeNode wordNode = new DefaultMutableTreeNode(word);

            alphabetNode.add(wordNode);
        }

        rootNode.add(alphabetNode);
    }

    private void loadVariables()
    {
        List<Variable> variables = automatonPointer.get().getVariables();
        DefaultMutableTreeNode variablesNode = new DefaultMutableTreeNode("Variables");

        for(Variable var : variables)
        {
            DefaultMutableTreeNode varNode = new DefaultMutableTreeNode(var.getVarName());

            for(String value : var)
            {
                VariableTreeViewNode valueNode = new VariableTreeViewNode(var, value);

                varNode.add(valueNode);
            }

            variablesNode.add(varNode);
        }

        rootNode.add(variablesNode);
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
                    setBackgroundNonSelectionColor(Color.GREEN);
                    setBackgroundSelectionColor(Color.CYAN);
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
            super(value);
            this.variable = variable;
            this.value = value;
        }

        public boolean hasInitValue()
        {
            return variable.getInitValue().equals(value);
        }

        @Override
        public VariableTreeViewNode clone()
        {
            return (VariableTreeViewNode)super.clone();
        }
    }
}
