package ref_humbold.fita_view.viewer.tree;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.TreeNode;

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

    private NodeParameters(int xDist, int yDist, int depth, TreeNode node, int treeDepth)
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
        return this.node;
    }

    public NodeParameters getLeftParams()
    {
        int leftXDist;
        int leftYDist;

        if(node.isLeaf())
        {
            leftXDist = xDist == 0 ? -1 : xDist > 0 ? xDist : xDist - 1;
            leftYDist = xDist == 0 ? 1 : xDist > 0 ? yDist + 1 : yDist;
        }
        else
        {
            leftXDist = xDist == 0 ? -4 : xDist > 0 ? xDist : xDist - 1 - getInvertedDepth();
            leftYDist = xDist == 0 ? 4 : xDist > 0 ? yDist + getLeavesNumber() : yDist;
        }

        return new NodeParameters(leftXDist, leftYDist, depth + 1, node.getLeft(), treeDepth);
    }

    public NodeParameters getRightParams()
    {
        int rightXDist;
        int rightYDist;

        if(node.isLeaf())
        {
            rightXDist = xDist == 0 ? 2 : xDist > 0 ? xDist + 1 : xDist;
            rightYDist = xDist == 0 ? 2 : xDist > 0 ? yDist : yDist + 1;
        }
        else
        {
            rightXDist = xDist == 0 ? 4 : xDist > 0 ? xDist + 1 + getInvertedDepth() : xDist;
            rightYDist = xDist == 0 ? 4 : xDist > 0 ? yDist : yDist + getLeavesNumber();
        }

        return new NodeParameters(rightXDist, rightYDist, depth + 1, node.getRight(), treeDepth);
    }

    private int getLeavesNumber()
    {
        return 1 << getInvertedDepth();
    }

    private int getInvertedDepth()
    {
        return treeDepth - depth - 1;
    }
}
