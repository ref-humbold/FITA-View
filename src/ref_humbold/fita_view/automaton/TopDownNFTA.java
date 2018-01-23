package ref_humbold.fita_view.automaton;

import java.util.*;
import java.util.stream.Collectors;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;
import ref_humbold.fita_view.tree.TreeNode;

public class TopDownNFTA
    extends TopDownAutomaton
    implements NonDeterministicAutomaton
{
    protected TopDownTransitions<Set<Pair<String, String>>> transitions = new TopDownTransitions<>();
    private StateChoice choice;

    public TopDownNFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public StateChoice getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice choice)
    {
        this.choice = choice;
    }

    @Override
    public String getTypeName()
    {
        return "Top-down non-deterministic finite tree automaton";
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionWithStrings()
    {
        return this.transitions.convertToStringMap(this::keyToString, this::valueToString);
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

        if(o == null || !(o instanceof TopDownNFTA))
            return false;

        TopDownNFTA other = (TopDownNFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownNFTA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public void addTransition(Variable var, String value, String label, String leftResult,
                              String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        Set<Pair<String, String>> entry = getTransitionEntry(var, Pair.make(value, label));

        entry.add(Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> getTransitionResult(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        return choice.chooseState(transitions.get(var, Pair.make(value, label)));
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is infinite.");
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

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }

    String valueToString(Set<Pair<String, String>> value)
    {
        Set<String> stringSet = value.stream()
                                     .map(pair -> "( LEFT VALUE = \'" + pair.getFirst()
                                         + "\', RIGHT VALUE = " + pair.getSecond() + "\' )")
                                     .collect(Collectors.toSet());

        return stringSet.toString();
    }

    private Set<Pair<String, String>> getTransitionEntry(Variable var, Pair<String, String> key)
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
