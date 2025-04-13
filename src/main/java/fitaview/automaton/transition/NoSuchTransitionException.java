package fitaview.automaton.transition;

import fitaview.FitaViewException;

public class NoSuchTransitionException
        extends FitaViewException
{
    private static final long serialVersionUID = 3922393365263563771L;

    public NoSuchTransitionException(String s)
    {
        super(s);
    }
}
