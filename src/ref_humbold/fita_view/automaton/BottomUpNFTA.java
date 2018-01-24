package ref_humbold.fita_view.automaton;

import java.util.*;
import java.util.stream.Collectors;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.TreeNode;

public class BottomUpNFTA
    extends BottomUpAutomaton
    implements NonDeterministicAutomaton<String>
{
    private BottomUpTransitions<Set<String>> transitions = new BottomUpTransitions<>();
    private StateChoice<String> choice;

    public BottomUpNFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public StateChoice<String> getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice<String> choice)
    {
        this.choice = choice;
    }

    @Override
    public String getTypeName()
    {
        return "Bottom-up non-deterministic finite tree automaton";
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionWithStrings()
    {
        return this.transitions.convertToStringMap(this::keyToString, this::valueSetToString);
    }

    @Override
    public String convert(String value)
    {
        return valueToString(value);
    }

    @Override
    public void addTransition(Variable var, String leftValue, String rightValue, String label,
                              String result)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        Set<String> entry = getTransitionEntry(var, Triple.make(leftValue, rightValue, label));

        entry.add(result);
    }

    @Override
    public TreeNode generateTree()
    {
        return null;
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
    protected String getTransitionResult(Variable var, String leftValue, String rightValue,
                                         String label)
        throws NoSuchTransitionException
    {
        return choice.chooseState(transitions.get(var, Triple.make(leftValue, rightValue, label)));
    }

    private String valueSetToString(Set<String> value)
    {
        Set<String> stringSet = value.stream().map(this::valueToString).collect(Collectors.toSet());

        return stringSet.toString();
    }

    private Set<String> getTransitionEntry(Variable var, Triple<String, String, String> key)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        try
        {
            return transitions.get(var, key);
        }
        catch(NoSuchTransitionException e)
        {
            transitions.add(var, key, new HashSet<>());
            return getTransitionEntry(var, key);
        }
    }
}
