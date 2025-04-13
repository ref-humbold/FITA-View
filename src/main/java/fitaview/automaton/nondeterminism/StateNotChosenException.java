package fitaview.automaton.nondeterminism;

import fitaview.FitaViewException;

public class StateNotChosenException
        extends FitaViewException
{
    private static final long serialVersionUID = -5210455798955068530L;

    public StateNotChosenException(String s)
    {
        super(s);
    }
}
