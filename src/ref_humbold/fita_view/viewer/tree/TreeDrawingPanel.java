package ref_humbold.fita_view.viewer.tree;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ref_humbold.fita_view.automaton.AutomatonRunningSender;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;
import ref_humbold.fita_view.message.ParameterizedMessageReceiver;
import ref_humbold.fita_view.tree.TreeNode;

public class TreeDrawingPanel
    extends JPanel
    implements MessageReceiver, ParameterizedMessageReceiver<Iterable<TreeNode>>
{
    private static final long serialVersionUID = -6588296156972565117L;

    private TreePointer treePointer;
    private JLabel testLabel = new JLabel("empty tree");

    public TreeDrawingPanel(TreePointer treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonRunningSender.getInstance().addReceiver(this);

        this.add(testLabel);
    }

    @Override
    public void receiveParameterized(Message<Iterable<TreeNode>> message)
    {

    }

    @Override
    public void receive(Message<Void> message)
    {
        if(message.getSource() == treePointer)
            testLabel.setText(treePointer.isTreeEmpty() ? "empty tree" : "TREE");
    }
}
