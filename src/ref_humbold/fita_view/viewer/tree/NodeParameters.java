package ref_humbold.fita_view.viewer.tree;

import ref_humbold.fita_view.Pair;

class NodeParameters
{
    private int treeDepth;
    private int xDist;
    private int yDist;

    public NodeParameters(int treeDepth)
    {
        this(0, 0, treeDepth);
    }

    public NodeParameters(int xDist, int yDist, int treeDepth)
    {
        this.treeDepth = treeDepth;
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
        int leftYDist = yDist + 1;

        return new NodeParameters(leftXDist, leftYDist, treeDepth);
    }

    public NodeParameters getRightParams()
    {
        int rightXDist = xDist + getLeavesNumber() / 2;
        int rightYDist = yDist + 1;

        return new NodeParameters(rightXDist, rightYDist, treeDepth);
    }

    private int getLeavesNumber()
    {
        return 1 << (treeDepth - yDist - 1);
    }
}
