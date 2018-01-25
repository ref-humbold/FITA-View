package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;
import ref_humbold.fita_view.tree.TreeNode;

public class TopDownDITA
    extends TopDownDFTA
    implements InfiniteTreeAutomaton
{
    private AcceptanceConditions infiniteAcceptanceConditions = new AcceptanceConditions();

    public TopDownDITA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down deterministic infinite tree automaton";
    }

    @Override
    public boolean isInfinitelyAccepted()
    {
        return false;
    }

    @Override
    public TreeNode generateTree()
    {
        return null;
    }

    @Override
    public void continueRecursive()
        throws RecursiveContinuationException
    {
        traversing.continueRecursive();
    }

    @Override
    public void addInfinitelyAcceptingConditions(Map<Variable, Pair<String, Boolean>> accept)
    {
        infiniteAcceptanceConditions.add(accept);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof TopDownDITA))
            return false;

        TopDownDITA other = (TopDownDITA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.infiniteAcceptanceConditions, other.infiniteAcceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions,
                            infiniteAcceptanceConditions);
    }

    @Override
    public String toString()
    {
        return "TopDownDITA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(traversing.hasNext() ? AutomatonRunningMode.RUNNING
                                            : traversing.canContinue()
                                              ? AutomatonRunningMode.CONTINUING
                                              : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
    {
    }
}
