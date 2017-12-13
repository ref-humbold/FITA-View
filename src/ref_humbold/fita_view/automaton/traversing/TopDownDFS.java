package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFS
    extends TopDownTraversing
{
    /**
     * Getting next vertex in depth-first search order.
     * @return next vertex
     */
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        TreeVertex vertex = vertexDeque.pollFirst();

        if(vertex.hasChildren())
        {
            vertexDeque.addFirst(vertex.getRight());
            vertexDeque.addFirst(vertex.getLeft());
        }

        return Collections.singletonList(vertex);
    }
}
