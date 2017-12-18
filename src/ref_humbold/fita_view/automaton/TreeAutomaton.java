package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeAutomaton
{
    /**
     * Testing if associated tree is accepted by the automaton.
     * @return {@code true} if automaton accepts tree, otherwise {@code false}
     */
    boolean isAccepted()
        throws UndefinedAcceptanceException;

    void setTraversing(TraversingFactory.Mode mode)
        throws IncorrectTraversingException;

    void setTree(TreeVertex tree);

    /**
     * Testing if label is present in alphabet recognised by the automaton.
     * @param label label to test
     * @return {@code true} if label is present in alphabet, otherwise {@code false}
     */
    boolean isInAlphabet(String label);

    /**
     * Running full traversing of the automaton over the tree.
     * @throws IllegalVariableValueException if state of any node in tree is illegal
     * @throws NoSuchTransitionException if no transition entry was found
     * @throws NoTraversingException if no traversing strategy was set
     */
    void run()
        throws IllegalVariableValueException, NoSuchTransitionException, NoTraversingException;

    /**
     * Making a single traversing step of the automaton over the tree.
     * @throws IllegalVariableValueException if state of any node in tree is illegal
     * @throws NoSuchTransitionException if no transition entry was found
     * @throws NoTraversingException if no traversing strategy was set
     */
    void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException, NoTraversingException;

    /**
     * Generating a sample tree that could be accepted by the automaton.
     * @return sample accepted tree
     */
    TreeVertex generateTree();
}
