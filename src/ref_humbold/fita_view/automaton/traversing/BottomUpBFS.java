package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpBFS
    extends BottomUpTraversing
{
    private boolean canAddParent = false;

    @Override
    public void initialize(TreeVertex... vertices)
    {
        super.initialize(vertices);
        canAddParent = false;
    }

    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        TreeVertex vertex = vertexQueue.remove();

        if(vertex.getParent() != null && canAddParent)
            vertexQueue.add(vertex.getParent());

        canAddParent = !canAddParent;

        return Collections.singletonList(vertex);
    }
}
