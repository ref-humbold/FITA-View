package ref_humbold.fita_view.automaton;

public class IllegalVariableValueException
    extends Exception
{
    private static final long serialVersionUID = -2128138364416571613L;

    public IllegalVariableValueException()
    {
        super();
    }

    public IllegalVariableValueException(String s)
    {
        super(s);
    }

    public IllegalVariableValueException(Throwable t)
    {
        super(t);
    }

    public IllegalVariableValueException(String s, Throwable t)
    {
        super(s, t);
    }
}
