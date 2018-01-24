package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;

public class LeastHashCodeChoice<T>
    implements StateChoice<T>
{
    private final HashCodeComparator<T> comparator = new HashCodeComparator<>();

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
    public T chooseState(Collection<T> states)
    {
        return Collections.min(states, comparator);
    }
}
