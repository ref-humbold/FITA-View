package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class TopDownLevel
    extends TopDownTraversing
{
    @Override
    public TraversingMode getMode()
    {
        return TraversingMode.LEVEL;
    }

    /**
     * Getting next level nodes in breadth-first search order.
     * @return next nodes
     */
    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException("No more nodes in traversing.");

        appendRecursive();

        ArrayList<TreeNode> nodes = new ArrayList<>(nodeDeque);

        for(int i = 0; i < nodes.size(); ++i)
        {
            TreeNode node = nodeDeque.removeFirst();

            if(node.hasChildren())
            {
                processChild(node.getLeft());
                processChild(node.getRight());
            }
        }

        return nodes;
    }

    @Override
    protected void addNextNode(TreeNode node)
    {
        nodeDeque.addLast(node);
    }
}
