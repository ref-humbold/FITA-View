package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class TopDownFiniteTreeAutomaton
    extends TopDownAutomaton
{
    private Set<Map<Variable, String>> acceptingStates = new HashSet<>();
    private List<Map<Variable, String>> leafStates = new ArrayList<>();

    public TopDownFiniteTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
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

        for(TreeNode node : traversing.next())
        {
            Map<Variable, String> lastState = new HashMap<>();

            for(Variable v : variables)
            {
                try
                {
                    Pair<String, String> result =
                        doTransition(v, node.getStateValue(v), node.getLabel());

                    if(node.hasChildren())
                    {
                        node.getLeft().setStateValue(v, result.getFirst());
                        node.getRight().setStateValue(v, result.getSecond());
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

    @Override
    protected void initialize()
        throws IllegalVariableValueException
    {
        super.initialize();
        leafStates.clear();
    }

    @Override
    protected boolean checkAcceptance(Map<Variable, String> state)
        throws UndefinedTreeStateException
    {
        for(Map<Variable, String> accept : acceptingStates)
        {
            boolean contained = true;

            for(Variable var : accept.keySet())
            {
                if(state.get(var) == null)
                    throw new UndefinedTreeStateException(
                        "Node has an undefined state variable value.");

                contained &= accept.get(var).equals(state.get(var)) || accept.get(var)
                                                                             .equals(
                                                                                 Wildcard.EVERY_VALUE);
            }

            if(contained)
                return true;
        }

        return false;
    }

    /**
     * Adding an accepting state of automaton.
     * @param accept mapping from variables to their accepting values
     */
    protected void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
    }
}
