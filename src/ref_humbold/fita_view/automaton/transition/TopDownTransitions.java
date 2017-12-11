package ref_humbold.fita_view.automaton.transition;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.automaton.Wildcard;

public class TopDownTransitions<V>
    extends Transitions<Pair<String, String>, V>
{
    @Override
    public boolean containsEntry(Variable var, Pair<String, String> key)
    {
        for(int i = 0; i < 1 << key.size(); ++i)
        {
            Pair<String, String> kv = setWildcard(i, key);

            if(map.containsKey(Pair.make(var, kv)))
                return true;
        }

        return false;
    }

    @Override
    public V get(Variable var, Pair<String, String> key)
        throws NoSuchTransitionException
    {
        for(int i = 0; i < 1 << key.size(); ++i)
        {
            Pair<String, String> kv = setWildcard(i, key);
            V value = map.get(Pair.make(var, kv));

            if(value != null)
                return value;
        }

        throw new NoSuchTransitionException(
            "No entry for arguments " + key + " with variable " + var + ".");
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
