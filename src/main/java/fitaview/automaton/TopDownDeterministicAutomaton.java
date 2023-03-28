package fitaview.automaton;

import java.util.Collection;
import java.util.Map;

import fitaview.automaton.transition.DuplicatedTransitionException;
import fitaview.automaton.transition.IllegalTransitionException;
import fitaview.automaton.transition.NoSuchTransitionException;
import fitaview.automaton.transition.TopDownTransitions;
import fitaview.utils.Pair;

public abstract class TopDownDeterministicAutomaton
        extends TopDownAutomaton
{
    protected final TopDownTransitions<Pair<String, String>> transitions =
            new TopDownTransitions<>(this::keyToString, this::valueToString);

    public TopDownDeterministicAutomaton(Collection<Variable> variables,
                                         Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public Map<Pair<Variable, String>, String> getTransitionAsStrings()
    {
        return transitions.convertToStringMap();
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
