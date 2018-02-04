package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedStateValueException;

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
    public AutomatonDirection getDirection()
    {
        return AutomatonDirection.TOP_DOWN;
    }

    @Override
    public TopDownTraversing getTraversing()
    {
        return traversing;
    }

    @Override
    public void setTraversing(TraversingMode mode)
        throws AutomatonIsRunningException
    {
        if(this.isRunning())
            throw new AutomatonIsRunningException(
                "Cannot change traversing strategy when automaton is running.");

        this.traversing = TraversingFactory.getTopDownTraversing(mode);
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        if(leafStates.isEmpty())
            throw new UndefinedStateValueException("States in tree leaves are undefined.");

        for(Map<Variable, String> state : leafStates)
            if(!acceptanceConditions.check(state))
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
    public abstract void addTransition(Variable var, String value, String label, String leftResult,
                                       String rightResult)
        throws DuplicatedTransitionException, IllegalTransitionException;

    /**
     * Calling a transition function with specified arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     */
    protected abstract Pair<String, String> applyTransition(Variable var, String value,
                                                            String label)
        throws NoSuchTransitionException;

    protected Pair<Map<Variable, String>, Map<Variable, String>> applyTransition(
        Map<Variable, String> state, String label)
        throws NoSuchTransitionException
    {
        Map<Variable, String> leftResult = new HashMap<>();
        Map<Variable, String> rightResult = new HashMap<>();

        for(Variable var : variables)
        {
            Pair<String, String> resultValue = applyTransition(var, state.get(var), label);

            leftResult.put(var, resultValue.getFirst());
            rightResult.put(var, resultValue.getSecond());
        }

        return Pair.make(leftResult, rightResult);
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        super.initialize();

        tree.setInitialState(variables);
        traversing.initialize(tree);
        leafStates.clear();
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedStateValueException,
               NoSuchTransitionException
    {
        Pair<Map<Variable, String>, Map<Variable, String>> sonsStates = applyTransition(
            node.getState(), node.getLabel());

        node.getLeft().setState(sonsStates.getFirst());
        node.getRight().setState(sonsStates.getSecond());

        if(node.isLeaf())
        {
            leafStates.add(sonsStates.getFirst());
            leafStates.add(sonsStates.getSecond());
        }

        if(isSendingMessages)
            TransitionSender.getInstance()
                            .send(Triple.make(
                                Pair.make(node.getLeft().getLabel(), sonsStates.getFirst()),
                                Pair.make(node.getLabel(), node.getState()),
                                Pair.make(node.getRight().getLabel(), sonsStates.getSecond())));
    }

    /**
     * Converting transition key to its string representation.
     * @param key transition key
     * @return string representation
     */
    protected String keyToString(Pair<String, String> key)
    {
        return "VALUE = \'" + key.getFirst() + "\', LABEL = \'" + key.getSecond() + "\'";
    }

    /**
     * Converting transition value to its string representation.
     * @param value transition value
     * @return string representation
     */
    protected String valueToString(Pair<String, String> value)
    {
        return "( LEFT VALUE = \'" + value.getFirst() + "\', RIGHT VALUE = " + value.getSecond()
            + "\' )";
    }

    /**
     * Resolving wildcard in result value of transition.
     * @param result transition result
     * @param nodeValue value from tree node
     * @return resolved value
     */
    protected Pair<String, String> resolveWildcard(Pair<String, String> result, String nodeValue)
    {
        if(Objects.equals(result.getFirst(), Wildcard.SAME_VALUE) && Objects.equals(
            result.getSecond(), Wildcard.SAME_VALUE))
            return Pair.make(nodeValue, nodeValue);

        if(Objects.equals(result.getFirst(), Wildcard.SAME_VALUE))
            return Pair.make(nodeValue, result.getSecond());

        if(Objects.equals(result.getSecond(), Wildcard.SAME_VALUE))
            return Pair.make(result.getFirst(), nodeValue);

        return result;
    }
}
