package ref_humbold.fita_view.automaton.transition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.Variable;

public abstract class Transitions<K, V>
{
    Map<Pair<Variable, K>, V> map = new HashMap<>();

    /**
     * Testing if specified variable and arguments are present in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is transition entry with the exact arguments
     */
    public boolean containsKey(Variable var, K key)
    {
        return map.containsKey(Pair.make(var, key));
    }

    /**
     * Testing if specified variable and arguments fit for any entry in transition function.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry fitting the arguments
     */
    public abstract boolean containsEntry(Variable var, K key);

    /**
     * Adding new arguments-result entry to transition function for specified variable.
     * @param var variable
     * @param key arguments of transition
     * @param value result of transition
     */
    public void add(Variable var, K key, V value)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        if(containsKey(var, key))
            throw new DuplicatedTransitionException(
                "Duplicated transition entry for " + var + " + " + key + ".");

        map.put(Pair.make(var, key), value);
    }

    /**
     * Getting a result of transition function for specified arguments and variable.
     * @param var variable
     * @param key arguments of transition
     * @return result of transition for the arguments
     * @throws NoSuchTransitionException if there is no entry for specified arguments with variable.
     */
    public abstract V get(Variable var, K key)
        throws NoSuchTransitionException;

    /**
     * /**
     * Converting each key and value in transition function to strings according to specified functions.
     * @param keyFunc conversion of keys
     * @param valueFunc conversion of values
     * @return transition function with string representations
     */
    public Map<Pair<Variable, String>, String> convertToStringMap(Function<K, String> keyFunc,
                                                                  Function<V, String> valueFunc)
    {
        Map<Pair<Variable, String>, String> stringMap = new HashMap<>();

        map.forEach(
            (key, value) -> stringMap.put(Pair.make(key.getFirst(), keyFunc.apply(key.getSecond())),
                                          valueFunc.apply(value)));

        return stringMap;
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
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
    public String toString()
    {
        return "Transitions::" + map.toString();
    }

    protected abstract boolean hasNull(K key);
}
