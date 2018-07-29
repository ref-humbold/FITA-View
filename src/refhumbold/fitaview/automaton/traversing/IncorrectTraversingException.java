package refhumbold.fitaview.automaton.traversing;

import refhumbold.fitaview.FITAViewException;

public class IncorrectTraversingException
    extends FITAViewException
{
    private static final long serialVersionUID = 4567374783045637984L;

    public IncorrectTraversingException(String s)
    {
        super(s);
    }
}
