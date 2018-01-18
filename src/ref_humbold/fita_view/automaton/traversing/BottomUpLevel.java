package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeNode;

public class BottomUpLevel
    extends BottomUpTraversing
{
    int currentHeight;

    @Override
    public TraversingMode getMode()
    {
        return TraversingMode.LEVEL;
    }

    @Override
    public void initialize(TreeNode... nodes)
    {
        super.initialize(nodes);
        currentHeight = -1;

        for(TreeNode place : nodes)
            currentHeight = Math.max(currentHeight, countHeight(place));
    }

    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException("No more nodes in traversing.");

        ArrayList<TreeNode> nodes = new ArrayList<>();
        int index = nodeQueue.element().getIndex();

        boolean canAddParent = false;

        while(index >= 1 << currentHeight)
        {
            TreeNode node = nodeQueue.remove();

            nodes.add(node);

            if(node.getParent() != null && canAddParent)
                nodeQueue.add(node.getParent());

            canAddParent = !canAddParent;
            index = hasNext() ? nodeQueue.element().getIndex() : -1;
        }

        --currentHeight;

        return nodes;
    }

    private Integer countHeight(TreeNode node)
    {
        return 31 - Integer.numberOfLeadingZeros(node.getIndex());
    }
}
