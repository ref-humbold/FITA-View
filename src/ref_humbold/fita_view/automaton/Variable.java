package ref_humbold.fita_view.automaton;

import java.util.Collections;
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
        throws IllegalValueException
    {
        if(init == null || init.isEmpty())
            throw new IllegalValueException("Initial value is null or empty.");

        this.initValue = init;
        this.values.add(this.initValue);
    }

    public Variable(String init, String... values)
        throws IllegalValueException
    {
        this(init);

        for(String v : values)
            if(v == null || v.isEmpty())
                throw new IllegalValueException("Value is null or empty.");

        Collections.addAll(this.values, values);
    }

    public String getInitValue()
    {
        return initValue;
    }

    @Override
    public Iterator<String> iterator()
    {
        return values.iterator();
    }

    /**
     * Checking if given string is value of this variable.
     * @param value string to check
     * @return true if string is in values set, otherwise false
     */
    public boolean isValue(String value)
    {
        return values.contains(value);
    }

    /**
     * Adding given string to variable values set
     * @param value string to add
     * @throws IllegalValueException if string is null or empty
     */
    void addValue(String value)
        throws IllegalValueException
    {
        if(value == null || value.isEmpty())
            throw new IllegalValueException("Value is null or empty.");

        values.add(value);
    }

    @Override
    public String toString()
    {
        return "Variable of " + values.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof Variable))
            return false;

        Variable other = (Variable)o;

        return Objects.equals(this.initValue, other.initValue) && Objects.equals(this.values,
                                                                                 other.values);
    }

    @Override
    public int hashCode()
    {
        return initValue.hashCode() * 37 + values.hashCode();
    }
}
