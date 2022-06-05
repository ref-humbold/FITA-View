package fitaview.viewer.tree;

import fitaview.tree.TreeNode;
import fitaview.utils.Pair;

class NodeParameters
{
    private final int treeDepth;
    private TreeNode node;
    private int depth;
    private int xDist;
    private int yDist;

    NodeParameters(TreeNode node, int treeDepth)
    {
        this(0, 0, 0, node, treeDepth);
    }

    NodeParameters(int xDist, int yDist, int depth, TreeNode node, int treeDepth)
    {
        this.treeDepth = treeDepth;
        this.node = node;
        this.depth = depth;
        this.xDist = xDist;
        this.yDist = yDist;
    }

    public Pair<Integer, Integer> getDistance()
    {
        return Pair.make(xDist, yDist);
    }

    public TreeNode getNode()
    {
        return node;
    }

    public NodeParameters getLeftParams()
    {
        int leftXDist;
        int leftYDist;

        if(node.isLeaf())
        {
            leftXDist = xDist - 1;
            leftYDist = yDist + 1;
        }
        else
        {
            leftXDist = xDist > 0 ? xDist - 1 : xDist - 1 - getInvertedDepth();
            leftYDist = xDist > 0 ? yDist + getLeavesNumber() : yDist + 1;
        }

        return new NodeParameters(leftXDist, leftYDist, depth + 1, node.getLeft(), treeDepth);
    }

    public NodeParameters getRightParams()
    {
        int rightXDist;
        int rightYDist;

        if(node.isLeaf())
        {
            rightXDist = xDist + 1;
            rightYDist = yDist + 1;
        }
        else
        {
            rightXDist = xDist >= 0 ? xDist + 1 + getInvertedDepth() : xDist + 1;
            rightYDist = xDist >= 0 ? yDist + 1 : yDist + getLeavesNumber();
        }

        return new NodeParameters(rightXDist, rightYDist, depth + 1, node.getRight(), treeDepth);
    }

    int getLeavesNumber()
    {
        return node.isNull() ? 0 : 1 << getInvertedDepth();
    }

    int getInvertedDepth()
    {
        return treeDepth - depth - 1;
    }
}
