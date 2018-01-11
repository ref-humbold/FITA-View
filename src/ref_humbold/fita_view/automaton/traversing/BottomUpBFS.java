package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class BottomUpBFS
    extends BottomUpTraversing
{
    private boolean canAddParent = false;

    @Override
    public void initialize(TreeNode... nodes)
    {
        super.initialize(nodes);
        canAddParent = false;
    }

    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException("No more nodes in traversing.");

        TreeNode node = nodeQueue.remove();

        if(node.getParent() != null && canAddParent)
            nodeQueue.add(node.getParent());

        canAddParent = !canAddParent;

        return Collections.singletonList(node);
    }
}
