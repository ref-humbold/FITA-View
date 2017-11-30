package ref_humbold.fita_view.automaton;

public class DuplicatedTransitionException
    extends Exception
{
    private static final long serialVersionUID = -5872121228497322894L;

    public DuplicatedTransitionException()
    {
        super();
    }

    public DuplicatedTransitionException(String s)
    {
        super(s);
    }

    public DuplicatedTransitionException(Throwable t)
    {
        super(t);
    }

    public DuplicatedTransitionException(String s, Throwable t)
    {
        super(s, t);
    }
}
