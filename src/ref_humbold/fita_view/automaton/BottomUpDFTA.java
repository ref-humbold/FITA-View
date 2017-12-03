package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.Objects;

import ref_humbold.fita_view.Quadruple;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTA
    extends SimpleTreeAutomaton
{
    private Transitions<Quadruple<String, String, String, String>, String> transitions =
        new Transitions<>();

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
     * Getting a result of transition function for given arguments.
     * @param var variable
     * @param leftValue variable value in left son
     * @param leftLabel tree label of left son
     * @param rightValue variable value in right son
     * @param rightLabel tree label of right son
     * @return variable value in node
     */
    String doTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                        String rightLabel)
        throws NoSuchTransitionException
    {
        String result =
            transitions.get(var, Quadruple.make(leftValue, rightValue, leftLabel, rightLabel));

        if(result.equals(Transitions.LEFT_VALUE))
            return leftValue;

        if(result.equals(Transitions.RIGHT_VALUE))
            return rightValue;

        return result;
    }

    /**
     * Adding new transition entry to transition function of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param leftLabel tree label of left son
     * @param rightValue variable value in right son
     * @param rightLabel tree label of right son
     * @param result variable value in node
     */
    void addTransition(Variable var, String leftValue, String leftLabel, String rightValue,
                       String rightLabel, String result)
        throws DuplicatedTransitionException
    {
        if(transitions.containsKey(var,
                                   Quadruple.make(leftValue, rightValue, leftLabel, rightLabel)))
            throw new DuplicatedTransitionException();

        transitions.add(var, Quadruple.make(leftValue, rightValue, leftLabel, rightLabel), result);
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
