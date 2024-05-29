package fitaview.automaton;

import fitaview.FitaViewException;

public class UndefinedAcceptanceException
        extends FitaViewException
{
    private static final long serialVersionUID = -1497929791557889163L;

    public UndefinedAcceptanceException(String s)
    {
        super(s);
    }
}
