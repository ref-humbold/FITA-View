package refhumbold.fitaview.automaton.nondeterminism;

import refhumbold.fitaview.FITAViewException;

public class IncorrectStateChoiceModeException
    extends FITAViewException
{
    private static final long serialVersionUID = 7440785697513785686L;

    public IncorrectStateChoiceModeException(String s)
    {
        super(s);
    }
}
