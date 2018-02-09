package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import ref_humbold.fita_view.automaton.Variable;

public class LeastHashCodeChoice<K, R>
    implements StateChoice<K, R>
{
    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.LEAST;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param key transition key in node.
     * @param states set of possible state variable values
     * @return variable value chosen as value with least hash code
     */
    @Override
    public R chooseState(Variable var, K key, Collection<R> states)
    {
        return Collections.min(states, Comparator.comparingInt(Object::hashCode));
    }
}
