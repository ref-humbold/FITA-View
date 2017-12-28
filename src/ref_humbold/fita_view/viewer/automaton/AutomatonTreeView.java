package ref_humbold.fita_view.viewer.automaton;

import java.util.List;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
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

    public AutomatonTreeView()
    {
        super();

        AutomatonNullableProxy.getInstance().addReceiver(this);
        this.setModel(treeModel);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setShowsRootHandles(true);
    }

    @Override
    public void receive(Command<Void> command)
    {
        if(command.getSource() == AutomatonNullableProxy.getInstance())
        {
            rootNode.removeAllChildren();
            loadAlphabet();
            loadVariables();
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
                DefaultMutableTreeNode valueNode = new DefaultMutableTreeNode(value);

                varNode.add(valueNode);
            }

            variablesNode.add(varNode);
        }

        rootNode.add(variablesNode);
    }
}
