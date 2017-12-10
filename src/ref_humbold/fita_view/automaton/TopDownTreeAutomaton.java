package ref_humbold.fita_view.automaton;

import java.util.Collection;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;

public abstract class TopDownTreeAutomaton
    extends SimpleTreeAutomaton
{
    public TopDownTreeAutomaton(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    public void setTraversing(TraversingMode traversingMode)
        throws IncorrectTraversingException
    {
        this.traversing = TraversingFactory.getInstance()
                                           .getTraversing(traversingMode,
                                                          TraversingDirection.TOP_DOWN);
    }

    @Override
    protected void initializeTree()
        throws IllegalVariableValueException
    {
        for(Variable var : variables)
            tree.setState(var, var.getInitValue());
    }

    /**
     * Getting a result of transition function for given arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected final Pair<String, String> doTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        Pair<String, String> result = getTransition(var, value, label);

        return removeWildcard(value, result);
    }

    /**
     * Calling a transition function with given arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected abstract Pair<String, String> getTransition(Variable var, String value, String label)
        throws NoSuchTransitionException;

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
        throws DuplicatedTransitionException;

    private Pair<String, String> removeWildcard(String value, Pair<String, String> trans)
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
