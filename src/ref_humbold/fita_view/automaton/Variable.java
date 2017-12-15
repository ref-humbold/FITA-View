package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Variable
    implements Iterable<String>
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

    /**
     * Testing if specified string is value of the variable.
     * @param value string to test
     * @return {@code true} if this string is value, otherwise {@code false}
     */
    public boolean contains(String value)
    {
        return values.contains(value);
    }

    @Override
    public Iterator<String> iterator()
    {
        return values.iterator();
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
