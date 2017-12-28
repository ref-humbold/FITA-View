package ref_humbold.fita_view.automaton;

import java.util.*;

public class Variable
    implements Iterable<String>
{
    private int index;
    private String initValue;
    private Set<String> values = new HashSet<>();

    public Variable(int index, String init)
        throws IllegalVariableValueException
    {
        if(init == null || init.isEmpty())
            throw new IllegalVariableValueException("Initial value is null or empty.");

        this.index = index;
        this.initValue = init;
        this.values.add(this.initValue);
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
                throw new IllegalVariableValueException("Value is null or empty.");

        this.values.addAll(values);
    }

    public List<String> getValues()
    {
        return new ArrayList<>(values);
    }

    public String getInitValue()
    {
        return initValue;
    }

    public String getVarName()
    {
        return "Var_" + Integer.toString(index);
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

    /**
     * @return number of possible values
     */
    public int size()
    {
        return values.size();
    }

    @Override
    public Iterator<String> iterator()
    {
        return values.iterator();
    }

    @Override
    public int hashCode()
    {
        return initValue.hashCode() * 37 + values.hashCode();
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
    public String toString()
    {
        return getVarName() + "::" + values.toString();
    }
}
