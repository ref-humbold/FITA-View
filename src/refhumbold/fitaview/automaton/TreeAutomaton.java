package refhumbold.fitaview.automaton;

import java.util.Collection;
import java.util.Map;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.automaton.transition.NoSuchTransitionException;
import refhumbold.fitaview.automaton.traversing.IncorrectTraversingException;
import refhumbold.fitaview.automaton.traversing.TraversingMode;
import refhumbold.fitaview.automaton.traversing.TreeTraversing;
import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.tree.UndefinedStateValueException;

public interface TreeAutomaton
{
    /**
     * @return type name of the automaton
     */
    String getTypeName();

    /**
     * @return traversing direction of the automaton
     */
    AutomatonDirection getDirection();

    /**
     * @return all variables in the automaton
     */
    Collection<Variable> getVariables();

    /**
     * @return label alphabet recognized by the automaton
     */
    Collection<String> getAlphabet();

    /**
     * @return current traversing strategy of the automaton
     */
    TreeTraversing getTraversing();

    /**
     * Setting a new traversing strategy specified by traversing mode.
     * @param mode new traversing mode for the automaton
     * @throws IncorrectTraversingException if traversing mode is not supported
     */
    void setTraversing(TraversingMode mode)
        throws IncorrectTraversingException, AutomatonIsRunningException;

    /**
     * @return transition relation as string representation
     */
    Map<Pair<Variable, String>, String> getTransitionAsStrings();

    /**
     * @return acceptance conditions for states in the automaton
     */
    AcceptanceConditions getAcceptanceConditions();

    /**
     * @return running mode of the automaton
     */
    AutomatonRunningMode getRunningMode();

    /**
     * Testing if the automaton is traversing an associated tree.
     * @return {@code true} if the automaton is running, otherwise {@code false}
     */
    default boolean isRunning()
    {
        return getRunningMode() == AutomatonRunningMode.RUNNING
            || getRunningMode() == AutomatonRunningMode.CONTINUING;
    }

    /**
     * @param tree new tree to associate the automaton with
     * @throws TreeFinitenessException if tree finiteness is not suitable for the automaton
     */
    void setTree(TreeNode tree)
        throws TreeFinitenessException;

    /**
     * @param sendingMessages if {@code true} then the automaton sends messages informing about its work
     */
    void setSendingMessages(boolean sendingMessages);

    /**
     * Testing if associated tree is accepted by the automaton.
     * @return {@code true} if automaton accepts tree, otherwise {@code false}
     * @throws UndefinedAcceptanceException if no acceptance conditions were defined
     * @throws UndefinedStateValueException if state of the tree is undefined
     * @throws NoTreeException if the automaton contains no tree
     */
    Boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, NoTreeException;

    /**
     * Adding acceptance conditions for states to the automaton.
     * @param accept mapping from variables to acceptance conditions on their values
     */
    void addAcceptanceConditions(Map<Variable, Pair<String, Boolean>> accept);

    /**
     * Testing if label is present in alphabet recognised by the automaton.
     * @param label label to test
     * @return {@code true} if label is present in alphabet, otherwise {@code false}
     */
    boolean isInAlphabet(String label);

    /**
     * Running full traversing of the automaton over the tree until leaves or recursive nodes.
     * @throws IllegalVariableValueException if state of any node in tree is illegal
     * @throws NoSuchTransitionException if no transition entry was found
     * @throws NoTraversingStrategyException if no traversing strategy was set
     * @throws NoTreeException if the automaton contains no tree
     * @throws NoNonDeterministicStrategyException if strategy was not specified
     */
    void run()
        throws IllegalVariableValueException, NoSuchTransitionException,
               NoTraversingStrategyException, UndefinedStateValueException, NoTreeException,
               NoNonDeterministicStrategyException;

    /**
     * Making a single traversing step of the automaton over the tree.
     * @throws IllegalVariableValueException if state of any node in tree is illegal
     * @throws NoSuchTransitionException if no transition entry was found
     * @throws NoTraversingStrategyException if no traversing strategy was set
     * @throws NoTreeException if the automaton contains no tree
     * @throws NoNonDeterministicStrategyException if strategy was not specified
     */
    void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException,
               NoTraversingStrategyException, UndefinedStateValueException, NoTreeException,
               NoNonDeterministicStrategyException;

    /**
     * Stopping traversing the tree by the automaton.
     */
    void stopTraversing();
}
