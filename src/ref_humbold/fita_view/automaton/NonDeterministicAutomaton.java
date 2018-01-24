package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;

public interface NonDeterministicAutomaton<T>
{
    StateChoice<T> getChoice();

    void setChoice(StateChoice<T> choice);

    String convert(T value);
}
