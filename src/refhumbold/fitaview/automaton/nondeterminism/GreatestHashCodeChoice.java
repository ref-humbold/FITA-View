package refhumbold.fitaview.automaton.nondeterminism;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import refhumbold.fitaview.automaton.Variable;

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
    public R chooseState(Variable var, K key, Collection<R> states)
    {
        return Collections.max(states, Comparator.comparingInt(Object::hashCode));
    }
}
