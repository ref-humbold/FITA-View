package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;
import ref_humbold.fita_view.automaton.traversing.TopDownDFS;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedStateValueException;

public class TopDownNITA
    extends TopDownNFTA
    implements InfiniteTreeAutomaton
{
    private Integer statesNum;
    private AcceptanceConditions infiniteAcceptanceConditions = new AcceptanceConditions();
    private Map<TreeNode, Map<Map<Variable, String>, Boolean>> repeatingStates = new HashMap<>();

    public TopDownNITA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);

        statesNum = variables.parallelStream()
                             .reduce(1, (u, var) -> u * var.size(), (a, b) -> a * b);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down non-deterministic infinite tree automaton";
    }

    @Override
    public Boolean isInfinitelyAccepted()
        throws UndefinedStateValueException, UndefinedAcceptanceException
    {
        if(repeatingStates.isEmpty())
            return true;

        for(Map<Map<Variable, String>, Boolean> map : repeatingStates.values())
            for(Map.Entry<Map<Variable, String>, Boolean> entry : map.entrySet())
                if(entry.getValue() && infiniteAcceptanceConditions.check(entry.getKey()))
                    return true;

        boolean allStates = repeatingStates.values()
                                           .stream()
                                           .map(map -> (map.keySet().size() == statesNum))
                                           .reduce(true, (a, b) -> a && b);

        return allStates ? false : null;
    }

    @Override
    public Boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, EmptyTreeException
    {
        Boolean infiniteAcc = isInfinitelyAccepted();

        return infiniteAcc == null ? null : infiniteAcc && super.isAccepted();
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

        if(o == null || !(o instanceof TopDownNITA))
            return false;

        TopDownNITA other = (TopDownNITA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.infiniteAcceptanceConditions, other.infiniteAcceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownNITA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions,
                            infiniteAcceptanceConditions);
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(traversing.hasNext() ? AutomatonRunningMode.RUNNING
                                            : traversing.canContinue()
                                              ? AutomatonRunningMode.CONTINUING
                                              : AutomatonRunningMode.STOPPED);
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
        super.initialize();
        findRepeating();
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedStateValueException,
               NoSuchTransitionException
    {
        if(repeatingStates.containsKey(node))
        {
            repeatingStates.get(node).computeIfPresent(node.getState(), (k, v) -> true);
            repeatingStates.get(node).putIfAbsent(node.getState(), false);
        }

        super.processNode(node);
    }

    private void findRepeating()
    {
        TopDownTraversing t = new TopDownDFS();

        repeatingStates.clear();
        t.initialize(tree);

        t.forEachRemaining(iterator -> iterator.forEach(v -> {
            if(v.getLeft().getType() == NodeType.REC)
                addRepeating(v);

            if(v.getRight().getType() == NodeType.REC)
                addRepeating(v);
        }));
    }

    private void addRepeating(TreeNode node)
    {
        while(node != null && node.getType() != NodeType.REPEAT)
        {
            repeatingStates.put(node, new HashMap<>());
            node = node.getParent();
        }
    }
}
