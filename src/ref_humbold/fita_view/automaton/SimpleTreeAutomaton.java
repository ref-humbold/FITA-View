package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeVertex;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class SimpleTreeAutomaton
    implements TreeAutomaton
{
    protected Set<String> alphabet;
    protected List<Variable> variables;
    protected TreeVertex tree;
    protected Set<Map<Variable, String>> acceptingStates = new HashSet<>();
    protected boolean isRunning = false;

    public SimpleTreeAutomaton(Collection<String> alphabet, Collection<Variable> variables)
    {
        this.alphabet = new HashSet<>(alphabet);
        this.variables = new ArrayList<>(variables);
    }

    /**
     * @return current traversing strategy of the automaton
     */
    protected abstract TreeTraversing getTraversing();

    @Override
    public void setTree(TreeVertex tree)
    {
        this.tree = tree;
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return alphabet.contains(label);
    }

    @Override
    public void run()
        throws IllegalVariableValueException, NoSuchTransitionException, NoTraversingException,
               UndefinedTreeStateException
    {
        if(getTraversing() == null)
            throw new NoTraversingException("Automaton has no traversing strategy.");

        initialize();

        try
        {
            while(getTraversing().hasNext())
                makeStepForward();
        }
        finally
        {
            isRunning = false;
        }
    }

    @Override
    public int hashCode()
    {
        return alphabet.hashCode() * 37 + variables.hashCode();
    }

    /**
     * Testing if specified tree node state is an accepting state.
     * @param state state of a tree node
     * @return {@code true} if state can be accepted, otherwise {@code false}
     */
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
     * Initializing automaton and tree before running on tree.
     */
    protected void initialize()
        throws IllegalVariableValueException
    {
        TopDownTraversing t =
            TraversingFactory.getInstance().getTopDownTraversing(TraversingFactory.Mode.DFS);

        t.initialize(tree);

        while(t.hasNext())
            for(TreeVertex v : t.next())
                v.deleteFullState();

        isRunning = true;
    }

    /**
     * Adding an accepting state of automaton
     * @param accept mapping from variables to their accepting values
     */
    protected void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
    }
}
