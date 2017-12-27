package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class TopDownLevel
    extends TopDownTraversing
{
    /**
     * Getting next level nodes in breadth-first search order.
     * @return next nodes
     */
    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeNode> nodes = new ArrayList<>(nodeDeque);
        int length = nodeDeque.size();

        for(int i = 0; i < length; ++i)
        {
            TreeNode node = nodeDeque.removeFirst();

            if(node.hasChildren())
            {
                nodeDeque.addLast(node.getLeft());
                nodeDeque.addLast(node.getRight());
            }
        }

        return nodes;
    }
}
