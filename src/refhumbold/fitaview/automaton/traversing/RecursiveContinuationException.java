package refhumbold.fitaview.automaton.traversing;

import refhumbold.fitaview.FITAViewException;

public class RecursiveContinuationException
    extends FITAViewException
{
    private static final long serialVersionUID = -837861842265501474L;

    public RecursiveContinuationException(String s)
    {
        super(s);
    }
}
