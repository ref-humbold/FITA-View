package ref_humbold.fita_view.automaton;

public class IllegalValueException
    extends Exception
{
    private static final long serialVersionUID = -2128138364416571613L;

    public IllegalValueException()
    {
        super();
    }

    public IllegalValueException(String s)
    {
        super(s);
    }

    public IllegalValueException(Throwable t)
    {
        super(t);
    }

    public IllegalValueException(String s, Throwable t)
    {
        super(s, t);
    }
}
