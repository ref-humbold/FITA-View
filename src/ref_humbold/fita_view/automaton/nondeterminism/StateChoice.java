package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;

public interface StateChoice<K, R>
{
    /**
     * @return type of non-deterministic choice
     */
    StateChoiceMode getMode();

    /**
     * Non-deterministically choosing variable values.
     * @param key transition key node
     * @param states possible state variable values
     * @return variable value chosen
     */
    R chooseState(K key, Collection<R> states);
}
