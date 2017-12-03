package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeAutomaton
{
    void setTraversing(TraversingMode traversingMode);

    void setTree(TreeVertex tree);

    /**
     * Testing of label for presence in alphabet recognised by the automaton.
     * @param label label to test
     * @return {@code true} if label is present in alphabet, otherwise {@code false}
     */
    boolean isInAlphabet(String label);

    TreeVertex generateTree();

    void run();

    void makeStepForward();

    void makeStepBackward();
}
