package fitaview.automaton;

import java.util.*;

import fitaview.automaton.transition.BottomUpTransitions;
import fitaview.automaton.transition.DuplicatedTransitionException;
import fitaview.automaton.transition.IllegalTransitionException;
import fitaview.automaton.transition.NoSuchTransitionException;
import fitaview.tree.UndefinedStateValueException;
import fitaview.utils.Pair;
import fitaview.utils.Triple;

public class BottomUpDfta
        extends BottomUpAutomaton
{
    private final BottomUpTransitions<String> transitions =
            new BottomUpTransitions<>(this::keyToString, this::valueToString);

    public BottomUpDfta(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionAsStrings()
    {
        return transitions.convertToStringMap();
    }

    @Override
    public String getTypeName()
    {
        return "Bottom-up deterministic finite tree automaton";
    }

    @Override
    public boolean checkEmptiness()
            throws UndefinedAcceptanceException, UndefinedStateValueException
    {
        Set<Map<Variable, String>> reachableStates = new HashSet<>();
        List<Map<Variable, String>> currentStates = Collections.singletonList(getInitialState());

        while(!currentStates.isEmpty())
        {
            Set<Map<Variable, String>> generatedStates = new HashSet<>();

            for(int i = 0; i < currentStates.size(); ++i)
                for(int j = i; j < currentStates.size(); ++j)
                    for(String word : alphabet)
                    {
                        Map<Variable, String> newState1 =
                                getNextState(currentStates.get(i), currentStates.get(j), word);

                        if(newState1 != null && !reachableStates.contains(newState1))
                        {
                            if(acceptanceConditions.check(newState1))
                                return false;

                            reachableStates.add(newState1);
                            generatedStates.add(newState1);
                        }

                        Map<Variable, String> newState2 =
                                getNextState(currentStates.get(j), currentStates.get(i), word);

                        if(newState2 != null && !reachableStates.contains(newState2))
                        {
                            if(acceptanceConditions.check(newState2))
                                return false;

                            reachableStates.add(newState2);
                            generatedStates.add(newState2);
                        }
                    }

            currentStates = new ArrayList<>(generatedStates);
        }

        return true;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof BottomUpDfta))
            return false;

        BottomUpDfta other = (BottomUpDfta)o;

        return Objects.equals(alphabet, other.alphabet) && Objects.equals(variables,
                                                                          other.variables)
                       && Objects.equals(acceptanceConditions, other.acceptanceConditions)
                       && Objects.equals(transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return String.format("BottomUpDFTA:\n  alphabet = %s\n  variables = %s\n  transitions = %s",
                             alphabet, variables, transitions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    public void addTransition(
            Variable var, String leftValue, String rightValue, String label, String result)
            throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Triple.make(leftValue, rightValue, label), result);
    }

    @Override
    protected String applyTransition(
            Variable var, String leftValue, String rightValue, String label)
            throws NoSuchTransitionException
    {
        String result = transitions.getMatched(var, Triple.make(leftValue, rightValue, label));

        return resolveWildcard(result, leftValue, rightValue);
    }

    private Map<Variable, String> getNextState(
            Map<Variable, String> leftState, Map<Variable, String> rightState, String word)
    {
        try
        {
            return applyTransition(leftState, rightState, word);
        }
        catch(NoSuchTransitionException e)
        {
            return null;
        }
    }
}
