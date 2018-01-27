package ref_humbold.fita_view.viewer.tree;

import ref_humbold.fita_view.Pair;

class NodeParameters
{
    private int treeDepth;
    private int depth;
    private int xDist;
    private int yDist;

    NodeParameters(int treeDepth)
    {
        this(0, 0, 0, treeDepth);
    }

    private NodeParameters(int xDist, int yDist, int depth, int treeDepth)
    {
        this.treeDepth = treeDepth;
        this.depth = depth;
        this.xDist = xDist;
        this.yDist = yDist;
    }

    public Pair<Integer, Integer> getDistance()
    {
        return Pair.make(xDist, yDist);
    }

    public NodeParameters getLeftParams()
    {
        int leftXDist = xDist == 0 ? -4 : xDist > 0 ? xDist : xDist - 2;
        int leftYDist = xDist == 0 ? 4 : xDist > 0 ? yDist + getLeavesNumber() : yDist;

        return new NodeParameters(leftXDist, leftYDist, depth + 1, treeDepth);
    }

    public NodeParameters getRightParams()
    {
        int rightXDist = xDist == 0 ? 4 : xDist > 0 ? xDist + 2 : xDist;
        int rightYDist = xDist == 0 ? 4 : xDist > 0 ? yDist : yDist + getLeavesNumber();

        return new NodeParameters(rightXDist, rightYDist, depth + 1, treeDepth);
    }

    private int getLeavesNumber()
    {
        return 1 << (treeDepth - depth - 1);
    }
}
