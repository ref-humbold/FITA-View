package ref_humbold.fita_view.automaton;

public class ImproperTraversingException
    extends RuntimeException
{
    private static final long serialVersionUID = 2447071178631351066L;

    public ImproperTraversingException()
    {
        super();
    }

    public ImproperTraversingException(String s)
    {
        super(s);
    }

    public ImproperTraversingException(Throwable t)
    {
        super(t);
    }

    public ImproperTraversingException(String s, Throwable t)
    {
        super(s, t);
    }
}
