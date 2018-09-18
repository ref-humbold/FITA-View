package refhumbold.fitaview.automaton.nondeterminism;

import refhumbold.fitaview.FITAViewException;

public class StateNotChosenException
    extends FITAViewException
{
    private static final long serialVersionUID = -5210455798955068530L;

    public StateNotChosenException(String s)
    {
        super(s);
    }
}