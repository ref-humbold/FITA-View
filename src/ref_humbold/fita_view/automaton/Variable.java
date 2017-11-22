package ref_humbold.fita_view.automaton;

import java.util.HashSet;
import java.util.Set;

public class Variable
{
    private String initValue;
    private Set<String> values = new HashSet<>();

    public Variable(String initValue)
    {
        this.initValue = initValue;
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
}
