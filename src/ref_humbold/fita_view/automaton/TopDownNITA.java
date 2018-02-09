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
    extends TopDownNondeterministicAutomaton
    implements InfiniteTreeAutomaton
{
    private AcceptanceConditions infiniteAcceptanceConditions = new AcceptanceConditions();
    private Map<TreeNode, Map<Map<Variable, String>, Integer>> repeatingStates = new HashMap<>();
    private Map<TreeNode, Integer> numberRecursive = new HashMap<>();
    private int maximumRecursive;

    public TopDownNITA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down non-deterministic infinite tree automaton";
    }

    @Override
    public AcceptanceConditions getBuchiAcceptanceConditions()
    {
        return this.infiniteAcceptanceConditions;
    }

    @Override
    public Boolean isBuchiAccepted()
        throws UndefinedStateValueException, UndefinedAcceptanceException
    {
        for(Map<Map<Variable, String>, Integer> map : repeatingStates.values())
            for(Map.Entry<Map<Variable, String>, Integer> entry : map.entrySet())
                if(entry.getValue() >= maximumRecursive + 2 && infiniteAcceptanceConditions.check(
                    entry.getKey()))
                    return true;

        boolean allNotAccept = true;

        for(Map<Map<Variable, String>, Integer> map : repeatingStates.values())
            for(Map.Entry<Map<Variable, String>, Integer> entry : map.entrySet())
                if(entry.getValue() > maximumRecursive + 2)
                    allNotAccept &= infiniteAcceptanceConditions.check(entry.getKey());

        return allNotAccept ? null : false;
    }

    @Override
    public Boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, EmptyTreeException
    {
        Boolean infiniteAcc = isBuchiAccepted();

        return infiniteAcc == null ? null : infiniteAcc && super.isAccepted();
    }

    @Override
    public void continueRecursive()
        throws RecursiveContinuationException
    {
        traversing.continueRecursive();
    }

    @Override
    public void addBuchiAcceptanceConditions(Map<Variable, Pair<String, Boolean>> accept)
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
        throws TreeFinitenessException
    {
        if(!containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is finite.");
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
            repeatingStates.get(node).putIfAbsent(node.getState(), 0);
            repeatingStates.get(node).computeIfPresent(node.getState(), (k, v) -> v + 1);
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
                addRepeating(v.getLeft());

            if(v.getRight().getType() == NodeType.REC)
                addRepeating(v.getRight());
        }));
    }

    private void addRepeating(TreeNode node)
    {
        while(node != null && node.getType() != NodeType.REPEAT)
        {
            repeatingStates.put(node, new HashMap<>());
            node = node.getParent();
        }

        if(node != null && node.getType() == NodeType.REPEAT)
        {
            numberRecursive.putIfAbsent(node, 0);
            numberRecursive.computeIfPresent(node, (k, v) -> v + 1);
        }
    }
}
