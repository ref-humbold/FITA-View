package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;

import ref_humbold.fita_view.Pair;

public class LeastHashCodeChoice
    implements StateChoice
{
    private static final PairHashCodeComparator COMPARATOR = new PairHashCodeComparator();

    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.LEAST;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param states set of possible state variable values
     * @return variable value chosen as value with least hash code
     */
    @Override
    public Pair<String, String> chooseState(Collection<Pair<String, String>> states)
    {
        return Collections.min(states, COMPARATOR);
    }
}
