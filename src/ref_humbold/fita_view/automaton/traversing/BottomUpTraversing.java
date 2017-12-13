package ref_humbold.fita_view.automaton.traversing;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.TreeVertex;

public abstract class BottomUpTraversing
    implements TreeTraversing<Pair<TreeVertex, Integer>>
{
    Queue<Pair<TreeVertex, Integer>> vertexQueue = new PriorityQueue<>(16, new PairComparator());
    boolean canAddParent;
    int maxDepth;

    @Override
    public void initialize(Pair<TreeVertex, Integer> place)
    {
        clear();
        vertexQueue.add(place);
        maxDepth = countDepth(place.getSecond());
    }

    @Override
    public void initialize(Collection<Pair<TreeVertex, Integer>> places)
    {
        clear();
        vertexQueue.addAll(places);

        for(Pair<TreeVertex, Integer> place : places)
            maxDepth = Math.max(maxDepth, countDepth(place.getSecond()));
    }

    @Override
    public boolean hasNext()
    {
        return !vertexQueue.isEmpty();
    }

    private void clear()
    {
        vertexQueue.clear();
        canAddParent = false;
        maxDepth = -1;
    }

    private Integer countDepth(Integer index)
    {
        return 31 - Integer.numberOfLeadingZeros(index);
    }

    private class PairComparator
        implements Comparator<Pair<TreeVertex, Integer>>
    {
        @Override
        public int compare(Pair<TreeVertex, Integer> pair1, Pair<TreeVertex, Integer> pair2)
        {
            return -pair1.getSecond().compareTo(pair2.getSecond());
        }
    }
}
