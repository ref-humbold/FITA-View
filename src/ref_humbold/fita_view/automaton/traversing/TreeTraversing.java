package ref_humbold.fita_view.automaton.traversing;

import java.util.Iterator;

import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeTraversing
    extends Iterator<Iterable<TreeVertex>>
{
    /**
     * Setting vertices in tree to start traversing from.
     * @param vertices initial vertices
     */
    void initialize(TreeVertex... vertices);
}
