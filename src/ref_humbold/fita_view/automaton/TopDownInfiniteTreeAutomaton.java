package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Map;

// TODO: implement infinite tree automaton

public abstract class TopDownInfiniteTreeAutomaton
    extends TopDownAutomaton
{
    public TopDownInfiniteTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public boolean isAccepted()
    {
        return false;
    }

    @Override
    public void makeStepForward()
    {

    }

    @Override
    protected boolean checkAcceptance(Map<Variable, String> state)
    {
        return false;
    }
}
