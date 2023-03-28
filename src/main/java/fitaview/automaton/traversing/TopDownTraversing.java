package fitaview.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.Queue;

import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;
import fitaview.tree.NodeType;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;

public abstract class TopDownTraversing
        implements TreeTraversing
{
    protected final Deque<TreeNode> nodeDeque = new ArrayDeque<>();
    private final Queue<Pair<TreeNode, Map<Variable, String>>> pendingRecursiveNodes =
            new ArrayDeque<>();
    private final Queue<Pair<TreeNode, Map<Variable, String>>> newRecursiveNodes =
            new ArrayDeque<>();

    @Override
    public void initialize(TreeNode... nodes)
    {
        clear();
        nodeDeque.addAll(Arrays.asList(nodes));
    }

    @Override
    public void clear()
    {
        nodeDeque.clear();
        pendingRecursiveNodes.clear();
        newRecursiveNodes.clear();
    }

    @Override
    public boolean hasNext()
    {
        return !nodeDeque.isEmpty() || !pendingRecursiveNodes.isEmpty();
    }

    public boolean canContinue()
    {
        return !hasNext() && !newRecursiveNodes.isEmpty();
    }

    public void continueRecursive()
            throws RecursiveContinuationException
    {
        if(!canContinue())
            throw new RecursiveContinuationException(
                    "Automaton is not ready yet to recursively continue traversing");

        pendingRecursiveNodes.addAll(newRecursiveNodes);
        newRecursiveNodes.clear();
    }

    public void addNewRecursive(TreeNode node)
    {
        if(node.getType() == NodeType.REC)
            newRecursiveNodes.add(Pair.make(node, node.getStateWithNulls()));
    }

    protected abstract void addNextNode(TreeNode node);

    protected void processChild(TreeNode node)
    {
        if(node.getType() != NodeType.REC)
            addNextNode(node);
    }

    protected void appendRecursive()
    {
        if(nodeDeque.isEmpty() && !pendingRecursiveNodes.isEmpty())
        {
            Pair<TreeNode, Map<Variable, String>> pair = pendingRecursiveNodes.remove();

            try
            {
                pair.getFirst().setState(pair.getSecond());
            }
            catch(IllegalVariableValueException e)
            {
                throw new IllegalStateException("Unexpected TreeNode#setState error", e);
            }

            addNextNode(pair.getFirst());
        }
    }
}
