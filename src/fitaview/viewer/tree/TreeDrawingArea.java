package fitaview.viewer.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import fitaview.Pair;
import fitaview.Pointer;
import fitaview.automaton.AutomatonCurrentNodesSender;
import fitaview.messaging.Message;
import fitaview.messaging.MessageReceiver;
import fitaview.messaging.SignalReceiver;
import fitaview.tree.NodeType;
import fitaview.tree.TreeNode;
import fitaview.viewer.UserMessageBox;

public class TreeDrawingArea
    extends JPanel
    implements SignalReceiver, MessageReceiver<Iterable<TreeNode>>, MouseListener
{
    static final int MAX_ZOOM = 4;
    private static final int NODE_SIDE = 6;
    private static final long serialVersionUID = -6588296156972565117L;

    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private Stack<NodeParameters> repeatNodes = new Stack<>();
    private Map<Pair<Integer, Integer>, TreeNode> nodesPoints = new HashMap<>();
    private Set<TreeNode> currentNodes = new HashSet<>();
    private int horizontalAxis = 0;
    private int verticalAxis = 0;
    private int zoomLevel = 0;
    private int unitFactor = 1;

    public TreeDrawingArea(Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.treePointer = treePointer;
        treePointer.addReceiver(this);
        AutomatonCurrentNodesSender.getInstance().addReceiver(this);

        addMouseListener(this);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    public int getZoomLevel()
    {
        return zoomLevel;
    }

    public Pair<Integer, Integer> getAxisPoint()
    {
        return Pair.make(horizontalAxis, verticalAxis);
    }

    int getStepUnit()
    {
        return 16 * countNodeSide();
    }

    @Override
    public void receiveMessage(Message<Iterable<TreeNode>> message)
    {
        currentNodes.clear();
        message.getParam().forEach(currentNodes::add);
        revalidate();
        repaint();
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        Pair<TreeNode, Integer> pair = treePointer.get();

        nodesPoints.clear();
        currentNodes.clear();
        centralize();

        if(pair != null)
        {
            NodeParameters rootParams = new NodeParameters(pair.getFirst(), pair.getSecond());

            saveNodesPoints(pair.getFirst(), rootParams);
        }
    }

    public void moveArea(int x, int y)
    {
        horizontalAxis = checkBounds(horizontalAxis + y * getStepUnit(), -(1 << 24), 1 << 10);
        verticalAxis = checkBounds(verticalAxis + x * getStepUnit(), -(1 << 24), 1 << 24);
        revalidate();
        repaint();
    }

    public void centralize()
    {
        horizontalAxis = 0;
        verticalAxis = 0;
        revalidate();
        repaint();
    }

    public void zoom(int i)
    {
        zoomLevel = checkBounds(zoomLevel + i, 0, MAX_ZOOM);
        unitFactor = zoomLevel / 2 + 1;
        revalidate();
        repaint();
    }

    public void zeroZoom()
    {
        zoomLevel = 0;
        unitFactor = 1;
        revalidate();
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
            NodeParameters rootParams = new NodeParameters(pair.getFirst(), pair.getSecond());

            drawTree(graphics, rootParams);
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

        node.getStateWithNulls()
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

        if(isInnerNode(tree))
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

    private void drawTree(Graphics graphics, NodeParameters parameters)
    {
        TreeNode node = parameters.getNode();

        if(node.getType() == NodeType.REPEAT)
            repeatNodes.push(parameters);

        if(isInnerNode(node))
            drawEdges(graphics, parameters);
        else if(node.getType() == NodeType.REC)
            drawRecursiveEdge(graphics, parameters);

        drawSingleNode(graphics, parameters);

        if(isInnerNode(node))
        {
            drawTree(graphics, parameters.getLeftParams());
            drawTree(graphics, parameters.getRightParams());
        }

        if(node.getType() == NodeType.REPEAT)
            repeatNodes.pop();
    }

    private void drawRecursiveEdge(Graphics graphics, NodeParameters parameters)
    {
        Pair<Integer, Integer> recPosition = countNodePos(parameters);
        Pair<Integer, Integer> repeatPosition = countNodePos(repeatNodes.peek());
        int horizontalEdgePos = recPosition.getSecond() + 3 * countUnit() / 4;

        graphics.setColor(Color.MAGENTA);
        graphics.drawLine(recPosition.getFirst(), recPosition.getSecond(), recPosition.getFirst(),
                          horizontalEdgePos);
        graphics.drawLine(recPosition.getFirst(), horizontalEdgePos, repeatPosition.getFirst(),
                          horizontalEdgePos);
        graphics.drawLine(repeatPosition.getFirst(), horizontalEdgePos, repeatPosition.getFirst(),
                          repeatPosition.getSecond() + countNodeSide() / 2);
    }

    private void drawEdges(Graphics graphics, NodeParameters parameters)
    {
        Pair<Integer, Integer> position = countNodePos(parameters);
        Pair<Integer, Integer> leftPosition = countNodePos(parameters.getLeftParams());
        Pair<Integer, Integer> rightPosition = countNodePos(parameters.getRightParams());

        if(parameters.getNode().isLeaf())
            graphics.setColor(Color.LIGHT_GRAY);
        else
            graphics.setColor(Color.BLACK);

        graphics.drawLine(position.getFirst(), position.getSecond(), leftPosition.getFirst(),
                          leftPosition.getSecond());
        graphics.drawLine(position.getFirst(), position.getSecond(), rightPosition.getFirst(),
                          rightPosition.getSecond());
    }

    private void drawSingleNode(Graphics graphics, NodeParameters parameters)
    {
        TreeNode node = parameters.getNode();

        switch(node.getType())
        {
            case NODE:
                graphics.setColor(Color.BLACK);
                break;

            case REPEAT:
                graphics.setColor(new Color(128, 0, 255));
                break;

            case REC:
                graphics.setColor(Color.MAGENTA);
                break;

            case NULL:
                graphics.setColor(Color.LIGHT_GRAY);
        }

        Pair<Integer, Integer> position = countNodePos(parameters);

        if(isInside(position))
        {
            return;
        }

        int nodeSide = countNodeSide();
        int cornerX = position.getFirst() - nodeSide / 2;
        int cornerY = position.getSecond() - nodeSide / 2;

        if(node.isNull())
            graphics.fillOval(cornerX, cornerY + nodeSide / 4, nodeSide, nodeSide / 2);
        else
            graphics.fillRect(cornerX, cornerY, nodeSide, nodeSide);

        if(currentNodes.contains(node))
        {
            graphics.setColor(Color.GREEN);
            graphics.fillOval(cornerX, cornerY, nodeSide, nodeSide);
        }

        if(!node.isNull() && zoomLevel >= MAX_ZOOM / 2)
            drawNodeLabel(graphics, node, cornerX, cornerY);
    }

    private boolean isInside(Pair<Integer, Integer> position)
    {
        return position.getFirst() < -getStepUnit()
            || position.getFirst() > getWidth() + getStepUnit()
            || position.getSecond() < -getStepUnit()
            || position.getSecond() > getHeight() + getStepUnit();
    }

    private void drawNodeLabel(Graphics graphics, TreeNode node, int cornerX, int cornerY)
    {
        graphics.setFont(new Font(null, Font.PLAIN, 12));

        FontMetrics metrics = graphics.getFontMetrics();
        Rectangle2D rect = metrics.getStringBounds(node.getLabel(), graphics);

        graphics.setColor(Color.ORANGE);
        graphics.fillRect(cornerX, cornerY - metrics.getAscent(),
                          (int)Math.ceil(rect.getWidth()) + 2, (int)Math.ceil(rect.getHeight()));

        graphics.setColor(Color.BLACK);
        graphics.drawString(node.getLabel(), cornerX + 1, cornerY);
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

    private int countNodeSide()
    {
        return zoomLevel + zoomLevel + NODE_SIDE;
    }

    private int countUnit()
    {
        return unitFactor * countNodeSide();
    }

    private int roundPos(double pos)
    {
        return (int)Math.rint(pos / countNodeSide());
    }

    private int checkBounds(int value, int minimum, int maximum)
    {
        return Math.min(Math.max(value, minimum), maximum);
    }

    private boolean isInnerNode(TreeNode node)
    {
        return !node.isNull() && node.getType() != NodeType.REC;
    }
}
