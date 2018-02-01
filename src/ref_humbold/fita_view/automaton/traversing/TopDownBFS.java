package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class TopDownBFS
    extends TopDownTraversing
{
    @Override
    public TraversingMode getMode()
    {
        return TraversingMode.BFS;
    }

    /**
     * Getting next node in breadth-first search order.
     * @return next node
     */
    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException("No more nodes in traversing.");

        appendRecursive();

        TreeNode node = nodeDeque.removeFirst();

        if(!node.isLeaf())
        {
            processChild(node.getLeft());
            processChild(node.getRight());
        }

        return Collections.singletonList(node);
    }

    @Override
    protected void addNextNode(TreeNode node)
    {
        nodeDeque.addLast(node);
    }
}
