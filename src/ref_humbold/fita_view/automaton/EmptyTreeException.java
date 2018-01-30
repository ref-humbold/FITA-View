package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.FITAViewException;

public class EmptyTreeException
    extends FITAViewException
{
    private static final long serialVersionUID = 8624749288560161357L;

    public EmptyTreeException(String s)
    {
        super(s);
    }
}
