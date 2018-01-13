package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonCurrentNodesSender;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.messaging.SignalReceiver;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;

public class TreeDrawingArea
    extends JPanel
    implements SignalReceiver, MessageReceiver<Iterable<TreeNode>>
{
    private static final long serialVersionUID = -6588296156972565117L;
    private static final int NODE_SIDE = 16;
    private static final int X_DIFF = 5 + NODE_SIDE / 2;
    private static final int Y_DIFF = 3 * NODE_SIDE;

    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private Iterable<TreeNode> currentNodes;
    private int upperBorder = 0;
    private int leftBorder = 0;

    public TreeDrawingArea(Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonCurrentNodesSender.getInstance().addReceiver(this);

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
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
        upperBorder = 0;
        leftBorder = 0;
        repaint();
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);
        Pair<TreeNode, Integer> pair = treePointer.get();

        if(pair == null)
            drawEmpty(graphics);
        else
            drawTree(graphics, pair.getFirst(), 0, 0, 1 << (pair.getSecond() - 1));

        graphics.setColor(Color.BLUE);
    }

    public void moveArea(int x, int y)
    {
        upperBorder += y;
        leftBorder += x;
        repaint();
    }

    private void drawEmpty(Graphics graphics)
    {
        graphics.setFont(new Font(null, Font.BOLD, 18));
        graphics.drawString("No tree specified...", getWidth() / 4, getHeight() / 2);
    }

    private void drawTree(Graphics graphics, TreeNode tree, int xDist, int yDist, int numLeaves)
    {
        int leftXDist = xDist - numLeaves / 2;
        int rightXDist = xDist + numLeaves / 2;

        if(tree.getType() != NodeType.REC && tree.hasChildren())
            drawEdges(graphics, xDist, yDist, leftXDist, rightXDist);

        drawSingleNode(graphics, tree, countXPos(xDist), countYPos(yDist));

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            drawTree(graphics, tree.getLeft(), leftXDist, yDist + 1, numLeaves / 2);
            drawTree(graphics, tree.getRight(), rightXDist, yDist + 1, numLeaves / 2);
        }
    }

    private void drawEdges(Graphics graphics, int xDist, int yDist, int leftXDist, int rightXDist)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(countXPos(xDist), countYPos(yDist), countXPos(leftXDist),
                          countYPos(yDist + 1));
        graphics.drawLine(countXPos(xDist), countYPos(yDist), countXPos(rightXDist),
                          countYPos(yDist + 1));
    }

    private void drawSingleNode(Graphics graphics, TreeNode tree, int xPos, int yPos)
    {
        switch(tree.getType())
        {
            case NODE:
                graphics.setColor(Color.BLACK);
                break;

            case REPEAT:
                graphics.setColor(Color.ORANGE);
                break;

            case REC:
                graphics.setColor(Color.MAGENTA);
                break;
        }

        graphics.fillRect(xPos - NODE_SIDE / 2, yPos - NODE_SIDE / 2, NODE_SIDE, NODE_SIDE);
    }

    private int countXPos(int xDist)
    {
        return leftBorder + getWidth() / 2 + xDist * X_DIFF;
    }

    private int countYPos(int yDist)
    {
        return upperBorder + getHeight() / 4 + yDist * Y_DIFF;
    }
}
