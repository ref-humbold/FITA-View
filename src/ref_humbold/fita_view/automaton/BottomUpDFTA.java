package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.TreeNode;

public class BottomUpDFTA
    extends BottomUpAutomaton
{
    private BottomUpTransitions<String> transitions = new BottomUpTransitions<>();

    public BottomUpDFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionWithStrings()
    {
        return this.transitions.convertToStringMap(this::keyToString, this::valueToString);
    }

    @Override
    public String getTypeName()
    {
        return "Bottom-up deterministic finite tree automaton";
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

        if(o == null || !(o instanceof BottomUpDFTA))
            return false;

        BottomUpDFTA other = (BottomUpDFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "BottomUpDFTA:\n  alphabet = " + alphabet.toString() + "\n  variables = " + variables
            .toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    @Override
    public void addTransition(Variable var, String leftValue, String rightValue, String label,
                              String result)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Triple.make(leftValue, rightValue, label), result);
    }

    @Override
    protected String getTransitionResult(Variable var, String leftValue, String rightValue,
                                         String label)
        throws NoSuchTransitionException
    {
        return transitions.get(var, Triple.make(leftValue, rightValue, label));
    }
}
