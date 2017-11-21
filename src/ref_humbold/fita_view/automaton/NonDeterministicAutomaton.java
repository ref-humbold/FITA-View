package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;

public interface NonDeterministicAutomaton
{
    StateChoice getChoice();

    void setChoice(StateChoice choice);
}
