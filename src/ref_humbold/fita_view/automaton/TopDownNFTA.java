package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownNFTA
    extends TopDownFiniteTreeAutomaton
    implements NonDeterministicAutomaton
{
    private StateChoice choice;
    private TopDownTransitions<Set<Pair<String, String>>> transitions = new TopDownTransitions<>();

    public TopDownNFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    public StateChoice getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice choice)
    {
        this.choice = choice;
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

        if(o == null || !(o instanceof TopDownNFTA))
            return false;

        TopDownNFTA other = (TopDownNFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "TopDownNFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        Set<Pair<String, String>> entrySet = getTransitionEntrySet(var, Pair.make(value, label));

        entrySet.add(Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> getTransitionResult(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        return choice.chooseState(transitions.get(var, Pair.make(value, label)));
    }

    private Set<Pair<String, String>> getTransitionEntrySet(Variable var, Pair<String, String> key)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        try
        {
            return transitions.get(var, key);
        }
        catch(NoSuchTransitionException e)
        {
            transitions.add(var, key, new HashSet<>());
            return getTransitionEntrySet(var, key);
        }
    }
}
