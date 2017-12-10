package ref_humbold.fita_view.automaton.traversing;

public class IncorrectTraversingException
    extends Exception
{
    private static final long serialVersionUID = 4567374783045637984L;

    public IncorrectTraversingException(String s)
    {
        super(s);
    }

    public IncorrectTraversingException(String s, Throwable t)
    {
        super(s, t);
    }
}
