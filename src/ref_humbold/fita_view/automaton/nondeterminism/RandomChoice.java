package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import ref_humbold.fita_view.Pair;

public class RandomChoice
    implements StateChoice
{
    private static final Random random = new Random();

    public RandomChoice()
    {
    }

    @Override
    public Pair<String, String> chooseState(Set<Pair<String, String>> states)
    {
        int index = random.nextInt(states.size());
        Iterator<Pair<String, String>> iterator = states.iterator();

        for(int i = 1; i < index; ++i)
            iterator.next();

        return iterator.next();
    }
}
