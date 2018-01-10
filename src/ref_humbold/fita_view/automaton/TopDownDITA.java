package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Objects;

import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;
import ref_humbold.fita_view.tree.TreeNode;

public class TopDownDITA
    extends TopDownDFTA
    implements InfiniteTreeAutomaton
{
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
    public TreeNode generateTree()
    {
        return null;
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
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownDITA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public void continueRecursive()
        throws RecursiveContinuationException
    {
        traversing.continueRecursive();
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
