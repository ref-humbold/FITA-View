package fitaview.tree;

import fitaview.FITAViewException;

public class NodeHasParentException
    extends FITAViewException
{
    private static final long serialVersionUID = 5710948952820613251L;

    public NodeHasParentException(String s)
    {
        super(s);
    }
}
