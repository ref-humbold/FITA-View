package refhumbold.fitaview.automaton;

import java.util.Collection;
import java.util.Map;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.automaton.transition.DuplicatedTransitionException;
import refhumbold.fitaview.automaton.transition.IllegalTransitionException;
import refhumbold.fitaview.automaton.transition.NoSuchTransitionException;
import refhumbold.fitaview.automaton.transition.TopDownTransitions;

public abstract class TopDownDeterministicAutomaton
    extends TopDownAutomaton
{
    protected TopDownTransitions<Pair<String, String>> transitions = new TopDownTransitions<>(
        this::keyToString, this::valueToString);

    public TopDownDeterministicAutomaton(Collection<Variable> variables,
                                         Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionAsStrings()
    {
        return this.transitions.convertToStringMap();
    }

    @Override
    public void addTransition(Variable var, String value, String label, String leftResult,
                              String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Pair.make(value, label), Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> applyTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        Pair<String, String> result = transitions.getMatched(var, Pair.make(value, label));

        return resolveWildcard(result, value);
    }
}