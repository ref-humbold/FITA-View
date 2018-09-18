package refhumbold.fitaview.automaton;

import java.util.Collection;
import java.util.Objects;

import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.tree.UndefinedStateValueException;

public class TopDownNFTA
    extends TopDownNondeterministicAutomaton
{
    public TopDownNFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public String getTypeName()
    {
        return "Top-down non-deterministic finite tree automaton";
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

        if(!(o instanceof TopDownNFTA))
            return false;

        TopDownNFTA other = (TopDownNFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownNFTA\n  alphabet = " + alphabet.toString() + "\n  variables = "
            + variables.toString() + "\n  transitions = " + transitions.toString();
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
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }
}
