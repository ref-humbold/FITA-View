package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.NodeType;
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
            throw new NoSuchElementException("No more nodes in traversing.");

        ArrayList<TreeNode> nodes = new ArrayList<>(nodeDeque);

        for(int i = 0; i < nodes.size(); ++i)
        {
            TreeNode node = nodeDeque.removeFirst();

            if(node.hasChildren())
            {
                addNextNode(node.getLeft());
                addNextNode(node.getRight());
            }
        }

        return nodes;
    }

    private void addNextNode(TreeNode node)
    {
        if(node.getType() == NodeType.REC)
            recursiveNodes.add(node);
        else
            nodeDeque.addLast(node);
    }
}
