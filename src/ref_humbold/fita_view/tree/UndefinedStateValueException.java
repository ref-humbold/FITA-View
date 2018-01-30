package ref_humbold.fita_view.tree;

import ref_humbold.fita_view.FITAViewException;

public class UndefinedStateValueException
    extends FITAViewException
{
    private static final long serialVersionUID = -395187049765591339L;

    public UndefinedStateValueException(String s)
    {
        super(s);
    }
}
