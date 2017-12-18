package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpLevel
    extends BottomUpTraversing
{
    int currentDepth;

    @Override
    public void initialize(TreeVertex... vertices)
    {
        super.initialize(vertices);
        currentDepth = -1;

        for(TreeVertex place : vertices)
            currentDepth = Math.max(currentDepth, countDepth(place.getIndex()));
    }

    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeVertex> vertices = new ArrayList<>();
        int index = vertexQueue.element().getIndex();

        boolean canAddParent = false;

        while(index >= 1 << currentDepth)
        {
            TreeVertex vertex = vertexQueue.remove();

            vertices.add(vertex);

            if(vertex.getParent() != null && canAddParent)
                vertexQueue.add(vertex.getParent());

            canAddParent = !canAddParent;
            index = hasNext() ? vertexQueue.element().getIndex() : -1;
        }

        --currentDepth;

        return vertices;
    }

    private Integer countDepth(Integer index)
    {
        return 31 - Integer.numberOfLeadingZeros(index);
    }
}
