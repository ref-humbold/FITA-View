package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GreatestHashCodeChoice<T>
    implements StateChoice<T>
{
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
    public T chooseState(Collection<T> states)
    {
        return Collections.max(states, Comparator.comparingInt(Object::hashCode));
    }
}
