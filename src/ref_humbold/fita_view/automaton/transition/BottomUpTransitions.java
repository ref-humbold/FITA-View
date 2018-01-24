package ref_humbold.fita_view.automaton.transition;

import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.automaton.Wildcard;

public class BottomUpTransitions<V>
    extends Transitions<Triple<String, String, String>, V>
{
    private final String[] sameMasks = new String[]{"*=_", "=*_", "*=*", "=**"};

    @Override
    public boolean containsEntry(Variable var, Triple<String, String, String> key)
    {
        if(hasNull(key))
            return false;

        if(map.containsKey(Pair.make(var, key)))
            return true;

        if(Objects.equals(key.getFirst(), key.getSecond()))
        {
            for(String mask : sameMasks)
            {
                Triple<String, String, String> kv = setWildcardSame(mask, key);

                if(map.containsKey(Pair.make(var, kv)))
                    return true;
            }
        }

        for(int i = 1; i < 1 << key.size(); ++i)
        {
            Triple<String, String, String> kv = setWildcardEvery(i, key);

            if(map.containsKey(Pair.make(var, kv)))
                return true;
        }

        return false;
    }

    @Override
    public void add(Variable var, Triple<String, String, String> key, V value)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        if((Objects.equals(key.getFirst(), Wildcard.SAME_VALUE) && !Objects.equals(key.getSecond(),
                                                                                   Wildcard.EVERY_VALUE))
            || (Objects.equals(key.getSecond(), Wildcard.SAME_VALUE) && !Objects.equals(
            key.getFirst(), Wildcard.EVERY_VALUE)))
            throw new IllegalTransitionException(
                "Transition cannot contain wildcard " + Wildcard.SAME_VALUE
                    + " as both left and right value.");

        super.add(var, key, value);
    }

    @Override
    public V get(Variable var, Triple<String, String, String> key)
        throws NoSuchTransitionException
    {
        if(hasNull(key))
            throw new NoSuchTransitionException("Key contains a null value");

        if(Objects.equals(key.getFirst(), key.getSecond()))
        {
            for(String mask : sameMasks)
            {
                Triple<String, String, String> kv = setWildcardSame(mask, key);
                V value = map.get(Pair.make(var, kv));

                if(value != null)
                    return value;
            }
        }

        for(int i = 0; i < 1 << key.size(); ++i)
        {
            Triple<String, String, String> kv = setWildcardEvery(i, key);
            V value = map.get(Pair.make(var, kv));

            if(value != null)
                return value;
        }

        throw new NoSuchTransitionException(
            "No entry for arguments " + key + " with variable " + var + ".");
    }

    @Override
    protected boolean hasNull(Triple<String, String, String> key)
    {
        return key.getFirst() == null || key.getSecond() == null || key.getThird() == null;
    }

    private Triple<String, String, String> setWildcardEvery(int mask,
                                                            Triple<String, String, String> k)
    {
        String[] elems = new String[]{k.getFirst(), k.getSecond(), k.getThird()};

        for(int i = 0; i < elems.length; ++i)
        {
            if((mask & (1 << i)) == 1 << i)
                elems[i] = Wildcard.EVERY_VALUE;
        }

        return Triple.make(elems[0], elems[1], elems[2]);
    }

    private Triple<String, String, String> setWildcardSame(String mask,
                                                           Triple<String, String, String> k)
    {
        String[] elems = new String[]{k.getFirst(), k.getSecond(), k.getThird()};

        for(int i = 0; i < elems.length; ++i)
        {
            if(mask.charAt(i) == '*')
                elems[i] = Wildcard.EVERY_VALUE;
            else if(mask.charAt(i) == '=')
                elems[i] = Wildcard.SAME_VALUE;
        }

        return Triple.make(elems[0], elems[1], elems[2]);
    }
}
