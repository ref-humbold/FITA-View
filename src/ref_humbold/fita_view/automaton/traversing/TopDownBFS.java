package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownBFS
    extends TopDownTraversing
{
    /**
     * Getting next vertex in breadth-first search order.
     * @return next vertex
     */
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        TreeVertex vertex = vertexDeque.removeFirst();

        if(vertex.hasChildren())
        {
            vertexDeque.addLast(vertex.getLeft());
            vertexDeque.addLast(vertex.getRight());
        }

        return Collections.singletonList(vertex);
    }
}
