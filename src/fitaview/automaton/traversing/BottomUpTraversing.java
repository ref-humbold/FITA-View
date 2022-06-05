package fitaview.automaton.traversing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import fitaview.tree.TreeNode;

public abstract class BottomUpTraversing
        implements TreeTraversing
{
    protected final Queue<TreeNode> nodeQueue = new PriorityQueue<>(16, new NodeIndexComparator());

    @Override
    public void initialize(TreeNode... nodes)
    {
        clear();
        nodeQueue.addAll(Arrays.asList(nodes));
    }

    @Override
    public void clear()
    {
        nodeQueue.clear();
    }

    @Override
    public boolean hasNext()
    {
        return !nodeQueue.isEmpty();
    }

    private static class NodeIndexComparator
            implements Comparator<TreeNode>
    {
        @Override
        public int compare(TreeNode node1, TreeNode node2)
        {
            return -Integer.compare(node1.getIndex(), node2.getIndex());
        }
    }
}
