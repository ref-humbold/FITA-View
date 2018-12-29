package refhumbold.fitaview.automaton;

import java.util.*;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.transition.DuplicatedTransitionException;
import refhumbold.fitaview.automaton.transition.IllegalTransitionException;
import refhumbold.fitaview.automaton.transition.NoSuchTransitionException;
import refhumbold.fitaview.automaton.traversing.TopDownTraversing;
import refhumbold.fitaview.automaton.traversing.TraversingFactory;
import refhumbold.fitaview.automaton.traversing.TraversingMode;
import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.tree.UndefinedStateValueException;

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
        if(isRunning())
            throw new AutomatonIsRunningException(
                "Cannot change traversing strategy when automaton is running");

        traversing = TraversingFactory.getTopDownTraversing(mode);
    }

    @Override
    public Boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, NoTreeException
    {
        for(Map<Variable, String> state : leafStates)
            if(!acceptanceConditions.check(state))
                return false;

        return true;
    }

    /**
     * Adding new transition entry to transition relation of automaton.
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
     * Calling a transition relation with specified arguments.
     * @param var variable
     * @param value variable value in node
     * @param label tree label of node
     * @return pair of variable values in sons (first left, second right)
     * @throws NoSuchTransitionException if no transition entry was found
     */
    protected abstract Pair<String, String> applyTransition(Variable var, String value,
                                                            String label)
        throws NoSuchTransitionException;

    /**
     * Calling a transition relation on the whole state with specified arguments.
     * @param state state of the automaton
     * @param label tree label of node
     * @return pair of states in sons (first left, second right)
     * @throws NoSuchTransitionException if no transition entry was found
     */
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
        throws IllegalVariableValueException, NoTreeException, NoTraversingStrategyException,
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

        traversing.addNewRecursive(node.getLeft());
        traversing.addNewRecursive(node.getRight());

        if(node.isLeaf())
        {
            leafStates.add(sonsStates.getFirst());
            leafStates.add(sonsStates.getSecond());
        }

        if(isSendingMessages)
        {
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.LEFT_SON, node.getLeft().getLabel(),
                                              sonsStates.getFirst()));
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.PARENT, node.getLabel(),
                                              node.getState()));
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.RIGHT_SON, node.getRight().getLabel(),
                                              sonsStates.getSecond()));
        }
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
