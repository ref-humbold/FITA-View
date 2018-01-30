package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GreatestHashCodeChoice<K, R>
    implements StateChoice<K, R>
{
    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.GREATEST;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param key transition key in node.
     * @param states set of possible state variable values
     * @return variable value chosen as value with greatest hash code
     */
    @Override
    public R chooseState(K key, Collection<R> states)
    {
        return Collections.max(states, Comparator.comparingInt(Object::hashCode));
    }
}
