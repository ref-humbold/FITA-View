package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collections;
import java.util.Set;

import ref_humbold.fita_view.Pair;

public class GreatestHashCodeChoice
    implements StateChoice
{
    private static final PairHashCodeComparator COMPARATOR = new PairHashCodeComparator();

    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.GREATEST;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param states set of possible state variable values
     * @return variable value chosen as value with greatest hash code
     */
    @Override
    public Pair<String, String> chooseState(Set<Pair<String, String>> states)
    {
        return Collections.max(states, COMPARATOR);
    }
}
