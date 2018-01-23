package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.*;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public class BottomUpDFTA
    extends AbstractTreeAutomaton
{
    private BottomUpTraversing traversing;
    private BottomUpTransitions transitions = new BottomUpTransitions();
    private List<TreeNode> leaves = new ArrayList<>();

    public BottomUpDFTA(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
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
    public Map<Pair<Variable, String>, String> getTransitionWithStrings()
    {
        return this.transitions.convertToStringMap(this::keyToString, this::valueToString);
    }

    @Override
    public String getTypeName()
    {
        return "Bottom-up deterministic finite tree automaton";
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        return acceptanceConditions.checkAcceptance(tree.getState());
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        super.setTree(tree);
        this.findLeaves();
    }

    @Override
    public TreeNode generateTree()
    {
        return null;
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
            && Objects.equals(this.acceptanceConditions, other.acceptanceConditions)
            && Objects.equals(this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "BottomUpDFTA:\n  alphabet = " + alphabet.toString() + "\n  variables = " + variables
            .toString() + "\n  transitions = " + transitions.toString();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(alphabet, variables, acceptanceConditions, traversing, transitions);
    }

    /**
     * Adding new transition entry to transition function of automaton.
     * @param var variable
     * @param leftValue variable value in left son
     * @param rightValue variable value in right son
     * @param label tree label of node
     * @param result variable value in node
     */
    public void addTransition(Variable var, String leftValue, String rightValue, String label,
                              String result)
        throws DuplicatedTransitionException, IllegalTransitionException
    {
        transitions.add(var, Triple.make(leftValue, rightValue, label), result);
    }

    @Override
    protected void changeRunningMode()
    {
        setRunningMode(
            traversing.hasNext() ? AutomatonRunningMode.RUNNING : AutomatonRunningMode.FINISHED);
    }

    @Override
    protected void processNode(TreeNode node)
        throws IllegalVariableValueException, UndefinedTreeStateException, NoSuchTransitionException
    {
        for(Variable var : variables)
        {
            String leftValue = node.getLeft() == null ? var.getInitValue()
                                                      : node.getLeft().getStateValue(var);
            String rightValue = node.getRight() == null ? var.getInitValue()
                                                        : node.getRight().getStateValue(var);
            String result = doTransition(var, leftValue, rightValue, node.getLabel());

            node.setStateValue(var, result);
        }
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

    String keyToString(Triple<String, String, String> key)
    {
        return "LEFT VALUE = \'" + key.getFirst() + "\', RIGHT VALUE = \'" + key.getSecond()
            + "\', LABEL = \'" + key.getThird() + "\'";
    }

    String valueToString(String value)
    {
        return "VALUE = \'" + value + "\'";
    }

    private String doTransition(Variable var, String leftValue, String rightValue, String label)
        throws NoSuchTransitionException
    {
        String result = transitions.get(var, Triple.make(leftValue, rightValue, label));

        if(Objects.equals(result, Wildcard.LEFT_VALUE))
            return leftValue;

        if(Objects.equals(result, Wildcard.RIGHT_VALUE))
            return rightValue;

        return result;
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
