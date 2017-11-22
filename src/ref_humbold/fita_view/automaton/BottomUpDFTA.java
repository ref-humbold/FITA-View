package ref_humbold.fita_view.automaton;

import java.util.Map;
import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTA
    extends SimpleTreeAutomaton
{
    private Map<Triple<Variable, Pair<String, String>, Pair<String, String>>, String> transitions;

    public BottomUpDFTA(Set<String> alphabet, Variable... vars)
    {
        super(alphabet, vars);
    }

    @Override
    public void setTraversing(TraversingMode traversingMode)
    {
        this.traversing = TraversingFactory.getInstance()
                                           .getTraversing(traversingMode,
                                                          TraversingDirection.BOTTOM_UP);
    }

    @Override
    public void run()
    {
    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
    }

    @Override
    protected void initTree()
    {
    }

    void addTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                       String rightLabel, String result)
    {
        transitions.put(
            Triple.make(var, Pair.make(leftValue, leftLabel), Pair.make(rightValue, rightLabel)),
            result);
    }

    String doTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                        String rightLabel)
    {
        return transitions.get(
            Triple.make(var, Pair.make(leftValue, leftLabel), Pair.make(rightValue, rightLabel)));
    }
}
