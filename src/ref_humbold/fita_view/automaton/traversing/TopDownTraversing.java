package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import ref_humbold.fita_view.tree.TreeVertex;

public abstract class TopDownTraversing
    implements TreeTraversing
{
    protected Deque<TreeVertex> vertexDeque = new ArrayDeque<>();

    @Override
    public void initialize(TreeVertex... vertices)
    {
        vertexDeque.clear();
        vertexDeque.addAll(Arrays.asList(vertices));
    }

    @Override
    public boolean hasNext()
    {
        return !vertexDeque.isEmpty();
    }
}
