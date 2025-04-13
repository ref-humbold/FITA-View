package fitaview.tree;

import fitaview.FitaViewException;

public class NodeHasParentException
        extends FitaViewException
{
    private static final long serialVersionUID = 5710948952820613251L;

    public NodeHasParentException(String s)
    {
        super(s);
    }
}
