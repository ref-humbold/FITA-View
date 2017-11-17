package ref_humbold.fita_view.automaton;

public class IncorrectValueException
    extends RuntimeException
{
    private static final long serialVersionUID = -2128138364416571613L;

    public IncorrectValueException()
    {
        super();
    }

    public IncorrectValueException(String s)
    {
        super(s);
    }

    public IncorrectValueException(Throwable t)
    {
        super(t);
    }

    public IncorrectValueException(String s, Throwable t)
    {
        super(s, t);
    }
}
