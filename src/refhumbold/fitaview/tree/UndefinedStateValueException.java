package refhumbold.fitaview.tree;

import refhumbold.fitaview.FITAViewException;

public class UndefinedStateValueException
    extends FITAViewException
{
    private static final long serialVersionUID = -395187049765591339L;

    public UndefinedStateValueException(String s)
    {
        super(s);
    }
}
