package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFTA
    extends TopDownTreeAutomaton
{
    private Transitions<Pair<String, String>, Pair<String, String>> transitions =
        new Transitions<>();

    public TopDownDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    public boolean isAccepted()
    {
        return false;
    }

    @Override
    public void run()
    {
    }

    @Override
    public void makeStepForward()
    {

    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
    }

    @Override
    protected Pair<String, String> getTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        return transitions.get(var, Pair.make(value, label));
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
        throws DuplicatedTransitionException
    {
        Pair<String, String> key = Pair.make(value, label);

        if(transitions.containsKey(var, key))
            throw new DuplicatedTransitionException(
                "Duplicated transition entry for " + var + " + " + key + ".");

        transitions.add(var, key, Pair.make(leftResult, rightResult));
    }

    @Override
    public String toString()
    {
        return "TopDownDFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
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
            && Objects.equals(this.transitions, other.transitions);
    }
}
