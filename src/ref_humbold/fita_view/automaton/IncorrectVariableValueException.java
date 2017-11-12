package ref_humbold.fita_view.automaton;

public class IncorrectVariableValueException
    extends RuntimeException
{
    private static final long serialVersionUID = -2128138364416571613L;

    public IncorrectVariableValueException()
    {
        super();
    }

    public IncorrectVariableValueException(String s)
    {
        super(s);
    }

    public IncorrectVariableValueException(Throwable t)
    {
        super(t);
    }

    public IncorrectVariableValueException(String s, Throwable t)
    {
        super(s, t);
    }
}
