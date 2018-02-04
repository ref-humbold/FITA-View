package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedStateValueException;

public class TopDownDITA
    extends TopDownDFTA
    implements InfiniteTreeAutomaton
{
    private AcceptanceConditions infiniteAcceptanceConditions = new AcceptanceConditions();
    private Map<TreeNode, Map<Map<Variable, String>, Integer>> recursiveNodesStates = new HashMap<>();

    public TopDownDITA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down deterministic infinite tree automaton";
    }

    @Override
    public boolean isInfinitelyAccepted()
        throws UndefinedStateValueException, UndefinedAcceptanceException
    {
        for(Map<Map<Variable, String>, Integer> map : recursiveNodesStates.values())
            for(Map.Entry<Map<Variable, String>, Integer> entry : map.entrySet())
                if(entry.getValue() > 1 && infiniteAcceptanceConditions.check(entry.getKey()))
                    return true;

        return false;
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, EmptyTreeException
    {
        return super.isAccepted() && isInfinitelyAccepted();
    }

    @Override
    public void continueRecursive()
        throws RecursiveContinuationException
    {
        traversing.continueRecursive();
    }

    @Override
    public void addInfinitelyAcceptingConditions(Map<Variable, Pair<String, Boolean>> accept)
    {
        infiniteAcceptanceConditions.add(accept);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof TopDownDITA))
            return false;

        TopDownDITA other = (TopDownDITA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.infiniteAcceptanceConditions, other.infiniteAcceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions,
                            infiniteAcceptanceConditions);
    }

    @Override
    public String toString()
    {
        return "TopDownDITA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(traversing.hasNext() ? AutomatonRunningMode.RUNNING
                                            : traversing.canContinue()
                                              ? AutomatonRunningMode.CONTINUING
                                              : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
    {
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        recursiveNodesStates.clear();

        super.initialize();
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedStateValueException,
               NoSuchTransitionException
    {
        if(node.getType() == NodeType.REC)
            recursiveNodesStates.computeIfAbsent(node, k -> new HashMap<>())
                                .merge(node.getState(), 1, Integer::sum);

        super.processNode(node);
    }
}
