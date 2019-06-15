package fitaview.automaton;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import fitaview.Pair;
import fitaview.tree.UndefinedStateValueException;

public class AcceptanceConditions
{
    private Set<Map<Variable, Pair<String, Boolean>>> statesConditions = new HashSet<>();

    /**
     * Describing single acceptance entry with condition on value for variable.
     * @param entry equality on value for variable
     * @return description of the entry
     */
    public static String getEntryString(Map.Entry<Variable, Pair<String, Boolean>> entry)
    {
        String conditionString;

        if(entry.getValue().getSecond())
        {
            conditionString = Objects.equals(entry.getValue().getFirst(), Wildcard.EVERY_VALUE)
                              ? "any value possible"
                              : "equal to \'" + entry.getValue().getFirst() + "\'";
        }
        else
            conditionString = "other than \'" + entry.getValue().getFirst() + "\'";

        return entry.getKey().getVarName() + " :: " + conditionString;
    }

    public Set<Map<Variable, Pair<String, Boolean>>> getStatesConditions()
    {
        return this.statesConditions;
    }

    /**
     * @return number of acceptance conditions
     */
    public int size()
    {
        return statesConditions.size();
    }

    /**
     * Adding a new acceptance condition.
     * @param mapping mapping from variables to acceptance conditions on their values
     */
    public void add(Map<Variable, Pair<String, Boolean>> mapping)
    {
        statesConditions.add(mapping);
    }

    /**
     * Testing if specified state can be accepted by the automaton.
     * @param state state from a tree node
     * @return {@code true} if state is accepted, otherwise {@code false}
     * @throws UndefinedStateValueException if state contains a variable with undefined value
     * @throws UndefinedAcceptanceException if set of acceptance conditions is empty
     */
    public boolean check(Map<Variable, String> state)
        throws UndefinedStateValueException, UndefinedAcceptanceException
    {
        if(statesConditions.isEmpty())
            throw new UndefinedAcceptanceException(
                "Automaton has no acceptance conditions defined");

        for(Map<Variable, Pair<String, Boolean>> condition : statesConditions)
        {
            boolean canAccept = true;

            for(Variable var : condition.keySet())
            {
                if(state.get(var) == null)
                    throw new UndefinedStateValueException(
                        "State contains a variable with undefined value");

                Pair<String, Boolean> valueEquality = condition.get(var);

                if(valueEquality.getSecond())
                    canAccept &= Objects.equals(valueEquality.getFirst(), state.get(var)) || Objects
                        .equals(valueEquality.getFirst(), Wildcard.EVERY_VALUE);
                else
                    canAccept &= !Objects.equals(valueEquality.getFirst(), state.get(var));
            }

            if(canAccept)
                return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof AcceptanceConditions))
            return false;

        AcceptanceConditions other = (AcceptanceConditions)o;

        return Objects.equals(this.statesConditions, other.statesConditions);
    }

    @Override
    public int hashCode()
    {
        return statesConditions.hashCode();
    }
}
