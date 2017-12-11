package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownNFTA
    extends TopDownTreeAutomaton
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
        return choice.chooseState(transitions.get(var, Pair.make(value, label)));
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
    {
        Pair<String, String> key = Pair.make(value, label);

        if(!transitions.containsKey(var, key))
            transitions.add(var, key, new HashSet<>());

        try
        {
            transitions.get(var, key).add(Pair.make(leftResult, rightResult));
        }
        catch(NoSuchTransitionException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString()
    {
        return "TopDownNFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
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
}
