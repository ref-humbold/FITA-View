package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFTA
    extends TopDownTreeAutomaton
{
    private TopDownTransitions<Pair<String, String>> transitions = new TopDownTransitions<>();

    public TopDownDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
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

    @Override
    public String toString()
    {
        return "TopDownDFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Pair.make(value, label), Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> getTransitionResult(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        return transitions.get(var, Pair.make(value, label));
    }
}
