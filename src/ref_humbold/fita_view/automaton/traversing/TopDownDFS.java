package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class TopDownDFS
    extends TopDownTraversing
{
    /**
     * Getting next node in depth-first search order.
     * @return next node
     */
    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        TreeNode node = nodeDeque.removeFirst();

        if(node.hasChildren())
        {
            nodeDeque.addFirst(node.getRight());
            nodeDeque.addFirst(node.getLeft());
        }

        return Collections.singletonList(node);
    }
}
