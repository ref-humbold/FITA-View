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
    public void run()
    {
    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
    {
        transitions.add(var, Pair.make(value, label), Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> doTransition(Variable var, String value, String label)
    {
        Pair<String, String> result = transitions.get(var, Pair.make(value, label));

        return removeWildcard(value, result);
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
