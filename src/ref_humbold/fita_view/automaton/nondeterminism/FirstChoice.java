package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Set;

import ref_humbold.fita_view.Pair;

public class FirstChoice
    implements StateChoice
{
    public FirstChoice()
    {
    }

    @Override
    public Pair<String, String> chooseState(Set<Pair<String, String>> states)
    {
        return states.iterator().next();
    }
}
