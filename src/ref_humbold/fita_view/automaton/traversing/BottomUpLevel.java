package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpLevel
    extends TreeTraversing
{
    @Override
    public void initialize(Collection<TreeVertex> vertices)
    {
        super.initialize(vertices);
        vertexDeque.add(null);
    }

    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeVertex> vertices = new ArrayList<>();
        TreeVertex vertex = vertexDeque.pollFirst();

        while(vertex != null)
        {
            vertices.add(vertex);

            if(vertexDeque.peekLast() != vertex.getParent())
                vertexDeque.addLast(vertex.getParent());

            vertex = vertexDeque.pollFirst();
        }

        return vertices;
    }
}
