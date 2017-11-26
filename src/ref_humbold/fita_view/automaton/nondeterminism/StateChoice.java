package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Set;

import ref_humbold.fita_view.Pair;

public interface StateChoice
{
    /**
     * Non-deterministically choosing variable values.
     * @param states possible state variable values
     * @return variable value chosen
     */
    Pair<String, String> chooseState(Set<Pair<String, String>> states);
}
