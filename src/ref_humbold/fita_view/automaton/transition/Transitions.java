package ref_humbold.fita_view.automaton.transition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.Variable;

public abstract class Transitions<K, V>
{
    protected Map<Pair<Variable, K>, V> map = new HashMap<>();

    /**
     * Testing of variable and arguments for presence in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry with given arguments
     */
    public boolean containsKey(Variable var, K key)
    {
        return map.containsKey(Pair.make(var, key));
    }

    /**
     * Testing of variable and arguments for fitting entry in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry fitting given arguments
     */
    public abstract boolean containsEntry(Variable var, K key);

    /**
     * Adding new arguments-result entry to transition function for variable.
     * @param var variable
     * @param key arguments of transition
     * @param value result of transition
     */
    public void add(Variable var, K key, V value)
        throws DuplicatedTransitionException, IllegalTransitionException
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
    public abstract V get(Variable var, K key)
        throws NoSuchTransitionException;

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
