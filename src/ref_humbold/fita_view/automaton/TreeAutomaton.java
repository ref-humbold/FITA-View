package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeAutomaton
{
    void setTraversing(TraversingMode traversingMode);

    void setTree(TreeVertex tree);

    /**
     * Checking if given label is in alphabet recognised by the automaton
     * @param label label to check
     * @return true if label is in alphabet, otherwise false
     */
    boolean isInAlphabet(String label);

    TreeVertex generateTree();

    void run();

    void makeStepForward();

    void makeStepBackward();
}
