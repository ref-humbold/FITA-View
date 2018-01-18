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
    protected AutomatonRunningMode runningMode = AutomatonRunningMode.STOPPED;
    protected boolean isSendingMessages = false;

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
    public AutomatonRunningMode getRunningMode()
    {
        return this.runningMode;
    }

    protected void setRunningMode(AutomatonRunningMode runningMode)
    {
        this.runningMode = runningMode;

        if(isSendingMessages)
            AutomatonRunningModeSender.getInstance().send(runningMode);
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        assertFiniteness(tree);

        this.tree = tree;
        this.setRunningMode(AutomatonRunningMode.STOPPED);
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
        throws IllegalVariableValueException, NoSuchTransitionException,
               NoTraversingStrategyException, UndefinedTreeStateException, EmptyTreeException,
               NoNonDeterministicStrategyException
    {
        if(getTraversing() == null)
        {
            stopTraversing();
            throw new NoTraversingStrategyException("Automaton has no traversing strategy.");
        }

        if(runningMode == AutomatonRunningMode.STOPPED
            || runningMode == AutomatonRunningMode.FINISHED)
            initialize();

        while(getTraversing().hasNext())
            makeStepForward();
    }

    @Override
    public void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException,
               NoTraversingStrategyException, UndefinedTreeStateException, EmptyTreeException,
               NoNonDeterministicStrategyException
    {
        if(getTraversing() == null)
        {
            stopTraversing();
            throw new NoTraversingStrategyException("Automaton has no traversing strategy.");
        }

        if(runningMode == AutomatonRunningMode.STOPPED)
            initialize();

        Iterable<TreeNode> nextNodes;

        try
        {
            nextNodes = getTraversing().next();
            processNodes(nextNodes);
        }
        catch(Exception e)
        {
            stopTraversing();
            throw e;
        }

        if(isSendingMessages)
            AutomatonCurrentNodesSender.getInstance().send(nextNodes);

        changeRunningMode();
    }

    @Override
    public void stopTraversing()
    {
        setRunningMode(AutomatonRunningMode.STOPPED);

        if(getTraversing() != null)
            getTraversing().clear();

        if(tree != null)
            deleteTreeStates();

        if(isSendingMessages)
            AutomatonCurrentNodesSender.getInstance().send(Collections::emptyIterator);
    }

    @Override
    public int hashCode()
    {
        return alphabet.hashCode() * 37 + variables.hashCode();
    }

    protected abstract void changeRunningMode();

    /**
     * Verifying finiteness of the tree.
     * @param tree tree to verify
     * @throws TreeFinitenessException if tree finiteness is violated
     */
    protected abstract void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException;

    /**
     * Initializing automaton and tree before running on tree.
     */
    protected void initialize()
        throws IllegalVariableValueException, EmptyTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        if(getTraversing() == null)
            throw new NoTraversingStrategyException("Automaton has no traversing strategy.");

        if(tree == null)
            throw new EmptyTreeException("No tree specified.");

        deleteTreeStates();
        setRunningMode(AutomatonRunningMode.RUNNING);
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
     * Testing if specified state can be accepted by the automaton.
     * @param state state from a tree node
     * @return {@code true} if state is accepted, otherwise {@code false}
     * @throws UndefinedTreeStateException if state contains a variable with undefined value
     */
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

                String[] splitValue = accept.get(var).split(" ");

                if(Objects.equals(splitValue[0], "+"))
                    contained &= Objects.equals(splitValue[1], state.get(var)) || Objects.equals(
                        splitValue[1], Wildcard.EVERY_VALUE);
                else if(Objects.equals(splitValue[0], "-"))
                    contained &= !Objects.equals(splitValue[1], state.get(var));
                else
                    throw new IllegalStateException("Neither inclusion or exclusion specified.");
            }

            if(contained)
                return true;
        }

        return false;
    }

    /**
     * Processing tree nodes in each step.
     * @param nextNodes nodes to process
     */
    protected abstract void processNodes(Iterable<TreeNode> nextNodes)
        throws NoSuchTransitionException, IllegalVariableValueException,
               UndefinedTreeStateException;

    private void deleteTreeStates()
    {
        TopDownTraversing t = new TopDownDFS();

        t.initialize(tree);

        while(t.hasNext())
            for(TreeNode v : t.next())
                v.deleteState();
    }
}
