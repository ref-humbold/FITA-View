package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeAutomaton
{
    void setTraversing(TraversingMode traversingMode)
        throws IncorrectTraversingException;

    void setTree(TreeVertex tree);

    /**
     * Testing of label for presence in alphabet recognised by the automaton.
     * @param label label to test
     * @return {@code true} if label is present in alphabet, otherwise {@code false}
     */
    boolean isInAlphabet(String label);

    /**
     * Testing if given tree is accepted by the automaton.
     * @return {@code true} if automaton accepts tree, otherwise {@code false}
     */
    boolean isAccepted();

    void run();

    void makeStepForward();

    TreeVertex generateTree();
}
