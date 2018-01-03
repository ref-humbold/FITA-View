package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonRunningSender;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;
import ref_humbold.fita_view.message.ParameterizedMessageReceiver;
import ref_humbold.fita_view.tree.TreeNode;

public class TreeDrawingArea
    extends JPanel
    implements MessageReceiver, ParameterizedMessageReceiver<Iterable<TreeNode>>
{
    private static final long serialVersionUID = -6588296156972565117L;

    private Pointer<TreeNode> treePointer;
    private Iterable<TreeNode> currentNodes;

    public TreeDrawingArea(Pointer<TreeNode> treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonRunningSender.getInstance().addReceiver(this);

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    @Override
    public void receiveParameterized(Message<Iterable<TreeNode>> message)
    {
        if(message.getSource() == AutomatonRunningSender.getInstance())
        {
            currentNodes = message.getParam();
            repaint();
        }
    }

    @Override
    public void receive(Message<Void> message)
    {
        if(message.getSource() == treePointer)
            repaint();
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        drawTree(graphics, treePointer.get());
    }

    private void drawTree(Graphics graphics, TreeNode tree)
    {
        //TODO: draw tree in component
    }
}
