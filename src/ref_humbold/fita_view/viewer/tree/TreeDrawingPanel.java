package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pointer;
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

    private Pointer<TreeNode> treePointer;

    public TreeDrawingPanel(Pointer<TreeNode> treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonRunningSender.getInstance().addReceiver(this);

        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(300, 300));
    }

    @Override
    public void receiveParameterized(Message<Iterable<TreeNode>> message)
    {

    }

    @Override
    public void receive(Message<Void> message)
    {
        if(message.getSource() == treePointer)
            this.repaint();
    }

    @Override
    public void paintComponents(Graphics graphics)
    {
        super.paintComponents(graphics);

        graphics.drawString("TREE", 10, 10);
    }
}
