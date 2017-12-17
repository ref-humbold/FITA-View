package ref_humbold.fita_view.automaton.transition;

public class NoSuchTransitionException
    extends Exception
{
    private static final long serialVersionUID = 3922393365263563771L;

    public NoSuchTransitionException(String s)
    {
        super(s);
    }
}
