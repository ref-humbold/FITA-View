package fitaview.automaton.traversing;

import fitaview.FitaViewException;

public class IncorrectTraversingException
        extends FitaViewException
{
    private static final long serialVersionUID = 4567374783045637984L;

    public IncorrectTraversingException(String s)
    {
        super(s);
    }
}
