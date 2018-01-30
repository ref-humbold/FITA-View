package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.FITAViewException;

public class UndefinedAcceptanceException
    extends FITAViewException
{
    private static final long serialVersionUID = -1497929791557889163L;

    public UndefinedAcceptanceException(String s)
    {
        super(s);
    }
}
