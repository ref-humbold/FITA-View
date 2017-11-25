package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFTA
    extends TopDownTreeAutomaton
{
    private Map<Triple<Variable, String, String>, Pair<String, String>> transitions;

    public TopDownDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
        this.transitions = new HashMap<>();
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
        transitions.put(Triple.make(var, value, label), Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> doTransition(Variable var, String value, String label)
    {
        return transitions.get(Triple.make(var, value, label));
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
