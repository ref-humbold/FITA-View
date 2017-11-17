package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Variable
{
    private String initValue;
    private Set<String> values = new HashSet<>();

    public Variable(String initValue, String... values)
    {
        this.initValue = initValue;
        this.values.add(this.initValue);
        this.values.addAll(Arrays.asList(values));
    }

    public String getInitValue()
    {
        return initValue;
    }

    public boolean isCorrectValue(String value)
    {
        return values.contains(value);
    }
}
