package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;

public abstract class TopDownNondeterministicAutomaton
    extends TopDownAutomaton
    implements NonDeterministicAutomaton<Pair<String, String>, Pair<String, String>>
{
    protected TopDownTransitions<Set<Pair<String, String>>> transitions = new TopDownTransitions<>(
        this::keyToString, this::valueSetToString);
    private StateChoice<Pair<String, String>, Pair<String, String>> choice;

    public TopDownNondeterministicAutomaton(Collection<Variable> variables,
                                            Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public StateChoice<Pair<String, String>, Pair<String, String>> getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice<Pair<String, String>, Pair<String, String>> choice)
    {
        this.choice = choice;
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionAsStrings()
    {
        return this.transitions.convertToStringMap();
    }

    @Override
    public String convertKeyToString(Pair<String, String> key)
    {
        return keyToString(key);
    }

    @Override
    public String convertResultToString(Pair<String, String> result)
    {
        return valueToString(result);
    }

    @Override
    public void addTransition(Variable var, String value, String label, String leftResult,
                              String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        Set<Pair<String, String>> entry = getTransitionValuesSet(var, Pair.make(value, label));

        entry.add(Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> applyTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        return choice.chooseState(var, Pair.make(value, label),
                                  getAllTransitionResults(var, value, label));
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        if(choice == null)
            throw new NoNonDeterministicStrategyException(
                "Automaton has no non-deterministic strategy.");

        super.initialize();
    }

    private Set<Pair<String, String>> getAllTransitionResults(Variable var, String value,
                                                              String label)
        throws NoSuchTransitionException
    {
        return transitions.getAll(var, Pair.make(value, label))
                          .stream()
                          .flatMap(set -> set.stream().map(res -> resolveWildcard(res, value)))
                          .collect(Collectors.toSet());
    }

    private String valueSetToString(Set<Pair<String, String>> value)
    {
        Set<String> stringSet = value.stream().map(this::valueToString).collect(Collectors.toSet());

        return stringSet.toString();
    }

    private Set<Pair<String, String>> getTransitionValuesSet(Variable var, Pair<String, String> key)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        try
        {
            return transitions.getMatched(var, key);
        }
        catch(NoSuchTransitionException e)
        {
            transitions.add(var, key, new HashSet<>());
            return getTransitionValuesSet(var, key);
        }
    }
}
