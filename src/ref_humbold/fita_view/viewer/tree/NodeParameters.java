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
        int leftXDist = xDist - getLeavesNumber() / 2;
        int leftYDist = yDist + 3;

        return new NodeParameters(leftXDist, leftYDist, depth + 1, treeDepth);
    }

    public NodeParameters getRightParams()
    {
        int rightXDist = xDist + getLeavesNumber() / 2;
        int rightYDist = yDist + 3;

        return new NodeParameters(rightXDist, rightYDist, depth + 1, treeDepth);
    }

    private int getLeavesNumber()
    {
        return 1 << (treeDepth - depth - 1);
    }
}
