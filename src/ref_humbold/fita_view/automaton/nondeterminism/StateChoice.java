package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Set;

import ref_humbold.fita_view.Pair;

public interface StateChoice
{
    Pair<String, String> chooseState(Set<Pair<String, String>> states);
}
