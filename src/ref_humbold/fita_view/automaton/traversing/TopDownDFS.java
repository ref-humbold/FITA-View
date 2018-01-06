package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.NodeType;
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
            throw new NoSuchElementException("No more nodes in traversing.");

        TreeNode node = nodeDeque.removeFirst();

        if(node.hasChildren())
        {
            addNextNode(node.getRight());
            addNextNode(node.getLeft());
        }

        return Collections.singletonList(node);
    }

    private void addNextNode(TreeNode node)
    {
        if(node.getType() == NodeType.REC)
            recursiveNodes.add(node);
        else
            nodeDeque.addFirst(node);
    }
}
