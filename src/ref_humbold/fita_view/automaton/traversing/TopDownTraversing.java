package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Deque;

import ref_humbold.fita_view.tree.TreeVertex;

public abstract class TopDownTraversing
    implements TreeTraversing<TreeVertex>
{
    protected Deque<TreeVertex> vertexDeque = new ArrayDeque<>();

    @Override
    public void initialize(TreeVertex place)
    {
        vertexDeque.clear();
        vertexDeque.addLast(place);
    }

    @Override
    public boolean hasNext()
    {
        return !vertexDeque.isEmpty();
    }
}
