package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownLevel
    extends TopDownTraversing
{
    /**
     * Getting next level vertices in breadth-first search order.
     * @return next vertices
     */
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeVertex> vertices = new ArrayList<>(vertexDeque);
        int length = vertexDeque.size();

        for(int i = 0; i < length; ++i)
        {
            TreeVertex vertex = vertexDeque.removeFirst();

            if(vertex.hasChildren())
            {
                vertexDeque.addLast(vertex.getLeft());
                vertexDeque.addLast(vertex.getRight());
            }
        }

        return vertices;
    }
}
