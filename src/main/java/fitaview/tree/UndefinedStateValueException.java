package fitaview.tree;

import fitaview.FitaViewException;

public class UndefinedStateValueException
        extends FitaViewException
{
    private static final long serialVersionUID = -395187049765591339L;

    public UndefinedStateValueException(String s)
    {
        super(s);
    }
}
