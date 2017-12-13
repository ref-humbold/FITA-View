package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpLevel
    extends BottomUpTraversing
{
    @Override
    public Iterable<TreeVertex> next()
    {
        if(!hasNext())
            throw new NoSuchElementException();

        ArrayList<TreeVertex> vertices = new ArrayList<>();
        int index = vertexQueue.peek().getSecond();

        canAddParent = false;

        while(index >= 1 << maxDepth)
        {
            Pair<TreeVertex, Integer> vertexPair = vertexQueue.poll();
            TreeVertex vertex = vertexPair.getFirst();

            index = vertexPair.getSecond();
            vertices.add(vertex);

            if(vertex.getParent() != null && canAddParent)
                vertexQueue.add(Pair.make(vertex.getParent(), index / 2));

            canAddParent = !canAddParent;
            index = hasNext() ? vertexQueue.peek().getSecond() : -1;
        }

        --maxDepth;

        return vertices;
    }
}
