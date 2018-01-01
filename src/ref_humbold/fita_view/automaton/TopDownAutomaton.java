package ref_humbold.fita_view.automaton;

import java.util.Collection;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeNode;

public abstract class TopDownAutomaton
    extends AbstractTreeAutomaton
{
    protected TopDownTraversing traversing;

    public TopDownAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public TopDownTraversing getTraversing()
    {
        return traversing;
    }

    @Override
    public void setTraversing(TraversingMode mode)
    {
        this.traversing = TraversingFactory.getTopDownTraversing(mode);
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Specified tree is infinite.");

        super.setTree(tree);
    }

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
        throws DuplicatedTransitionException, IllegalTransitionException;

    /**
     * Calling a transition function with specified arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected abstract Pair<String, String> getTransitionResult(Variable var, String value,
                                                                String label)
        throws NoSuchTransitionException;

    protected final Pair<String, String> doTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        Pair<String, String> result = getTransitionResult(var, value, label);

        return removeSameWildcard(value, result);
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingException
    {
        super.initialize();

        for(Variable var : variables)
            tree.setStateValue(var, var.getInitValue());

        traversing.initialize(tree);
    }

    private Pair<String, String> removeSameWildcard(String value, Pair<String, String> trans)
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
