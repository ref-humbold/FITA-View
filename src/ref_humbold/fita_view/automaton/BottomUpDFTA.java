package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTA
    extends SimpleTreeAutomaton
{
    private Map<Triple<Variable, Pair<String, String>, Pair<String, String>>, String> transitions =
        new HashMap<>();

    public BottomUpDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
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

    /**
     * Adding new transition to transition function of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param leftLabel tree label of left son
     * @param rightValue variable value in right son
     * @param rightLabel tree label of right son
     * @param result variable value in node
     */
    void addTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                       String rightLabel, String result)
    {
        transitions.put(
            Triple.make(var, Pair.make(leftValue, leftLabel), Pair.make(rightValue, rightLabel)),
            result);
    }

    /**
     * Calling a transition function with given arguments.
     * @param var variable
     * @param leftValue variable value in left son
     * @param leftLabel tree label of left son
     * @param rightValue variable value in right son
     * @param rightLabel tree label of right son
     * @return variable value in node
     */
    String doTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                        String rightLabel)
    {
        return transitions.get(
            Triple.make(var, Pair.make(leftValue, leftLabel), Pair.make(rightValue, rightLabel)));
    }

    @Override
    public String toString()
    {
        return "BottomUpDFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof BottomUpDFTA))
            return false;

        BottomUpDFTA other = (BottomUpDFTA)o;

        return Objects.equals(this.alphabet, other.alphabet) && Objects.equals(this.variables,
                                                                               other.variables)
            && Objects.equals(this.transitions, other.transitions);
    }
}
