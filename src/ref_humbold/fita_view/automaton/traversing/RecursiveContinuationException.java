package ref_humbold.fita_view.automaton.traversing;

import ref_humbold.fita_view.FITAViewException;

public class RecursiveContinuationException
    extends FITAViewException
{
    private static final long serialVersionUID = -837861842265501474L;

    public RecursiveContinuationException(String s)
    {
        super(s);
    }
}
