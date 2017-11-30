package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Variable
    implements Collection<String>
{
    private String initValue;
    private Set<String> values = new HashSet<>();

    public Variable(String init)
        throws IllegalVariableValueException
    {
        if(init == null || init.isEmpty())
            throw new IllegalVariableValueException("Initial value is null or empty.");

        this.initValue = init;
        this.values.add(this.initValue);
    }

    public Variable(String init, Collection<String> values)
        throws IllegalVariableValueException
    {
        this(init);

        for(String v : values)
            if(v == null || v.isEmpty())
                throw new IllegalVariableValueException("Value is null or empty.");

        this.values.addAll(values);
    }

    public Variable(String init, String... values)
        throws IllegalVariableValueException
    {
        this(init, Arrays.asList(values));
    }

    public String getInitValue()
    {
        return initValue;
    }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public boolean isEmpty()
    {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object value)
    {
        return values.contains(value);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return values.containsAll(collection);
    }

    @Override
    public Iterator<String> iterator()
    {
        return values.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts)
    {
        return values.toArray(ts);
    }

    @Override
    public boolean add(String value)
    {
        throw new UnsupportedOperationException("Cannot add value to the variable.");
    }

    @Override
    public boolean addAll(Collection<? extends String> collection)
    {
        throw new UnsupportedOperationException("Cannot add value to the variable.");
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot remove values from variable.");
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot remove values from variable.");
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot retain values from variable.");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Cannot remove values from variable.");
    }

    @Override
    public String toString()
    {
        return "Variable::" + values.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || !(obj instanceof Variable))
            return false;

        Variable other = (Variable)obj;

        return Objects.equals(this.initValue, other.initValue) && Objects.equals(this.values,
                                                                                 other.values);
    }

    @Override
    public int hashCode()
    {
        return initValue.hashCode() * 37 + values.hashCode();
    }
}
