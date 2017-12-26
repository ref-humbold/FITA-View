package ref_humbold.fita_view.automaton.traversing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import ref_humbold.fita_view.tree.TreeVertex;

public abstract class BottomUpTraversing
    implements TreeTraversing
{
    protected Queue<TreeVertex> vertexQueue = new PriorityQueue<>(16, new VertexIndexComparator());

    @Override
    public void initialize(TreeVertex... vertices)
    {
        vertexQueue.clear();
        vertexQueue.addAll(Arrays.asList(vertices));
    }

    @Override
    public boolean hasNext()
    {
        return !vertexQueue.isEmpty();
    }

    private class VertexIndexComparator
        implements Comparator<TreeVertex>
    {
        @Override
        public int compare(TreeVertex vertex1, TreeVertex vertex2)
        {
            return -Integer.compare(vertex1.getIndex(), vertex2.getIndex());
        }
    }
}
