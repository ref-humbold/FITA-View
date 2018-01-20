package ref_humbold.fita_view.automaton;

import java.util.Map;

import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;

public interface InfiniteTreeAutomaton
    extends TreeAutomaton
{
    /**
     * Reloading recursive nodes so as to continue recursive traversing.
     */
    void continueRecursive()
        throws RecursiveContinuationException;

    /**
     * Adding an infinitely appearing accepting state to the automaton.
     */
    void addInfinitelyAcceptingState(Map<Variable, String> accept);
}
