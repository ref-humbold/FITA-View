package fitaview.automaton.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import fitaview.automaton.Variable;
import fitaview.automaton.Wildcard;
import fitaview.utils.Pair;

public class TopDownTransitions<V>
        extends Transitions<Pair<String, String>, V>
{
    public TopDownTransitions(Function<Pair<String, String>, String> keyConversion,
                              Function<V, String> valueConversion)
    {
        super(keyConversion, valueConversion);
    }

    @Override
    public boolean containsEntry(Variable var, Pair<String, String> key)
    {
        if(hasNull(key))
            return false;

        for(int i = 0; i < 4; ++i)
        {
            Pair<String, String> wildcardKey = setWildcard(i, key);

            if(map.containsKey(Pair.make(var, wildcardKey)))
                return true;
        }

        return false;
    }

    @Override
    public List<V> getAll(Variable var, Pair<String, String> key)
            throws NoSuchTransitionException
    {
        if(hasNull(key))
            throw new NoSuchTransitionException("Key contains a null value");

        List<V> results = new ArrayList<>();

        for(int i = 0; i < 4; ++i)
        {
            Pair<String, String> wildcardKey = setWildcard(i, key);
            V value = map.get(Pair.make(var, wildcardKey));

            if(value != null)
            {
                sendEntry(var, wildcardKey, value);
                results.add(value);
            }
        }

        if(results.isEmpty())
            throw new NoSuchTransitionException(
                    String.format("No entry for arguments %s with variable %s", key, var));

        return results;
    }

    @Override
    public V getMatched(Variable var, Pair<String, String> key)
            throws NoSuchTransitionException
    {
        if(hasNull(key))
            throw new NoSuchTransitionException("Key contains a null value");

        for(int i = 0; i < 4; ++i)
        {
            Pair<String, String> wildcardKey = setWildcard(i, key);
            V value = map.get(Pair.make(var, wildcardKey));

            if(value != null)
            {
                sendEntry(var, wildcardKey, value);
                return value;
            }
        }

        throw new NoSuchTransitionException(
                String.format("No entry for arguments %s with variable %s", key, var));
    }

    @Override
    protected boolean hasNull(Pair<String, String> key)
    {
        return key.getFirst() == null || key.getSecond() == null;
    }

    private Pair<String, String> setWildcard(int mask, Pair<String, String> k)
    {
        String[] elems = new String[]{k.getFirst(), k.getSecond()};

        for(int i = 0; i < elems.length; ++i)
        {
            if((mask & (1 << i)) == 1 << i)
                elems[i] = Wildcard.EVERY_VALUE;
        }

        return Pair.make(elems[0], elems[1]);
    }
}
