package refhumbold.fitaview.automaton;

import java.util.*;

import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.transition.DuplicatedTransitionException;
import refhumbold.fitaview.automaton.transition.IllegalTransitionException;
import refhumbold.fitaview.automaton.transition.NoSuchTransitionException;
import refhumbold.fitaview.automaton.traversing.*;
import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.tree.UndefinedStateValueException;

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
        throws IncorrectTraversingException, AutomatonIsRunningException
    {
        if(this.isRunning())
            throw new AutomatonIsRunningException(
                "Cannot change traversing strategy when automaton is running");

        this.traversing = TraversingFactory.getBottomUpTraversing(mode);
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException
    {
        super.setTree(tree);

        if(tree != null)
            this.findLeaves();
    }

    @Override
    public Boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedStateValueException, NoTreeException
    {
        if(tree == null)
            throw new NoTreeException("No tree specified");

        return acceptanceConditions.check(tree.getState());
    }

    /**
     * Adding new transition entry to transition relation of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label tree label of node
     * @param result variable value in node
     */
    public abstract void addTransition(Variable var, String leftValue, String rightValue,
                                       String label, String result)
        throws DuplicatedTransitionException, IllegalTransitionException;

    /**
     * Testing if the automaton can accept at least one tree.
     * @return {@code true} if the automaton cannot accept any tree, otherwise {@code false}
     */
    public abstract boolean checkEmptiness()
        throws UndefinedAcceptanceException, UndefinedStateValueException;

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException, NoTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        super.initialize();

        traversing.initialize(leaves.toArray(new TreeNode[0]));
        leaves.forEach(leaf -> {
            leaf.getLeft().setInitialState(variables);
            leaf.getRight().setInitialState(variables);
        });
    }

    @Override
    protected void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Tree is infinite");
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedStateValueException,
               NoSuchTransitionException
    {
        node.setState(applyTransition(node.getLeft().getState(), node.getRight().getState(),
                                      node.getLabel()));

        if(isSendingMessages)
        {
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.LEFT_SON, node.getLeft().getLabel(),
                                              node.getLeft().getState()));
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.PARENT, node.getLabel(),
                                              node.getState()));
            TransitionSender.getInstance()
                            .send(Triple.make(NodeInfoSource.RIGHT_SON, node.getRight().getLabel(),
                                              node.getRight().getState()));
        }
    }

    /**
     * Applying transition relation with specified arguments.
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
     * Applying transition relation on the whole state.
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
            if(v.isLeaf())
                leaves.add(v);
        }));
    }
}
