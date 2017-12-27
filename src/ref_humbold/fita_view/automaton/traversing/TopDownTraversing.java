package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import ref_humbold.fita_view.tree.TreeNode;

public abstract class TopDownTraversing
    implements TreeTraversing
{
    protected Deque<TreeNode> nodeDeque = new ArrayDeque<>();

    @Override
    public void initialize(TreeNode... nodes)
    {
        nodeDeque.clear();
        nodeDeque.addAll(Arrays.asList(nodes));
    }

    @Override
    public boolean hasNext()
    {
        return !nodeDeque.isEmpty();
    }
}
