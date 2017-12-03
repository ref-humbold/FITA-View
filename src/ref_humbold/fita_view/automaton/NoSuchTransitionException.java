package ref_humbold.fita_view.automaton;

public class NoSuchTransitionException
    extends Exception
{
    private static final long serialVersionUID = 3922393365263563771L;

    public NoSuchTransitionException()
    {
        super();
    }

    public NoSuchTransitionException(String s)
    {
        super(s);
    }

    public NoSuchTransitionException(Throwable t)
    {
        super(t);
    }

    public NoSuchTransitionException(String s, Throwable t)
    {
        super(s, t);
    }
}
