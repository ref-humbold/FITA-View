package fitaview.automaton;

import fitaview.FITAViewException;

public class UndefinedAcceptanceException
    extends FITAViewException
{
    private static final long serialVersionUID = -1497929791557889163L;

    public UndefinedAcceptanceException(String s)
    {
        super(s);
    }
}
