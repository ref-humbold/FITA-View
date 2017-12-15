package ref_humbold.fita_view.automaton.traversing;

import java.util.Collections;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpBFS
    extends BottomUpTraversing
{
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        Pair<TreeVertex, Integer> vertexPair = vertexQueue.remove();
        TreeVertex vertex = vertexPair.getFirst();
        int depth = vertexPair.getSecond();

        if(vertex.getParent() != null && canAddParent)
            vertexQueue.add(Pair.make(vertex.getParent(), depth / 2));

        canAddParent = !canAddParent;

        return Collections.singletonList(vertex);
    }
}
