package ref_humbold.fita_view.automaton.traversing;

import java.util.Iterator;

import ref_humbold.fita_view.tree.TreeNode;

public interface TreeTraversing
    extends Iterator<Iterable<TreeNode>>
{
    /**
     * Setting nodes in tree to start traversing from.
     * @param nodes initial nodes
     */
    void initialize(TreeNode... nodes);

    /**
     * Removing all active nodes in the traversing.
     */
    void clear();
}
