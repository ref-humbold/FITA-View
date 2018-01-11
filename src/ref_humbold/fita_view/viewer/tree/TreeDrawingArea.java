package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonCurrentNodesSender;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.messaging.SignalReceiver;
import ref_humbold.fita_view.tree.TreeNode;

public class TreeDrawingArea
    extends JPanel
    implements SignalReceiver, MessageReceiver<Iterable<TreeNode>>
{
    private static final long serialVersionUID = -6588296156972565117L;
    private static final int NODE_WIDTH = 5;
    private static final int NODE_HEIGHT = 5;

    private Pointer<TreeNode> treePointer;
    private Iterable<TreeNode> currentNodes;

    public TreeDrawingArea(Pointer<TreeNode> treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonCurrentNodesSender.getInstance().addReceiver(this);

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setPreferredSize(new Dimension((1 << TreeNode.MAX_HEIGHT + 1) * NODE_WIDTH,
                                            TreeNode.MAX_HEIGHT * NODE_HEIGHT * 3));
    }

    @Override
    public void receiveMessage(Message<Iterable<TreeNode>> message)
    {
        currentNodes = message.getParam();
        repaint();
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
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
