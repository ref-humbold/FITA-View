package ref_humbold.fita_view.automaton;

import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;

public abstract class TopDownTreeAutomaton
    extends SimpleTreeAutomaton
{
    public TopDownTreeAutomaton(Set<String> alphabet, Variable... vars)
    {
        super(alphabet, vars);
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
    {
        for(Variable var : variables)
            tree.setState(var, var.getInitValue());
    }

    protected abstract void addTransition(Variable var, String value, String label,
                                          String leftResult, String rightResult);

    protected abstract Pair<String, String> doTransition(Variable var, String value, String label);
}
