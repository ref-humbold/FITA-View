package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ref_humbold.fita_view.tree.TreeVertex;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class FiniteTreeAutomaton
    extends SimpleTreeAutomaton
{
    protected Set<Map<Variable, String>> acceptingStates = new HashSet<>();

    public FiniteTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public void setTree(TreeVertex tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Specified tree is infinite.");

        super.setTree(tree);
    }

    /**
     * Testing if specified tree node state is an accepting state.
     * @param state state of a tree node
     * @return {@code true} if state is accepted, otherwise {@code false}
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
     * Adding an accepting state of automaton.
     * @param accept mapping from variables to their accepting values
     */
    protected void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
    }
}
