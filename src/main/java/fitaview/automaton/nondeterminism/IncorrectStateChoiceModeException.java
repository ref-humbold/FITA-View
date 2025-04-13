package fitaview.automaton.nondeterminism;

import fitaview.FitaViewException;

public class IncorrectStateChoiceModeException
        extends FitaViewException
{
    private static final long serialVersionUID = 7440785697513785686L;

    public IncorrectStateChoiceModeException(String s)
    {
        super(s);
    }
}
