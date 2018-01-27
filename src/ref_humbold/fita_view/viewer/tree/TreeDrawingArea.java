package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.*;
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
import ref_humbold.fita_view.viewer.UserMessageBox;

public class TreeDrawingArea
    extends JPanel
    implements SignalReceiver, MessageReceiver<Iterable<TreeNode>>, MouseListener
{
    static final int MAX_ZOOM = 4;
    private static final int NODE_SIDE = 6;
    private static final long serialVersionUID = -6588296156972565117L;

    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private Set<TreeNode> currentNodes = new HashSet<>();
    private Stack<NodeParameters> repeatNodes = new Stack<>();
    private Map<Pair<Integer, Integer>, TreeNode> nodesPoints = new HashMap<>();
    private int horizontalAxis = 0;
    private int verticalAxis = 0;
    private int zoomLevel = 0;
    private int unitFactor = 1;

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

    public int getZoomLevel()
    {
        return this.zoomLevel;
    }

    public Pair<Integer, Integer> getAxisPoint()
    {
        return Pair.make(horizontalAxis, verticalAxis);
    }

    @Override
    public void receiveMessage(Message<Iterable<TreeNode>> message)
    {
        currentNodes.clear();
        message.getParam().forEach(currentNodes::add);
        repaint();
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        Pair<TreeNode, Integer> pair = treePointer.get();

        nodesPoints.clear();
        centralize();

        if(pair != null)
        {
            NodeParameters rootParams = new NodeParameters(pair.getSecond());

            saveNodesPoints(pair.getFirst(), rootParams);
        }
    }

    public void moveArea(int x, int y)
    {
        horizontalAxis = checkBounds(horizontalAxis + y, -700, 100);
        verticalAxis = checkBounds(verticalAxis + x, -400, 400);
        repaint();
    }

    public void centralize()
    {
        horizontalAxis = 0;
        verticalAxis = 0;
        repaint();
    }

    public void zoom(int i)
    {
        zoomLevel = checkBounds(zoomLevel + i, 0, MAX_ZOOM);
        unitFactor = (zoomLevel / 2 + 1);
        repaint();
    }

    public void zeroZoom()
    {
        zoomLevel = 0;
        unitFactor = 1;
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
        {
            NodeParameters rootParams = new NodeParameters(pair.getSecond());

            drawTree(graphics, pair.getFirst(), rootParams);
        }

        graphics.setColor(Color.BLUE);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        Pair<Integer, Integer> mouseDist = countDistPos(mouseEvent.getX(), mouseEvent.getY());

        if(mouseDist == null)
            return;

        TreeNode node = nodesPoints.get(mouseDist);

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

        node.getState()
            .forEach((key, value) -> builder.append("  ")
                                            .append(key.getVarName())
                                            .append(" => \'")
                                            .append(value)
                                            .append("\'\n"));

        return builder.toString();
    }

    private void saveNodesPoints(TreeNode tree, NodeParameters parameters)
    {
        nodesPoints.put(parameters.getDistance(), tree);

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            saveNodesPoints(tree.getLeft(), parameters.getLeftParams());
            saveNodesPoints(tree.getRight(), parameters.getRightParams());
        }
    }

    private void drawEmpty(Graphics graphics)
    {
        graphics.setFont(new Font(null, Font.BOLD, 18));
        graphics.drawString("No tree specified...", getWidth() / 4, getHeight() / 2);
    }

    private void drawTree(Graphics graphics, TreeNode tree, NodeParameters parameters)
    {
        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.push(parameters);

        if(tree.getType() != NodeType.REC && tree.hasChildren())
            drawEdges(graphics, parameters);
        else if(tree.getType() == NodeType.REC)
            drawRecursiveEdge(graphics, parameters);

        drawSingleNode(graphics, tree, parameters);

        if(tree.getType() != NodeType.REC && tree.hasChildren())
        {
            drawTree(graphics, tree.getLeft(), parameters.getLeftParams());
            drawTree(graphics, tree.getRight(), parameters.getRightParams());
        }

        if(tree.getType() == NodeType.REPEAT)
            repeatNodes.pop();
    }

    private void drawRecursiveEdge(Graphics graphics, NodeParameters parameters)
    {
        Pair<Integer, Integer> position = countNodePos(parameters);
        Pair<Integer, Integer> repeatPosition = countNodePos(repeatNodes.peek());
        int yEdgePos = position.getFirst() + 3 * NODE_SIDE / 4;

        graphics.setColor(Color.MAGENTA);
        graphics.drawLine(position.getFirst(), position.getSecond(), position.getFirst(), yEdgePos);
        graphics.drawLine(position.getFirst(), yEdgePos, repeatPosition.getFirst(), yEdgePos);
        graphics.drawLine(repeatPosition.getFirst(), yEdgePos, repeatPosition.getFirst(),
                          repeatPosition.getSecond() + NODE_SIDE / 2);
    }

    private void drawEdges(Graphics graphics, NodeParameters parameters)
    {
        Pair<Integer, Integer> position = countNodePos(parameters);
        Pair<Integer, Integer> leftPosition = countNodePos(parameters.getLeftParams());
        Pair<Integer, Integer> rightPosition = countNodePos(parameters.getRightParams());

        graphics.setColor(Color.BLACK);
        graphics.drawLine(position.getFirst(), position.getSecond(), leftPosition.getFirst(),
                          leftPosition.getSecond());
        graphics.drawLine(position.getFirst(), position.getSecond(), rightPosition.getFirst(),
                          rightPosition.getSecond());
    }

    private void drawSingleNode(Graphics graphics, TreeNode node, NodeParameters parameters)
    {
        switch(node.getType())
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

        Pair<Integer, Integer> position = countNodePos(parameters);
        int nodeSide = countNodeSide();
        int cornerX = position.getFirst() - nodeSide / 2;
        int cornerY = position.getSecond() - nodeSide / 2;

        graphics.fillRect(cornerX, cornerY, nodeSide, nodeSide);

        if(currentNodes.contains(node))
        {
            graphics.setColor(Color.GREEN);
            graphics.fillOval(cornerX, cornerY, nodeSide, nodeSide);
        }

        if(zoomLevel == MAX_ZOOM)
        {
            FontMetrics metrics = graphics.getFontMetrics();
            Rectangle2D rect = metrics.getStringBounds(node.getLabel(), graphics);

            graphics.setColor(Color.ORANGE);
            graphics.fillRect(cornerX, cornerY - nodeSide / 2 - metrics.getAscent(),
                              (int)Math.ceil(rect.getWidth()) + 2,
                              (int)Math.ceil(rect.getHeight()));

            graphics.setColor(Color.BLACK);
            graphics.drawString(node.getLabel(), cornerX + 1, cornerY - nodeSide / 2);
        }
    }

    private Pair<Integer, Integer> countRootPos()
    {
        return Pair.make(verticalAxis + getWidth() / 2, horizontalAxis + getHeight() / 8);
    }

    private Pair<Integer, Integer> countNodePos(NodeParameters parameters)
    {
        Pair<Integer, Integer> rootPos = countRootPos();
        Pair<Integer, Integer> dist = parameters.getDistance();

        return Pair.make(rootPos.getFirst() + dist.getFirst() * countUnit(),
                         rootPos.getSecond() + dist.getSecond() * countUnit());
    }

    private Pair<Integer, Integer> countDistPos(int x, int y)
    {
        Pair<Integer, Integer> rootPos = countRootPos();

        int posX = roundPos(x - rootPos.getFirst());
        int posY = roundPos(y - rootPos.getSecond());

        if(posX % unitFactor != 0 || posY % unitFactor != 0)
            return null;

        return Pair.make(posX / unitFactor, posY / unitFactor);
    }

    private int countUnit()
    {
        return unitFactor * countNodeSide();
    }

    private int countNodeSide()
    {
        return zoomLevel + zoomLevel + NODE_SIDE;
    }

    private int roundPos(double pos)
    {
        return (int)Math.rint(pos / countNodeSide());
    }

    private int checkBounds(int value, int minimum, int maximum)
    {
        return Math.min(Math.max(value, minimum), maximum);
    }
}
