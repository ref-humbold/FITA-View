package fitaview.automaton.nondeterminism;

import fitaview.FITAViewException;

public class StateNotChosenException
    extends FITAViewException
{
    private static final long serialVersionUID = -5210455798955068530L;

    public StateNotChosenException(String s)
    {
        super(s);
    }
}
