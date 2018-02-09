package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Map;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.transition.TopDownTransitions;

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
