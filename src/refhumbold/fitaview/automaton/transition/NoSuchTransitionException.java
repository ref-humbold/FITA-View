package refhumbold.fitaview.automaton.transition;

import refhumbold.fitaview.FITAViewException;

public class NoSuchTransitionException
    extends FITAViewException
{
    private static final long serialVersionUID = 3922393365263563771L;

    public NoSuchTransitionException(String s)
    {
        super(s);
    }
}
