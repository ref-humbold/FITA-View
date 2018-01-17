package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonCurrentNodesSender;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.messaging.SignalReceiver;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class TreeDrawingArea
    extends JPanel
    implements SignalReceiver, MessageReceiver<Iterable<TreeNode>>, MouseListener
{
    private static final long serialVersionUID = -6588296156972565117L;
    private static final int NODE_SIDE = 16;

    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private Set<TreeNode> currentNodes = new HashSet<>();
    private Stack<Pair<Integer, Integer>> repeatNodes = new Stack<>();
    private Map<Pair<Integer, Integer>, TreeNode> nodesPoints = new HashMap<>();
    private int horizontalAxis = 0;
    private int verticalAxis = 0;

    public TreeDrawingArea(Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.treePointer = treePointer;
        this.treePointer.addReceiver(this);
        AutomatonCurrentNodesSender.getInstance().addReceiver(this);
        this.addMouseListener(this);

        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public Pair<Integer, Integer> getAxisPoint()
    {
        return Pair.make(horizontalAxis, verticalAxis);
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
        Pair<TreeNode, Integer> pair = treePointer.get();

        horizontalAxis = 0;
        verticalAxis = 0;
        nodesPoints.clear();

        if(pair != null)
            saveNodesPoints(pair.getFirst(), 0, 0, 1 << (pair.getSecond() - 1));

        repaint();
    }

    public void moveArea(int x, int y)
    {
        horizontalAxis += y;
        verticalAxis += x;
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

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        int xMouse = countDistPosX(mouseEvent.getX());
        int yMouse = countDistPosY(mouseEvent.getY());
        TreeNode node = nodesPoints.get(Pair.make(xMouse, yMouse));

        if(mouseEvent.getButton() == MouseEvent.BUTTON1 && node != null)
            UserMessageBox.showInfo(node.getType().toString(), getNodeInfo(node));
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {
    }

    private String getNodeInfo(TreeNode node)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("LABEL: \'").append(node.getLabel()).append("\'\n");
        builder.append("STATE:\n");

        for(Map.Entry<Variable, String> entry : node.getState().entrySet())
        {
            builder.append("  ")
                   .append(entry.getKey())
                   .append(" => \'")
                   .append(entry.getValue())
                   .append("\'\n");
        }

        return builder.toString();
    }

    private void saveNodesPoints(TreeNode tree, int xDist, int yDist, int numLeaves)
    {
        nodesPoints.put(Pair.make(xDist, yDist), tree);

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            saveNodesPoints(tree.getLeft(), xDist - numLeaves / 2, yDist + 3, numLeaves / 2);
            saveNodesPoints(tree.getRight(), xDist + numLeaves / 2, yDist + 3, numLeaves / 2);
        }
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
        int nextYDist = yDist + 3;

        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.push(Pair.make(xDist, yDist));

        if(tree.getType() != NodeType.REC && tree.hasChildren())
            drawEdges(graphics, xDist, yDist, leftXDist, rightXDist, nextYDist);
        else if(tree.getType() == NodeType.REC)
            drawRecursiveEdge(graphics, xDist, yDist);

        drawSingleNode(graphics, tree, countNodePosX(xDist), countNodePosY(yDist));

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            drawTree(graphics, tree.getLeft(), leftXDist, nextYDist, numLeaves / 2);
            drawTree(graphics, tree.getRight(), rightXDist, nextYDist, numLeaves / 2);
        }

        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.pop();
    }

    private void drawRecursiveEdge(Graphics graphics, int xDist, int yDist)
    {
        Pair<Integer, Integer> repeatPos = repeatNodes.peek();
        int yEdgePos = countNodePosY(yDist) + 3 * NODE_SIDE / 4;

        graphics.setColor(Color.MAGENTA);
        graphics.drawLine(countNodePosX(xDist), countNodePosY(yDist), countNodePosX(xDist),
                          yEdgePos);
        graphics.drawLine(countNodePosX(xDist), yEdgePos, countNodePosX(repeatPos.getFirst()),
                          yEdgePos);
        graphics.drawLine(countNodePosX(repeatPos.getFirst()), yEdgePos,
                          countNodePosX(repeatPos.getFirst()),
                          countNodePosY(repeatPos.getSecond()) + NODE_SIDE / 2);
    }

    private void drawEdges(Graphics graphics, int xDist, int yDist, int leftXDist, int rightXDist,
                           int nextYDist)
    {
        graphics.setColor(Color.BLACK);
        graphics.drawLine(countNodePosX(xDist), countNodePosY(yDist), countNodePosX(leftXDist),
                          countNodePosY(nextYDist));
        graphics.drawLine(countNodePosX(xDist), countNodePosY(yDist), countNodePosX(rightXDist),
                          countNodePosY(nextYDist));
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

    private int countNodePosX(int xDist)
    {
        return countRootPosX() + xDist * NODE_SIDE;
    }

    private int countNodePosY(int yDist)
    {
        return countRootPosY() + yDist * NODE_SIDE;
    }

    private int countRootPosX()
    {
        return verticalAxis + getWidth() / 2;
    }

    private int countRootPosY()
    {
        return horizontalAxis + getHeight() / 8;
    }

    private int countDistPosX(int x)
    {
        return roundPos(x - countRootPosX());
    }

    private int countDistPosY(int y)
    {
        return roundPos(y - countRootPosY());
    }

    private int roundPos(double pos)
    {
        return (int)Math.rint(pos / NODE_SIDE);
    }
}
