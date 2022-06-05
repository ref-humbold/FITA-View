package fitaview.automaton;

import java.util.*;

public class Variable
        implements Iterable<String>
{
    private final int index;
    private final String initValue;
    private final Set<String> values = new HashSet<>();

    public Variable(int index, String init)
            throws IllegalVariableValueException
    {
        if(init == null || init.isEmpty())
            throw new IllegalVariableValueException("Initial value is null or empty");

        this.index = index;
        initValue = init;
        values.add(initValue);
    }

    public Variable(int index, String init, String... values)
            throws IllegalVariableValueException
    {
        this(index, init, Arrays.asList(values));
    }

    public Variable(int index, String init, Collection<String> values)
            throws IllegalVariableValueException
    {
        this(index, init);

        for(String v : values)
            if(v == null || v.isEmpty())
                throw new IllegalVariableValueException("Value is null or empty");

        this.values.addAll(values);
    }

    public String getInitValue()
    {
        return initValue;
    }

    public List<String> getValuesList()
    {
        return new ArrayList<>(values);
    }

    /**
     * @return name of the variable
     */
    public String getVarName()
    {
        return String.format("Var_%d", index);
    }

    /**
     * @return number of possible values
     */
    public int size()
    {
        return values.size();
    }

    /**
     * Checking if specified string is value of the variable.
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
    public int hashCode()
    {
        return Objects.hash(initValue, values);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(!(obj instanceof Variable))
            return false;

        Variable other = (Variable)obj;

        return Objects.equals(initValue, other.initValue) && Objects.equals(values, other.values);
    }

    @Override
    public String toString()
    {
        return String.format("%s::%s", getVarName(), values);
    }
}
