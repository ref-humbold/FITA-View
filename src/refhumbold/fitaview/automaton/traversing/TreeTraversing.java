package refhumbold.fitaview.automaton.traversing;

import java.util.Iterator;

import refhumbold.fitaview.tree.TreeNode;

public interface TreeTraversing
    extends Iterator<Iterable<TreeNode>>
{
    /**
     * @return mode of the traversing
     */
    TraversingMode getMode();

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
