package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Objects;

import ref_humbold.fita_view.tree.TreeNode;

public class TopDownDFTA
    extends TopDownDeterministicAutomaton
{
    public TopDownDFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down deterministic finite tree automaton";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof TopDownDFTA))
            return false;

        TopDownDFTA other = (TopDownDFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownDFTA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is infinite.");
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }
}
