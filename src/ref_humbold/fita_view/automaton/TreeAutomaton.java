package ref_humbold.fita_view.automaton;

import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public interface TreeAutomaton
{
    /**
     * @return name of automaton's type
     */
    String getTypeName();

    /**
     * @return list of all variables in the automaton
     */
    List<Variable> getVariables();

    /**
     * @return label alphabet recognized by the automaton
     */
    Set<String> getAlphabet();

    /**
     * Testing if associated tree is accepted by the automaton.
     * @return {@code true} if automaton accepts tree, otherwise {@code false}
     * @throws UndefinedAcceptanceException if no accepting states were defined
     */
    boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException;

    /**
     * @return current traversing strategy of the automaton
     */
    TreeTraversing getTraversing();

    /**
     * @param mode new traversing mode for the automaton
     * @throws IncorrectTraversingException if traversing mode is not supported
     */
    void setTraversing(TraversingMode mode)
        throws IncorrectTraversingException;

    /**
     * @param sendingMessages when {@code true} then the automaton sends messages after making each step
     */
    void setSendingMessages(boolean sendingMessages);

    /**
     * @param tree new tree to run automaton on
     * @throws TreeFinitenessException if tree finiteness is not suitable for the automaton
     * @throws EmptyTreeException if tree is empty
     */
    void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException;

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
        throws IllegalVariableValueException, NoSuchTransitionException, NoTraversingException,
               UndefinedTreeStateException, EmptyTreeException;

    /**
     * Making a single traversing step of the automaton over the tree.
     * @throws IllegalVariableValueException if state of any node in tree is illegal
     * @throws NoSuchTransitionException if no transition entry was found
     * @throws NoTraversingException if no traversing strategy was set
     */
    void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException, NoTraversingException,
               UndefinedTreeStateException, EmptyTreeException;

    /**
     * Generating a sample tree that could be accepted by the automaton.
     * @return sample accepted tree
     */
    TreeNode generateTree();
}
