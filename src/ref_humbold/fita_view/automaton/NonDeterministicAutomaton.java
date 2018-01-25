package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;

public interface NonDeterministicAutomaton<T>
{
    StateChoice<T> getChoice();

    void setChoice(StateChoice<T> choice);

    /**
     * Converting specified value to its string representation.
     * @param value value to convert
     * @return string representation
     */
    String convertToString(T value);
}
