package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import ref_humbold.fita_view.Pair;

public class RandomChoice
    implements StateChoice
{
    private static final Random random = new Random();

    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.RANDOM;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param states set of possible state variable values
     * @return variable value chosen randomly
     */
    @Override
    public Pair<String, String> chooseState(Collection<Pair<String, String>> states)
    {
        if(states.size() == 1)
            return states.iterator().next();

        int index = random.nextInt(states.size());
        Iterator<Pair<String, String>> iterator = states.iterator();

        for(int i = 1; i < index; ++i)
            iterator.next();

        return iterator.next();
    }
}
