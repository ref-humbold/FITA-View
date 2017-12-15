package ref_humbold.fita_view.automaton.traversing;

import java.util.Iterator;

import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeTraversing<T>
    extends Iterator<Iterable<TreeVertex>>
{
    /**
     * Setting place in tree to start traversing from.
     * @param place initial place
     */
    void initialize(T place);
}
