package ref_humbold.fita_view.automaton;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Variable
{
    private String initValue;
    private Set<String> values = new HashSet<>();

    public Variable(String init)
    {
        this.initValue = init;
        this.values.add(this.initValue);
    }

    public Variable(String init, String... values)
    {
        this(init);
        Collections.addAll(this.values, values);
    }

    public String getInitValue()
    {
        return initValue;
    }

    /**
     * Checking if given string is value of the variable.
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
     * @throws IncorrectValueException if string is null or empty
     */
    void addValue(String value)
        throws IncorrectValueException
    {
        if(value == null || value.isEmpty())
            throw new IncorrectValueException("Value is null or empty.");

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
