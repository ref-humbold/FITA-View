package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class BottomUpLevel
    extends BottomUpTraversing
{
    int currentDepth;

    @Override
    public void initialize(TreeNode... nodes)
    {
        super.initialize(nodes);
        currentDepth = -1;

        for(TreeNode place : nodes)
            currentDepth = Math.max(currentDepth, countDepth(place.getIndex()));
    }

    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeNode> nodes = new ArrayList<>();
        int index = nodeQueue.element().getIndex();

        boolean canAddParent = false;

        while(index >= 1 << currentDepth)
        {
            TreeNode node = nodeQueue.remove();

            nodes.add(node);

            if(node.getParent() != null && canAddParent)
                nodeQueue.add(node.getParent());

            canAddParent = !canAddParent;
            index = hasNext() ? nodeQueue.element().getIndex() : -1;
        }

        --currentDepth;

        return nodes;
    }

    private Integer countDepth(Integer index)
    {
        return 31 - Integer.numberOfLeadingZeros(index);
    }
}
