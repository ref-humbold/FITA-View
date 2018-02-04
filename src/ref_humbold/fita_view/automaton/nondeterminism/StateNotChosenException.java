package ref_humbold.fita_view.automaton.nondeterminism;

import ref_humbold.fita_view.FITAViewException;

public class StateNotChosenException
    extends FITAViewException
{
    private static final long serialVersionUID = -5210455798955068530L;

    public StateNotChosenException(String s)
    {
        super(s);
    }
}
