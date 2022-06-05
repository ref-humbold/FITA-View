package fitaview.automaton.transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import fitaview.automaton.Variable;
import fitaview.automaton.Wildcard;
import fitaview.utils.Pair;
import fitaview.utils.Triple;

public class BottomUpTransitions<V>
        extends Transitions<Triple<String, String, String>, V>
{
    private final String[] sameMasks = new String[]{"*=_", "=*_", "*=*", "=**"};

    public BottomUpTransitions(Function<Triple<String, String, String>, String> keyConversion,
                               Function<V, String> valueConversion)
    {
        super(keyConversion, valueConversion);
    }

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
                Triple<String, String, String> wildcardKey = setWildcardSame(mask, key);

                if(map.containsKey(Pair.make(var, wildcardKey)))
                    return true;
            }
        }

        for(int i = 1; i < 8; ++i)
        {
            Triple<String, String, String> wildcardKey = setWildcardEvery(i, key);

            if(map.containsKey(Pair.make(var, wildcardKey)))
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
            throw new IllegalTransitionException(String.format(
                    "Transition cannot contain wildcard %s as both left and right value",
                    Wildcard.SAME_VALUE));

        super.add(var, key, value);
    }

    @Override
    public List<V> getAll(Variable var, Triple<String, String, String> key)
            throws NoSuchTransitionException
    {
        if(hasNull(key))
            throw new NoSuchTransitionException("Key contains a null value");

        List<V> results = new ArrayList<>();

        if(Objects.equals(key.getFirst(), key.getSecond()))
        {
            for(String mask : sameMasks)
            {
                Triple<String, String, String> wildcardKey = setWildcardSame(mask, key);
                V value = map.get(Pair.make(var, wildcardKey));

                if(value != null)
                {
                    sendEntry(var, wildcardKey, value);
                    results.add(value);
                }
            }
        }

        for(int i = 0; i < 8; ++i)
        {
            Triple<String, String, String> wildcardKey = setWildcardEvery(i, key);
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
    public V getMatched(Variable var, Triple<String, String, String> key)
            throws NoSuchTransitionException
    {
        if(hasNull(key))
            throw new NoSuchTransitionException("Key contains a null value");

        if(Objects.equals(key.getFirst(), key.getSecond()))
        {
            for(String mask : sameMasks)
            {
                Triple<String, String, String> wildcardKey = setWildcardSame(mask, key);
                V value = map.get(Pair.make(var, wildcardKey));

                if(value != null)
                {
                    sendEntry(var, wildcardKey, value);
                    return value;
                }
            }
        }

        for(int i = 0; i < 8; ++i)
        {
            Triple<String, String, String> wildcardKey = setWildcardEvery(i, key);
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
