package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class TopDownAutomaton
    extends AbstractTreeAutomaton
{
    protected TopDownTraversing traversing;
    List<Map<Variable, String>> leafStates = new ArrayList<>();

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
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException
    {
        if(acceptingStates.isEmpty())
            throw new UndefinedAcceptanceException("Automaton has no accepting states defined.");

        if(leafStates.isEmpty())
            throw new UndefinedTreeStateException("States in tree leaves are undefined.");

        for(Map<Variable, String> state : leafStates)
            if(!checkAcceptance(state))
                return false;

        return true;
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

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        super.initialize();

        for(Variable var : variables)
            tree.setStateInitValue(var);

        traversing.initialize(tree);
        leafStates.clear();
    }

    @Override
    protected void processNodes(Iterable<TreeNode> nextNodes)
        throws IllegalVariableValueException, UndefinedTreeStateException, NoSuchTransitionException
    {
        for(TreeNode node : nextNodes)
        {
            Map<Variable, String> leafLeftState = new HashMap<>();
            Map<Variable, String> leafRightState = new HashMap<>();

            for(Variable v : variables)
            {
                Pair<String, String> result = doTransition(v, node.getStateValue(v),
                                                           node.getLabel());

                if(node.hasChildren())
                {
                    node.getLeft().setStateValue(v, result.getFirst());
                    node.getRight().setStateValue(v, result.getSecond());
                }
                else
                {
                    leafLeftState.put(v, result.getFirst());
                    leafRightState.put(v, result.getSecond());
                }
            }

            if(!leafLeftState.isEmpty())
                leafStates.add(leafLeftState);

            if(!leafRightState.isEmpty())
                leafStates.add(leafRightState);
        }
    }

    private Pair<String, String> doTransition(Variable var, String value, String label)
        throws NoSuchTransitionException
    {
        Pair<String, String> result = getTransitionResult(var, value, label);

        return removeSameWildcard(value, result);
    }

    private Pair<String, String> removeSameWildcard(String value, Pair<String, String> trans)
    {
        if(Objects.equals(trans.getFirst(), Wildcard.SAME_VALUE) && Objects.equals(
            trans.getSecond(), Wildcard.SAME_VALUE))
            return Pair.make(value, value);

        if(Objects.equals(trans.getFirst(), Wildcard.SAME_VALUE))
            return Pair.make(value, trans.getSecond());

        if(Objects.equals(trans.getSecond(), Wildcard.SAME_VALUE))
            return Pair.make(trans.getFirst(), value);

        return trans;
    }
}
