package refhumbold.fitaview.automaton.traversing;

import java.util.*;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.automaton.IllegalVariableValueException;
import refhumbold.fitaview.automaton.Variable;
import refhumbold.fitaview.tree.NodeType;
import refhumbold.fitaview.tree.TreeNode;

public abstract class TopDownTraversing
    implements TreeTraversing
{
    protected Deque<TreeNode> nodeDeque = new ArrayDeque<>();
    private Queue<Pair<TreeNode, Map<Variable, String>>> pendingRecursiveNodes = new ArrayDeque<>();
    private Queue<Pair<TreeNode, Map<Variable, String>>> newRecursiveNodes = new ArrayDeque<>();

    @Override
    public void initialize(TreeNode... nodes)
    {
        this.clear();
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
                "Automaton is not ready yet to recursively continue traversing.");

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
                throw new IllegalStateException("Unexpected TreeNode#setState error.");
            }

            addNextNode(pair.getFirst());
        }
    }
}
