package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
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
    private Set<TreeNode> currentNodes = new HashSet<>();
    private Stack<Pair<Integer, Integer>> repeatNodes = new Stack<>();
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

    public Pair<Integer, Integer> getBorderCorner()
    {
        return Pair.make(upperBorder, leftBorder);
    }

    @Override
    public void receiveMessage(Message<Iterable<TreeNode>> message)
    {
        currentNodes.clear();

        for(TreeNode node : message.getParam())
            currentNodes.add(node);

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
        repeatNodes.clear();

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

        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.push(Pair.make(xDist, yDist));

        if(tree.getType() != NodeType.REC && tree.hasChildren())
            drawEdges(graphics, xDist, yDist, leftXDist, rightXDist);
        else if(tree.getType() == NodeType.REC)
            drawRecursiveEdge(graphics, xDist, yDist);

        drawSingleNode(graphics, tree, countXPos(xDist), countYPos(yDist));

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            drawTree(graphics, tree.getLeft(), leftXDist, yDist + 1, numLeaves / 2);
            drawTree(graphics, tree.getRight(), rightXDist, yDist + 1, numLeaves / 2);
        }

        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.pop();
    }

    private void drawRecursiveEdge(Graphics graphics, int xDist, int yDist)
    {
        Pair<Integer, Integer> repeatPos = repeatNodes.peek();
        int yEdgePos = countYPos(yDist) + Y_DIFF / 4;

        graphics.setColor(Color.MAGENTA);
        graphics.drawLine(countXPos(xDist), countYPos(yDist), countXPos(xDist), yEdgePos);
        graphics.drawLine(countXPos(xDist), yEdgePos, countXPos(repeatPos.getFirst()), yEdgePos);
        graphics.drawLine(countXPos(repeatPos.getFirst()), yEdgePos,
                          countXPos(repeatPos.getFirst()),
                          countYPos(repeatPos.getSecond()) + NODE_SIDE / 2);
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

        if(currentNodes.contains(tree))
            graphics.fillOval(xPos - NODE_SIDE / 2, yPos - NODE_SIDE / 2, NODE_SIDE, NODE_SIDE);
    }

    private int countXPos(int xDist)
    {
        return leftBorder + getWidth() / 2 + xDist * X_DIFF;
    }

    private int countYPos(int yDist)
    {
        return upperBorder + getHeight() / 8 + yDist * Y_DIFF;
    }
}
