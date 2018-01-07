package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;

public interface InfiniteTreeAutomaton
    extends TreeAutomaton
{
    /**
     * Reloading recursive nodes so as to continue recursive traversing.
     */
    void continueRecursive()
        throws RecursiveContinuationException;
}
