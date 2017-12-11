package ref_humbold.fita_view.automaton;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingDirection;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTA
    extends SimpleTreeAutomaton
{
    private BottomUpTransitions transitions = new BottomUpTransitions();
    private Set<Map<Variable, String>> acceptingStates = new HashSet<>();

    public BottomUpDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(alphabet, variables);
    }

    @Override
    public void setTraversing(TraversingMode traversingMode)
        throws IncorrectTraversingException
    {
        this.traversing = TraversingFactory.getInstance()
                                           .getTraversing(traversingMode,
                                                          TraversingDirection.BOTTOM_UP);
    }

    @Override
    public boolean isAccepted()
    {
        return acceptingStates.contains(tree.getFullState());
    }

    @Override
    public void run()
    {
    }

    @Override
    public void makeStepForward()
    {

    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
    }

    @Override
    protected void initializeTree()
    {
    }

    void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
    }

    /**
     * Getting a result of transition function for given arguments.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label tree label of node
     * @return variable value in node
     */
    String doTransition(Variable var, String leftValue, String rightValue, String label)
        throws NoSuchTransitionException
    {
        String result = transitions.get(var, Triple.make(leftValue, rightValue, label));

        if(result.equals(Wildcard.LEFT_VALUE))
            return leftValue;

        if(result.equals(Wildcard.RIGHT_VALUE))
            return rightValue;

        return result;
    }

    /**
     * Adding new transition entry to transition function of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label tree label of node
     * @param result variable value in node
     */
    void addTransition(Variable var, String leftValue, String rightValue, String label,
                       String result)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Triple.make(leftValue, rightValue, label), result);
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
            && Objects.equals(this.acceptingStates, other.acceptingStates) && Objects.equals(
            this.transitions, other.transitions);
    }
}
