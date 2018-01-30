package ref_humbold.fita_view.automaton;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.UndefinedStateValueException;

public class BottomUpNFTA
    extends BottomUpAutomaton
    implements NonDeterministicAutomaton<Triple<String, String, String>, String>
{
    private StateChoice<Triple<String, String, String>, String> choice;
    private BottomUpTransitions<Set<String>> transitions = new BottomUpTransitions<>(
        this::keyToString, this::valueSetToString);

    public BottomUpNFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public StateChoice<Triple<String, String, String>, String> getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice<Triple<String, String, String>, String> choice)
    {
        this.choice = choice;
    }

    @Override
    public String getTypeName()
    {
        return "Bottom-up non-deterministic finite tree automaton";
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionAsStrings()
    {
        return this.transitions.convertToStringMap();
    }

    @Override
    public String convertKeyToString(Triple<String, String, String> key)
    {
        return keyToString(key);
    }

    @Override
    public String convertResultToString(String result)
    {
        return valueToString(result);
    }

    @Override
    public void addTransition(Variable var, String leftValue, String rightValue, String label,
                              String result)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        Set<String> entry = getTransitionValuesSet(var, Triple.make(leftValue, rightValue, label));

        entry.add(result);
    }

    @Override
    public boolean checkEmptiness()
        throws UndefinedStateValueException, UndefinedAcceptanceException
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
                        Set<Map<Variable, String>> newStates1 = getNextStates(currentStates.get(i),
                                                                              currentStates.get(j),
                                                                              word);

                        if(newStates1 != null)
                        {
                            for(Map<Variable, String> state : newStates1)
                                if(reachableStates.contains(state))
                                {
                                    if(acceptanceConditions.check(state))
                                        return false;

                                    reachableStates.add(state);
                                    generatedStates.add(state);
                                }
                        }

                        Set<Map<Variable, String>> newStates2 = getNextStates(currentStates.get(j),
                                                                              currentStates.get(i),
                                                                              word);

                        if(newStates2 != null)
                        {
                            for(Map<Variable, String> state : newStates2)
                                if(reachableStates.contains(state))
                                {
                                    if(acceptanceConditions.check(state))
                                        return false;

                                    reachableStates.add(state);
                                    generatedStates.add(state);
                                }
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

        if(o == null || !(o instanceof BottomUpNFTA))
            return false;

        BottomUpNFTA other = (BottomUpNFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "BottomUpNFTA:\n  alphabet = " + alphabet.toString() + "\n  variables = " + variables
            .toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    protected String applyTransition(Variable var, String leftValue, String rightValue,
                                     String label)
        throws NoSuchTransitionException
    {
        return choice.chooseState(Triple.make(leftValue, rightValue, label),
                                  getAllTransitionResults(var, leftValue, rightValue, label));
    }

    Set<Map<Variable, String>> getNextStates(Map<Variable, String> leftState,
                                             Map<Variable, String> rightState, String word)
    {
        Map<Variable, Set<String>> result = new HashMap<>();

        try
        {
            for(Variable var : variables)
                result.put(var,
                           getAllTransitionResults(var, leftState.get(var), rightState.get(var),
                                                   word));
        }
        catch(NoSuchTransitionException e)
        {
            return null;
        }

        return pairsToMap(convert(mapToPairs(result)));
    }

    private Set<String> getAllTransitionResults(Variable var, String leftValue, String rightValue,
                                                String label)
        throws NoSuchTransitionException
    {
        return transitions.getAll(var, Triple.make(leftValue, rightValue, label))
                          .stream()
                          .flatMap(set -> set.stream()
                                             .map(res -> resolveWildcard(res, leftValue,
                                                                         rightValue)))
                          .collect(Collectors.toSet());
    }

    private String valueSetToString(Set<String> value)
    {
        Set<String> stringSet = value.stream().map(this::valueToString).collect(Collectors.toSet());

        return stringSet.toString();
    }

    private Set<String> getTransitionValuesSet(Variable var, Triple<String, String, String> key)
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

    private Set<Map<Variable, String>> pairsToMap(Set<List<Pair<Variable, String>>> listSet)
    {
        return listSet.stream()
                      .map(pairList -> pairList.stream()
                                               .collect(
                                                   Collectors.toMap(Pair::getFirst, Pair::getSecond,
                                                                    (a, b) -> b)))
                      .collect(Collectors.toSet());
    }

    private Set<List<Pair<Variable, String>>> convert(Queue<Pair<Variable, Set<String>>> pairQueue)
    {
        if(pairQueue.isEmpty())
        {
            Set<List<Pair<Variable, String>>> empty = new HashSet<>();

            empty.add(new ArrayList<>());

            return empty;
        }

        Pair<Variable, Set<String>> pair = pairQueue.remove();
        Set<List<Pair<Variable, String>>> result = new HashSet<>();
        Set<List<Pair<Variable, String>>> recursive = convert(pairQueue);

        pair.getSecond()
            .forEach(value -> recursive.stream()
                                       .map(list -> Stream.concat(list.stream(), Stream.of(
                                           Pair.make(pair.getFirst(), value)))
                                                          .collect(Collectors.toList()))
                                       .forEach(result::add));

        return result;
    }

    private Queue<Pair<Variable, Set<String>>> mapToPairs(Map<Variable, Set<String>> map)
    {
        Queue<Pair<Variable, Set<String>>> pairList = new ArrayDeque<>();

        map.forEach((key, value) -> pairList.add(Pair.make(key, value)));

        return pairList;
    }
}
