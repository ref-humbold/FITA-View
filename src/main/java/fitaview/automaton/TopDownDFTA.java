package fitaview.automaton;

import java.util.Collection;
import java.util.Objects;

import fitaview.tree.TreeNode;
import fitaview.tree.UndefinedStateValueException;

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
    public Boolean isAccepted()
            throws UndefinedAcceptanceException, UndefinedStateValueException, NoTreeException
    {
        if(tree == null)
            throw new NoTreeException("No tree specified");

        if(leafStates.isEmpty())
            throw new UndefinedStateValueException("States in tree leaves are undefined");

        return super.isAccepted();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof TopDownDFTA))
            return false;

        TopDownDFTA other = (TopDownDFTA)o;

        return Objects.equals(alphabet, other.alphabet) && Objects.equals(variables,
                                                                          other.variables)
                && Objects.equals(acceptanceConditions, other.acceptanceConditions)
                && Objects.equals(transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return String.format("TopDownDFTA\n  alphabet = %s\n  variables = %s\n  transitions = %s",
                             alphabet, variables, transitions);
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
            throw new TreeFinitenessException("Tree is infinite");
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(traversing.hasNext()
                               ? AutomatonRunningMode.RUNNING
                               : AutomatonRunningMode.FINISHED);
    }
}
