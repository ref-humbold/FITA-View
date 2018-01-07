package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;

import ref_humbold.fita_view.Pair;

public interface StateChoice
{
    /**
     * @return type of non-deterministic choice
     */
    StateChoiceMode getMode();

    /**
     * Non-deterministically choosing variable values.
     * @param states possible state variable values
     * @return variable value chosen
     */
    Pair<String, String> chooseState(Collection<Pair<String, String>> states);
}
