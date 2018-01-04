package ref_humbold.fita_view.automaton;

import java.util.*;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownDFS;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.tree.NodeType;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public abstract class AbstractTreeAutomaton
    implements TreeAutomaton
{
    protected TreeNode tree;
    protected Set<String> alphabet;
    protected List<Variable> variables;
    protected Set<Map<Variable, String>> acceptingStates = new HashSet<>();
    protected boolean isRunning = false;
    private boolean isSendingMessages = false;

    public AbstractTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        this.variables = new ArrayList<>(variables);
        this.alphabet = new HashSet<>(alphabet);
    }

    @Override
    public Set<String> getAlphabet()
    {
        return this.alphabet;
    }

    @Override
    public List<Variable> getVariables()
    {
        return this.variables;
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        this.tree = tree;
    }

    @Override
    public void setSendingMessages(boolean sendingMessages)
    {
        this.isSendingMessages = sendingMessages;
    }

    @Override
    public void addAcceptingState(Map<Variable, String> accept)
    {
        acceptingStates.add(accept);
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return alphabet.contains(label);
    }

    @Override
    public void run()
        throws IllegalVariableValueException, NoSuchTransitionException, NoTraversingException,
               UndefinedTreeStateException, EmptyTreeException
    {
        if(getTraversing() == null)
        {
            isRunning = false;
            throw new NoTraversingException("Automaton has no traversing strategy.");
        }

        if(!isRunning)
            initialize();

        try
        {
            while(getTraversing().hasNext())
                makeStepForward();
        }
        finally
        {
            isRunning = false;
        }
    }

    @Override
    public void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException, NoTraversingException,
               UndefinedTreeStateException, EmptyTreeException
    {
        if(!isRunning)
            initialize();

        Iterable<TreeNode> nextNodes = getTraversing().next();

        processNodes(nextNodes);

        if(isSendingMessages)
            AutomatonRunningSender.getInstance().send(nextNodes);

        isRunning = getTraversing().hasNext();
    }

    @Override
    public int hashCode()
    {
        return alphabet.hashCode() * 37 + variables.hashCode();
    }

    /**
     * Initializing automaton and tree before running on tree.
     */
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingException
    {
        if(getTraversing() == null)
            throw new NoTraversingException("Automaton has no traversing strategy.");

        if(tree == null)
            throw new EmptyTreeException("No tree specified.");

        TopDownTraversing t = new TopDownDFS();

        t.initialize(tree);

        while(t.hasNext())
            for(TreeNode v : t.next())
                v.deleteState();

        isRunning = true;
    }

    /**
     * Testing if specified tree contains a recursive node.
     * @param node tree node
     * @return {@code true} if tree has a recursive node, otherwise {@code false}
     */
    protected boolean containsRecursiveNode(TreeNode node)
    {
        return node != null && (node.getType() == NodeType.REC || containsRecursiveNode(
            node.getLeft()) || containsRecursiveNode(node.getRight()));
    }

    /**
     * Testing if specified tree node state is an accepting state.
     * @param state state of a tree node
     * @return {@code true} if state is accepted, otherwise {@code false}
     */
    protected abstract boolean checkAcceptance(Map<Variable, String> state)
        throws UndefinedTreeStateException;

    /**
     * Processing tree nodes in each step.
     * @param nextNodes nodes to process
     */
    protected abstract void processNodes(Iterable<TreeNode> nextNodes)
        throws NoSuchTransitionException, IllegalVariableValueException,
               UndefinedTreeStateException;
}
