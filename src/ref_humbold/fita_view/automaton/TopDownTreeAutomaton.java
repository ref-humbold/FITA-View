package ref_humbold.fita_view.automaton;

import java.util.Collection;

import ref_humbold.fita_view.Pair;
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
    {
        this.traversing = TraversingFactory.getInstance()
                                           .getTraversing(traversingMode,
                                                          TraversingDirection.TOP_DOWN);
    }

    @Override
    public void makeStepForward()
    {
        traversing.moveForward();
    }

    @Override
    public void makeStepBackward()
    {
        traversing.moveBackward();
    }

    @Override
    protected void initTree()
        throws IllegalVariableValueException
    {
        for(Variable var : variables)
            tree.setState(var, var.getInitValue());
    }

    /**
     * Adding new transition to transition function of automaton.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @param leftResult variable value in left son
     * @param rightResult variable value in right son
     */
    protected abstract void addTransition(Variable var, String value, String label,
                                          String leftResult, String rightResult);

    /**
     * Calling a transition function with given arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected abstract Pair<String, String> doTransition(Variable var, String value, String label);

    protected Pair<String, String> removeWildcard(String value, Pair<String, String> trans)
    {
        if(trans.getFirst().equals(Transitions.SAME_VALUE) && trans.getSecond()
                                                                   .equals(
                                                                       (Transitions.SAME_VALUE)))
            return Pair.make(value, value);

        if(trans.getFirst().equals(Transitions.SAME_VALUE))
            return Pair.make(value, trans.getSecond());

        if(trans.getSecond().equals(Transitions.SAME_VALUE))
            return Pair.make(trans.getFirst(), value);

        return trans;
    }
}
