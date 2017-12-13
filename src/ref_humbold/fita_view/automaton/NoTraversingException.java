package ref_humbold.fita_view.automaton;

public class NoTraversingException
    extends Exception
{
    private static final long serialVersionUID = -2186499829046976659L;

    public NoTraversingException(String s)
    {
        super(s);
    }

    public NoTraversingException(String s, Throwable t)
    {
        super(s, t);
    }
}
