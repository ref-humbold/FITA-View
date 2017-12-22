package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.tree.TreeVertex;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class TopDownTreeAutomaton
    extends SimpleTreeAutomaton
{
    private TopDownTraversing traversing;
    private List<Map<Variable, String>> leafStates = new ArrayList<>();

    public TopDownTreeAutomaton(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    protected TopDownTraversing getTraversing()
    {
        return traversing;
    }

    @Override
    public void setTraversing(TraversingFactory.Mode mode)
    {
        this.traversing = TraversingFactory.getInstance().getTopDownTraversing(mode);
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException
    {
        if(acceptingStates.isEmpty())
            throw new UndefinedAcceptanceException("Automaton has no accepting states defined.");

        if(leafStates.isEmpty())
            throw new UndefinedTreeStateException("States in tree leaves are undefined.");

        for(Map<Variable, String> state : leafStates)
            if(!checkAcceptance(state))
                return false;

        return true;
    }

    @Override
    public void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException, NoTraversingException,
               UndefinedTreeStateException
    {
        if(traversing == null)
        {
            isRunning = false;
            throw new NoTraversingException("Automaton has no traversing strategy.");
        }

        if(!isRunning)
            initialize();

        if(!traversing.hasNext())
        {
            isRunning = false;
            return;
        }

        for(TreeVertex vertex : traversing.next())
        {
            Map<Variable, String> lastState = new HashMap<>();

            for(Variable v : variables)
            {
                try
                {
                    Pair<String, String> result =
                        doTransition(v, vertex.getState(v), vertex.getLabel());

                    if(vertex.hasChildren())
                    {
                        vertex.getLeft().setState(v, result.getFirst());
                        vertex.getRight().setState(v, result.getSecond());
                    }
                    else
                    {
                        lastState.put(v, result.getFirst());
                        lastState.put(v, result.getSecond());
                    }
                }
                catch(Exception e)
                {
                    isRunning = false;
                    throw e;
                }

                if(!lastState.isEmpty())
                    leafStates.add(lastState);
            }
        }
    }

    /**
     * Adding new transition entry to transition function of automaton.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @param leftResult variable value in left son
     * @param rightResult variable value in right son
     */
    protected abstract void addTransition(Variable var, String value, String label,
                                          String leftResult, String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException;

    /**
     * Calling a transition function with specified arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected abstract Pair<String, String> getTransitionResult(Variable var, String value,
                                                                String label)
        throws NoSuchTransitionException;

    @Override
    protected void initialize()
        throws IllegalVariableValueException
    {
        super.initialize();

        for(Variable var : variables)
            tree.setState(var, var.getInitValue());

        traversing.initialize(tree);
        leafStates.clear();
    }

    private Pair<String, String> doTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        Pair<String, String> result = getTransitionResult(var, value, label);

        return removeSameWildcard(value, result);
    }

    private Pair<String, String> removeSameWildcard(String value, Pair<String, String> trans)
    {
        if(trans.getFirst().equals(Wildcard.SAME_VALUE) && trans.getSecond()
                                                                .equals((Wildcard.SAME_VALUE)))
            return Pair.make(value, value);

        if(trans.getFirst().equals(Wildcard.SAME_VALUE))
            return Pair.make(value, trans.getSecond());

        if(trans.getSecond().equals(Wildcard.SAME_VALUE))
            return Pair.make(trans.getFirst(), value);

        return trans;
    }
}
