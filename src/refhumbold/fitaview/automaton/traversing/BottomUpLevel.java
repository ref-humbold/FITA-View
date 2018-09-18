package refhumbold.fitaview.automaton.traversing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import refhumbold.fitaview.tree.TreeNode;

public class BottomUpLevel
    extends BottomUpTraversing
{
    int currentDepth;

    @Override
    public TraversingMode getMode()
    {
        return TraversingMode.LEVEL;
    }

    @Override
    public void initialize(TreeNode... nodes)
    {
        super.initialize(nodes);
        currentDepth = -1;

        Arrays.stream(nodes)
              .forEachOrdered(place -> currentDepth = Math.max(currentDepth, countDepth(place)));
    }

    @Override
    public Iterable<TreeNode> next()
    {
        if(!hasNext())
            throw new NoSuchElementException("No more nodes in traversing");

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

    private Integer countDepth(TreeNode node)
    {
        return 31 - Integer.numberOfLeadingZeros(node.getIndex());
    }
}
