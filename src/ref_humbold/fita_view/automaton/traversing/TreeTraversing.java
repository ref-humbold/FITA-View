package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import ref_humbold.fita_view.tree.TreeVertex;

public abstract class TreeTraversing
    implements Iterator<Iterable<TreeVertex>>
{
    Deque<TreeVertex> vertexDeque = new ArrayDeque<>();

    /**
     * Setting vertices to start traversing from.
     * @param vertices initial vertices
     */
    public void initialize(Collection<TreeVertex> vertices)
    {
        vertexDeque.clear();
        vertexDeque.addAll(vertices);
    }

    /**
     * Setting single vertex to start traversing from.
     * @param vertex initial vertex
     */
    public void initialize(TreeVertex vertex)
    {
        vertexDeque.clear();
        vertexDeque.addLast(vertex);
    }

    @Override
    public boolean hasNext()
    {
        return !vertexDeque.isEmpty();
    }
}
