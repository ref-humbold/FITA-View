package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpBFS
    extends TreeTraversing
{
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        TreeVertex vertex = vertexDeque.pollFirst();

        if(vertexDeque.peekLast() != vertex.getParent())
            vertexDeque.addLast(vertex.getParent());

        return Collections.singletonList(vertex);
    }
}
