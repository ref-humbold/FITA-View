package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Set;

import ref_humbold.fita_view.Pair;

public class FirstElementChoice
    implements StateChoice
{
    /**
     * Non-deterministically choosing variable values.
     * @param states set of possible state variable values
     * @return variable value chosen as first value return by set iterator
     */
    @Override
    public Pair<String, String> chooseState(Set<Pair<String, String>> states)
    {
        return states.iterator().next();
    }
}