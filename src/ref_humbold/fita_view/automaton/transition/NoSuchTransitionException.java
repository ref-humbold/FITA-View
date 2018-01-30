package ref_humbold.fita_view.automaton.transition;

import ref_humbold.fita_view.FITAViewException;

public class NoSuchTransitionException
    extends FITAViewException
{
    private static final long serialVersionUID = 3922393365263563771L;

    public NoSuchTransitionException(String s)
    {
        super(s);
    }
}
