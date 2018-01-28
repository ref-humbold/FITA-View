package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.*;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedStateValueException;

public abstract class BottomUpAutomaton
    extends AbstractTreeAutomaton
{
    protected BottomUpTraversing traversing;
    private List<TreeNode> leaves = new ArrayList<>();

    public BottomUpAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public AutomatonDirection getDirection()
    {
        return AutomatonDirection.BOTTOM_UP;
    }

    @Override
    public BottomUpTraversing getTraversing()
    {
        return traversing;
    }

    @Override
    public void setTraversing(TraversingMode mode)
        throws IncorrectTraversingException
    {
        this.traversing = TraversingFactory.getBottomUpTraversing(mode);
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        return acceptanceConditions.check(tree.getState());
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        super.setTree(tree);
        this.findLeaves();
    }

    /**
     * Adding new transition entry to transition function of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label tree label of node
     * @param result variable value in node
     */
    public abstract void addTransition(Variable var, String leftValue, String rightValue,
                                       String label, String result)
        throws DuplicatedTransitionException, IllegalTransitionException;

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        super.initialize();

        traversing.initialize(leaves.toArray(new TreeNode[0]));
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is infinite.");
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedStateValueException,
               NoSuchTransitionException
    {
        Map<Variable, String> leftState;
        Map<Variable, String> rightState;

        if(node.hasChildren())
        {
            leftState = node.getLeft().getState();
            rightState = node.getRight().getState();
        }
        else
        {
            leftState = getInitialState();
            rightState = getInitialState();
        }

        node.setState(applyTransition(leftState, rightState, node.getLabel()));

        if(isSendingMessages)
            TransitionSender.getInstance()
                            .send(Triple.make(leftState, node.getState(), rightState));
    }

    /**
     * Applying transition function with specified arguments.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label label of node
     * @return variable value in node
     * @throws NoSuchTransitionException if no transition entry was found
     */
    protected abstract String applyTransition(Variable var, String leftValue, String rightValue,
                                              String label)
        throws NoSuchTransitionException;

    /**
     * Applying transition function on the whole state.
     * @param leftState state from left son
     * @param rightState state from right son
     * @param label label of node
     * @return state in the node
     * @throws NoSuchTransitionException if no transition entry was found
     */
    protected Map<Variable, String> applyTransition(Map<Variable, String> leftState,
                                                    Map<Variable, String> rightState, String label)
        throws NoSuchTransitionException
    {
        Map<Variable, String> result = new HashMap<>();

        for(Variable var : variables)
            result.put(var, applyTransition(var, leftState.get(var), rightState.get(var), label));

        return result;
    }

    /**
     * Resolving wildcard in result value of transition.
     * @param result transition result
     * @param leftValue value from left son
     * @param rightValue value from right son
     * @return resolved value
     */
    protected String resolveWildcard(String result, String leftValue, String rightValue)
    {
        if(Objects.equals(result, Wildcard.LEFT_VALUE))
            return leftValue;

        if(Objects.equals(result, Wildcard.RIGHT_VALUE))
            return rightValue;

        return result;
    }

    /**
     * Converting transition key to its string representation.
     * @param key transition key
     * @return string representation
     */
    protected String keyToString(Triple<String, String, String> key)
    {
        return "LEFT VALUE = \'" + key.getFirst() + "\', RIGHT VALUE = \'" + key.getSecond()
            + "\', LABEL = \'" + key.getThird() + "\'";
    }

    /**
     * Converting transition value to its string representation.
     * @param value transition value
     * @return string representation
     */
    protected String valueToString(String value)
    {
        return "VALUE = \'" + value + "\'";
    }

    private void findLeaves()
    {
        TopDownTraversing t = new TopDownDFS();

        leaves.clear();
        t.initialize(tree);

        t.forEachRemaining(iterator -> iterator.forEach(v -> {
            if(!v.hasChildren())
                leaves.add(v);
        }));
    }
}
