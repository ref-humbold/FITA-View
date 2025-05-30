package fitaview.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fitaview.automaton.transition.NoSuchTransitionException;
import fitaview.automaton.traversing.RecursiveContinuationException;
import fitaview.automaton.traversing.TopDownDfs;
import fitaview.automaton.traversing.TopDownTraversing;
import fitaview.tree.NodeType;
import fitaview.tree.TreeNode;
import fitaview.tree.UndefinedStateValueException;
import fitaview.utils.Pair;

public class TopDownDita
        extends TopDownDeterministicAutomaton
        implements InfiniteTreeAutomaton
{
    private final AcceptanceConditions infiniteAcceptanceConditions = new AcceptanceConditions();
    private final Map<TreeNode, Map<Map<Variable, String>, Integer>> repeatingStates =
            new HashMap<>();
    private final Map<TreeNode, Integer> numberRecursive = new HashMap<>();
    private int maximumRecursive;

    public TopDownDita(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down deterministic infinite tree automaton";
    }

    @Override
    public AcceptanceConditions getBuchiAcceptanceConditions()
    {
        return infiniteAcceptanceConditions;
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

        boolean allNotAccept = false;

        for(Map<Map<Variable, String>, Integer> map : repeatingStates.values())
            for(Map.Entry<Map<Variable, String>, Integer> entry : map.entrySet())
                if(entry.getValue() > maximumRecursive + 2)
                    allNotAccept |= !infiniteAcceptanceConditions.check(entry.getKey());

        return allNotAccept ? false : null;
    }

    @Override
    public Boolean isAccepted()
            throws UndefinedAcceptanceException, UndefinedStateValueException, NoTreeException
    {
        if(tree == null)
            throw new NoTreeException("No tree specified");

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

        if(!(o instanceof TopDownDita))
            return false;

        TopDownDita other = (TopDownDita)o;

        return Objects.equals(alphabet, other.alphabet) && Objects.equals(variables,
                                                                          other.variables)
                       && Objects.equals(acceptanceConditions, other.acceptanceConditions)
                       && Objects.equals(infiniteAcceptanceConditions,
                                         other.infiniteAcceptanceConditions) && Objects.equals(
                transitions, other.transitions);
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
        return String.format("TopDownDITA\n  alphabet = %s\n  variables = %s\n  transitions = %s",
                             alphabet, variables, transitions);
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
            throws TreeFinitenessException
    {
        if(!containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is infinite");
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(traversing.hasNext()
                       ? AutomatonRunningMode.RUNNING
                       : traversing.canContinue()
                         ? AutomatonRunningMode.CONTINUING
                         : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void initialize()
            throws IllegalVariableValueException, NoTreeException, NoTraversingStrategyException,
                   NoNonDeterministicStrategyException
    {
        super.initialize();
        findRepeating();
        maximumRecursive = numberRecursive.values().stream().reduce(0, Math::max);
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
        TopDownTraversing t = new TopDownDfs();

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
