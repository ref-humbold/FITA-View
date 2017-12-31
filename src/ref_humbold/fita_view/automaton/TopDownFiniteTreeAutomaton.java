package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class TopDownFiniteTreeAutomaton
    extends TopDownAutomaton
{
    List<Map<Variable, String>> leafStates = new ArrayList<>();
    private Set<Map<Variable, String>> acceptingStates = new HashSet<>();

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
    protected void processNodes(Iterable<TreeNode> nextNodes)
        throws IllegalVariableValueException, UndefinedTreeStateException, NoSuchTransitionException
    {
        for(TreeNode node : nextNodes)
        {
            Map<Variable, String> leafLeftState = new HashMap<>();
            Map<Variable, String> leafRightState = new HashMap<>();

            for(Variable v : variables)
                try
                {
                    Pair<String, String> result = doTransition(v, node.getStateValue(v),
                                                               node.getLabel());

                    if(node.hasChildren())
                    {
                        node.getLeft().setStateValue(v, result.getFirst());
                        node.getRight().setStateValue(v, result.getSecond());
                    }
                    else
                    {
                        leafLeftState.put(v, result.getFirst());
                        leafRightState.put(v, result.getSecond());
                    }
                }
                catch(Exception e)
                {
                    isRunning = false;
                    throw e;
                }

            if(!leafLeftState.isEmpty())
                leafStates.add(leafLeftState);

            if(!leafRightState.isEmpty())
                leafStates.add(leafRightState);
        }
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException
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
