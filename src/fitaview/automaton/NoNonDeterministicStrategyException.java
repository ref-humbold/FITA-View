package fitaview.automaton;

import fitaview.FITAViewException;

public class NoNonDeterministicStrategyException
    extends FITAViewException
{
    private static final long serialVersionUID = -532773326620067556L;

    public NoNonDeterministicStrategyException(String s)
    {
        super(s);
    }
}
