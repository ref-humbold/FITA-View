package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.BottomUpTransitions;
import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.BottomUpTraversing;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public class BottomUpDFTA
    extends AbstractTreeAutomaton
{
    private Set<Map<Variable, String>> acceptingStates = new HashSet<>();
    private BottomUpTraversing traversing;
    private BottomUpTransitions transitions = new BottomUpTransitions();
    private List<TreeNode> leaves = new ArrayList<>();

    public BottomUpDFTA(Collection<String> alphabet, Collection<Variable> variables)
    {
        super(variables, alphabet);
    }

    @Override
    protected BottomUpTraversing getTraversing()
    {
        return traversing;
    }

    @Override
    public void setTraversing(TraversingFactory.Mode mode)
        throws IncorrectTraversingException
    {
        this.traversing = TraversingFactory.getInstance().getBottomUpTraversing(mode);
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException
    {
        if(acceptingStates.isEmpty())
            throw new UndefinedAcceptanceException("Automaton has no accepting states defined.");

        return checkAcceptance(tree.getState());
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(containsRecursiveNode(tree))
            throw new TreeFinitenessException("Specified tree is infinite.");

        super.setTree(tree);
        this.findLeaves();
        this.isRunning = false;
    }

    @Override
    public void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException, NoTraversingException,
               UndefinedTreeStateException
    {
        if(traversing == null)
        {
            isRunning = false;
            throw new NoTraversingException("Automaton has no traversing strategy.");
        }

        if(!isRunning)
            initialize();

        if(!traversing.hasNext())
        {
            isRunning = false;
            return;
        }

        for(TreeNode node : traversing.next())
            for(Variable var : variables)
                try
                {
                    String leftValue = node.getLeft() == null ? var.getInitValue()
                                                              : node.getLeft().getStateValue(var);
                    String rightValue = node.getRight() == null ? var.getInitValue()
                                                                : node.getRight()
                                                                      .getStateValue(var);
                    String result = doTransition(var, leftValue, rightValue, node.getLabel());

                    node.setStateValue(var, result);
                }
                catch(Exception e)
                {
                    isRunning = false;
                    throw e;
                }
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
            && Objects.equals(this.acceptingStates, other.acceptingStates) && Objects.equals(
            this.transitions, other.transitions);
    }

    @Override
    public String toString()
    {
        return "BottomUpDFTA of " + alphabet.toString() + " & " + variables.toString() + " & "
            + transitions.toString();
    }

    @Override
    protected void initialize()
        throws IllegalVariableValueException
    {
        super.initialize();

        traversing.initialize(leaves.toArray(new TreeNode[0]));
    }

    @Override
    protected boolean checkAcceptance(Map<Variable, String> state)
        throws UndefinedTreeStateException
    {
        for(Map<Variable, String> accept : acceptingStates)
        {
            boolean contained = true;

            for(Variable var : accept.keySet())
            {
                if(state.get(var) == null)
                    throw new UndefinedTreeStateException(
                        "Node has an undefined state variable value.");

                contained &= accept.get(var).equals(state.get(var)) || accept.get(var)
                                                                             .equals(
                                                                                 Wildcard.EVERY_VALUE);
            }

            if(contained)
                return true;
        }

        return false;
    }

    /**
     * Adding an accepting state of automaton.
     * @param accept mapping from variables to their accepting values
     */
    protected void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
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

    private String doTransition(Variable var, String leftValue, String rightValue, String label)
        throws NoSuchTransitionException
    {
        String result = transitions.get(var, Triple.make(leftValue, rightValue, label));

        if(result.equals(Wildcard.LEFT_VALUE))
            return leftValue;

        if(result.equals(Wildcard.RIGHT_VALUE))
            return rightValue;

        return result;
    }

    private void findLeaves()
    {
        leaves.clear();

        TopDownTraversing t =
            TraversingFactory.getInstance().getTopDownTraversing(TraversingFactory.Mode.DFS);

        t.initialize(tree);

        while(t.hasNext())
            for(TreeNode v : t.next())
                if(!v.hasChildren())
                    leaves.add(v);
    }
}
