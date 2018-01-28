package ref_humbold.fita_view.automaton.traversing;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;

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

    protected abstract void addNextNode(TreeNode node);

    protected void processChild(TreeNode node)
    {
        if(node.getType() == NodeType.REC)
            newRecursiveNodes.add(Pair.make(node, node.getStateWithNulls()));
        else
            addNextNode(node);
    }

    protected void appendRecursive()
    {
        if(nodeDeque.isEmpty() && !pendingRecursiveNodes.isEmpty())
        {
            Pair<TreeNode, Map<Variable, String>> pending = pendingRecursiveNodes.remove();

            try
            {
                pending.getFirst().setState(pending.getSecond());
            }
            catch(IllegalVariableValueException e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }

            addNextNode(pending.getFirst());
        }
    }
}
