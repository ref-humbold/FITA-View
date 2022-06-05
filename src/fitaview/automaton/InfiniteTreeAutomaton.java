package fitaview.automaton;

import java.util.Map;

import fitaview.automaton.traversing.RecursiveContinuationException;
import fitaview.tree.UndefinedStateValueException;
import fitaview.utils.Pair;

public interface InfiniteTreeAutomaton
        extends TreeAutomaton
{
    /**
     * @return Buchi acceptance conditions for states in the automaton
     */
    AcceptanceConditions getBuchiAcceptanceConditions();

    /**
     * Testing if associated tree is accepted by the automaton in terms of Buchi acceptance.
     * @return {@code true} if automaton accepts tree, otherwise {@code false}
     */
    Boolean isBuchiAccepted()
            throws UndefinedStateValueException, UndefinedAcceptanceException;

    /**
     * Adding acceptance conditions for Buchi acceptance to the automaton.
     * @param accept mapping from variables to acceptance conditions on their values
     */
    void addBuchiAcceptanceConditions(Map<Variable, Pair<String, Boolean>> accept);

    /**
     * Reloading recursive nodes so as to continue recursive traversing.
     */
    void continueRecursive()
            throws RecursiveContinuationException;
}
