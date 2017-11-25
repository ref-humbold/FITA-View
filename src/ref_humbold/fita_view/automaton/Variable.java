package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Variable
{
    private String initValue;
    private Set<String> values;

    public Variable(String init)
    {
        this(init, Collections.singletonList(init));
    }

    public Variable(String init, Collection<String> values)
    {
        this.initValue = init;
        this.values = new HashSet<>(values);
        this.values.add(this.initValue);
    }

    public String getInitValue()
    {
        return initValue;
    }

    public boolean isValue(String value)
    {
        return values.contains(value);
    }

    void addValue(String value)
    {
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
