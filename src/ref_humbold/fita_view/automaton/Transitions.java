package ref_humbold.fita_view.automaton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Quadruple;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.Tuple;

public class Transitions<K extends Tuple, V>
{
    private Map<Pair<Variable, Tuple>, V> map = new HashMap<>();

    public Transitions()
    {
    }

    /**
     * Testing of variable and arguments for presence in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry with given arguments
     */
    public boolean containsKey(Variable var, K key)
    {
        return map.containsKey(Pair.make(var, (Tuple)key));
    }

    /**
     * Testing of variable and arguments for fitting entry in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry fitting given arguments
     */
    public boolean containsEntry(Variable var, K key)
    {
        for(int i = 0; i < 1 << key.size(); ++i)
        {
            Tuple t = setWildcard(i, key);
            boolean contains = map.containsKey(Pair.make(var, t));

            if(contains)
                return true;
        }

        return false;
    }

    /**
     * Adding new arguments-result entry to transition function for variable.
     * @param var variable
     * @param key arguments of transition
     * @param value result of transition
     */
    public void add(Variable var, K key, V value)
    {
        map.put(Pair.make(var, key), value);
    }

    /**
     * Getting a result of transition function for arguments with variable.
     * @param var variable
     * @param key arguments of transition
     * @return result of transition for the arguments
     * @throws NoSuchTransitionException if there is no entry for given arguments with variable.
     */
    public V get(Variable var, K key)
        throws NoSuchTransitionException
    {
        for(int i = 0; i < 1 << key.size(); ++i)
        {
            Tuple t = setWildcard(i, key);
            V value = map.get(Pair.make(var, t));

            if(value != null)
                return value;
        }

        throw new NoSuchTransitionException(
            "No entry for arguments " + key + " with variable " + var + ".");
    }

    private Tuple setWildcard(int mask, Tuple t)
    {
        Object[] objects = t.toArray();

        for(int i = 0; i < t.size(); ++i)
        {
            if((mask & (1 << i)) == 1 << i)
                objects[i] = Wildcard.EVERY_VALUE;
        }

        switch(t.size())
        {
            case 2:
                return Pair.make(objects[0], objects[1]);

            case 3:
                return Triple.make(objects[0], objects[1], objects[2]);

            case 4:
                return Quadruple.make(objects[0], objects[1], objects[2], objects[3]);

            default:
                return null;
        }
    }

    @Override
    public String toString()
    {
        return "Transitions::" + map.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || !(obj instanceof Transitions))
            return false;

        Transitions<?, ?> other = (Transitions<?, ?>)obj;

        return Objects.equals(this.map, other.map);
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }
}
