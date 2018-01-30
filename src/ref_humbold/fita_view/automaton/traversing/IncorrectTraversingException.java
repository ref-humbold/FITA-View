package ref_humbold.fita_view.automaton.traversing;

import ref_humbold.fita_view.FITAViewException;

public class IncorrectTraversingException
    extends FITAViewException
{
    private static final long serialVersionUID = 4567374783045637984L;

    public IncorrectTraversingException(String s)
    {
        super(s);
    }
}
