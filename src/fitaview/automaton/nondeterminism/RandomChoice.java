package fitaview.automaton.nondeterminism;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import fitaview.automaton.Variable;

public class RandomChoice<K, R>
    implements StateChoice<K, R>
{
    private final Random random = new Random();

    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.RANDOM;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param key transition key in node.
     * @param states set of possible state variable values
     * @return variable value chosen randomly
     */
    @Override
    public R chooseState(Variable var, K key, Collection<R> states)
    {
        if(states.size() == 1)
            return states.iterator().next();

        int index = random.nextInt(states.size());
        Iterator<R> iterator = states.iterator();

        for(int i = 1; i < index; ++i)
            iterator.next();

        return iterator.next();
    }
}
