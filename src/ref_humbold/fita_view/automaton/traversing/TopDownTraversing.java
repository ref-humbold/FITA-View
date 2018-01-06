package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Queue;

import ref_humbold.fita_view.tree.TreeNode;

public abstract class TopDownTraversing
    implements TreeTraversing
{
    protected Deque<TreeNode> nodeDeque = new ArrayDeque<>();
    protected Queue<TreeNode> recursiveNodes = new ArrayDeque<>();

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
        recursiveNodes.clear();
    }

    @Override
    public boolean hasNext()
    {
        return !nodeDeque.isEmpty();
    }

    public boolean canContinue()
    {
        return !hasNext() && !recursiveNodes.isEmpty();
    }

    public void continueRecursive()
        throws RecursiveContinuationException
    {
        if(!canContinue())
            throw new RecursiveContinuationException(
                "Automaton is not ready yet to recursively continue traversing.");

        nodeDeque.addAll(recursiveNodes);
        recursiveNodes.clear();
    }
}
