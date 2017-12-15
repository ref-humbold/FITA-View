package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Set;

import ref_humbold.fita_view.Pair;

public class FirstChoice
    implements StateChoice
{
    public FirstChoice()
    {
    }

    /**
     * Non-deterministically choosing variable values.
     * @param states possible state variable values
     * @return variable value chosen as first value specified from set
     */
    @Override
    public Pair<String, String> chooseState(Set<Pair<String, String>> states)
    {
        return states.iterator().next();
    }
}
