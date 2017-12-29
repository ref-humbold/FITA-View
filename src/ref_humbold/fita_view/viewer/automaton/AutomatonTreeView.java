package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import ref_humbold.fita_view.automaton.AutomatonNullableProxy;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.command.Command;
import ref_humbold.fita_view.command.CommandReceiver;

public class AutomatonTreeView
    extends JTree
    implements CommandReceiver<Void>
{
    private static final long serialVersionUID = 5636100205267426054L;

    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Automaton");
    private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    private AutomatonTreeViewRenderer renderer = new AutomatonTreeViewRenderer();

    public AutomatonTreeView()
    {
        super();

        AutomatonNullableProxy.getInstance().addReceiver(this);
        this.setModel(treeModel);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setShowsRootHandles(true);
        this.setCellRenderer(renderer);
    }

    @Override
    public void receive(Command<Void> command)
    {
        if(command.getSource() == AutomatonNullableProxy.getInstance())
        {
            rootNode.removeAllChildren();
            loadAlphabet();
            loadVariables();
            treeModel.reload();
        }
    }

    private void loadAlphabet()
    {
        Set<String> alphabet = AutomatonNullableProxy.getInstance().getAlphabet();
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
        List<Variable> variables = AutomatonNullableProxy.getInstance().getVariables();
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
