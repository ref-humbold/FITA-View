package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;

public interface NonDeterministicAutomaton<K, R>
{
    StateChoice<K, R> getChoice();

    void setChoice(StateChoice<K, R> choice);

    /**
     * Converting specified transition key to its string representation.
     * @param key value to convert
     * @return string representation
     */
    String convertKeyToString(K key);

    /**
     * Converting specified transition result to its string representation.
     * @param result value to convert
     * @return string representation
     */
    String convertResultToString(R result);
}
