package ref_humbold.fita_view.automaton.traversing;

import java.util.Collection;
import java.util.Iterator;

import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeTraversing<T>
    extends Iterable<Iterable<TreeVertex>>, Iterator<Iterable<TreeVertex>>
{
    /**
     * Setting places in tree to start traversing from.
     * @param places initial places
     */
    void initialize(Collection<T> places);

    /**
     * Setting single place to start traversing from.
     * @param place initial place
     */
    void initialize(T place);

    @Override
    default Iterator<Iterable<TreeVertex>> iterator()
    {
        return this;
    }
}
