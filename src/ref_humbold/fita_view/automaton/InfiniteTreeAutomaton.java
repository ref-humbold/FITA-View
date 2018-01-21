package ref_humbold.fita_view.automaton;

import java.util.Map;

import ref_humbold.fita_view.Pair;
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
     * Adding accepting conditions for infinitely appearing state to the automaton.
     * @param accept mapping from variables to accepting conditions on their values
     */
    void addInfinitelyAcceptingConditions(Map<Variable, Pair<String, Boolean>> accept);
}
