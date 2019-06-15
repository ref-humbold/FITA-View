package fitaview.automaton.nondeterminism;

import java.util.Collection;

import fitaview.automaton.Variable;

public interface StateChoice<K, R>
{
    /**
     * @return type of non-deterministic choice
     */
    StateChoiceMode getMode();

    /**
     * Non-deterministically choosing variable values.
     * @param var variable
     * @param key transition key node
     * @param states possible state variable values
     * @return variable value chosen
     */
    R chooseState(Variable var, K key, Collection<R> states);
}
