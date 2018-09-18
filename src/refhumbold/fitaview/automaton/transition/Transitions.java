package refhumbold.fitaview.automaton.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.Variable;

public abstract class Transitions<K, V>
{
    protected Map<Pair<Variable, K>, V> map = new HashMap<>();
    private Function<K, String> keyConversion;
    private Function<V, String> valueConversion;

    public Transitions(Function<K, String> keyConversion, Function<V, String> valueConversion)
    {
        if(keyConversion == null || valueConversion == null)
            throw new IllegalArgumentException("Conversion function is null");

        this.keyConversion = keyConversion;
        this.valueConversion = valueConversion;
    }

    /**
     * Testing if specified variable and arguments are present in transition relation.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is transition entry with the exact arguments
     */
    public boolean containsKey(Variable var, K key)
    {
        return map.containsKey(Pair.make(var, key));
    }

    /**
     * Testing if specified variable and arguments fit for any entry in transition relation.
     * @param var variable
     * @param key arguments of transition
     * @return {@code true} if there is any transition entry fitting the arguments
     */
    public abstract boolean containsEntry(Variable var, K key);

    /**
     * Adding new arguments-result entry to transition relation for specified variable.
     * @param var variable
     * @param key arguments of transition
     * @param value result of transition
     */
    public void add(Variable var, K key, V value)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        if(containsKey(var, key))
            throw new DuplicatedTransitionException(
                String.format("Duplicated transition entry for %s + %s", var.toString(),
                              key.toString()));

        map.put(Pair.make(var, key), value);
    }

    /**
     * Getting all results of transition relation for specified arguments and variable.
     * @param var variable
     * @param key arguments of transition
     * @return list of possible results of transition for the arguments
     * @throws NoSuchTransitionException if there is no entry for specified arguments with variable.
     */
    public abstract List<V> getAll(Variable var, K key)
        throws NoSuchTransitionException;

    /**
     * Getting first matched result of transition relation for specified arguments and variable.
     * @param var variable
     * @param key arguments of transition
     * @return matched results of transition for the arguments
     * @throws NoSuchTransitionException if there is no entry for specified arguments with variable.
     */
    public abstract V getMatched(Variable var, K key)
        throws NoSuchTransitionException;

    /**
     * /**
     * Converting each key and value in transition relation to strings according to conversion functions.
     * @return transition relation with string representations
     */
    public Map<Pair<Variable, String>, String> convertToStringMap()
    {
        Map<Pair<Variable, String>, String> stringMap = new HashMap<>();

        map.forEach((key, value) -> stringMap.put(
            Pair.make(key.getFirst(), keyConversion.apply(key.getSecond())),
            valueConversion.apply(value)));

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

        if(!(obj instanceof Transitions))
            return false;

        Transitions<?, ?> other = (Transitions<?, ?>)obj;

        return Objects.equals(this.map, other.map);
    }

    @Override
    public String toString()
    {
        return String.format("Transitions::%s", map.toString());
    }

    /**
     * Checking if arguments of transition contain null value.
     * @param key arguments of transition
     * @return {@code true} if arguments of transition have null, otherwise {@code false}
     */
    protected abstract boolean hasNull(K key);

    /**
     * Sending transition entry.
     * @param var variable
     * @param key arguments of transition
     * @param value result of transition
     */
    protected void sendEntry(Variable var, K key, V value)
    {
        TransitionsSender.getInstance()
                         .send(Triple.make(var, keyConversion.apply(key),
                                           valueConversion.apply(value)));
    }
}
