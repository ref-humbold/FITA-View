package fitaview.automaton;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import fitaview.FITAViewException;
import fitaview.Pair;
import fitaview.automaton.transition.NoSuchTransitionException;
import fitaview.automaton.traversing.TopDownDFS;
import fitaview.automaton.traversing.TopDownTraversing;
import fitaview.tree.NodeType;
import fitaview.tree.TreeNode;
import fitaview.tree.UndefinedStateValueException;

public abstract class AbstractTreeAutomaton
    implements TreeAutomaton
{
    protected TreeNode tree;
    protected Set<String> alphabet;
    protected List<Variable> variables;
    protected AcceptanceConditions acceptanceConditions = new AcceptanceConditions();
    protected AutomatonRunningMode runningMode = AutomatonRunningMode.STOPPED;
    protected boolean isSendingMessages = false;

    public AbstractTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        this.variables = new ArrayList<>(variables);
        this.alphabet = new HashSet<>(alphabet);
    }

    @Override
    public AcceptanceConditions getAcceptanceConditions()
    {
        return this.acceptanceConditions;
    }

    @Override
    public Collection<String> getAlphabet()
    {
        return this.alphabet;
    }

    @Override
    public Collection<Variable> getVariables()
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
            AutomatonRunningModeSender.getInstance().send();
    }

    /** @return initial state of tree in the automaton */
    protected Map<Variable, String> getInitialState()
    {
        return variables.stream()
                        .collect(Collectors.toMap(Function.identity(), Variable::getInitValue,
                                                  (a, b) -> b));
    }

    @Override
    public void setTree(TreeNode tree)
        throws TreeFinitenessException
    {
        if(tree != null)
            assertFiniteness(tree);

        this.tree = tree;
        stopTraversing();
    }

    @Override
    public void setSendingMessages(boolean isSendingMessages)
    {
        this.isSendingMessages = isSendingMessages;
    }

    @Override
    public void addAcceptanceConditions(Map<Variable, Pair<String, Boolean>> accept)
    {
        acceptanceConditions.add(accept);
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return alphabet.contains(label);
    }

    @Override
    public void run()
        throws IllegalVariableValueException, NoSuchTransitionException,
               NoTraversingStrategyException, UndefinedStateValueException, NoTreeException,
               NoNonDeterministicStrategyException
    {
        if(getTraversing() == null)
        {
            stopTraversing();
            throw new NoTraversingStrategyException("Automaton has no traversing strategy");
        }

        if(!isRunning())
            initialize();

        while(getTraversing().hasNext())
            makeStepForward();
    }

    @Override
    public void makeStepForward()
        throws NoSuchTransitionException, IllegalVariableValueException,
               NoTraversingStrategyException, UndefinedStateValueException, NoTreeException,
               NoNonDeterministicStrategyException
    {
        if(getTraversing() == null)
        {
            stopTraversing();
            throw new NoTraversingStrategyException("Automaton has no traversing strategy");
        }

        if(runningMode == AutomatonRunningMode.STOPPED)
            initialize();

        Iterable<TreeNode> nextNodes = getTraversing().next();

        if(isSendingMessages)
            AutomatonCurrentNodesSender.getInstance().send(nextNodes);

        try
        {
            for(TreeNode node : nextNodes)
                processNode(node);
        }
        catch(FITAViewException e)
        {
            stopTraversing();
            throw e;
        }

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

    /** Changing the running mode of the automaton */
    protected abstract void changeRunningMode();

    /**
     * Verifying finiteness of the tree
     * @param tree tree to verify
     * @throws TreeFinitenessException if tree finiteness is violated
     */
    protected abstract void assertFiniteness(TreeNode tree)
        throws TreeFinitenessException;

    /** Initializing automaton and tree before running on tree */
    protected void initialize()
        throws IllegalVariableValueException, NoTreeException, NoTraversingStrategyException,
               NoNonDeterministicStrategyException
    {
        if(tree == null)
            throw new NoTreeException("No tree specified");

        if(getTraversing() == null)
            throw new NoTraversingStrategyException("Automaton has no traversing strategy");

        deleteTreeStates();
        setRunningMode(AutomatonRunningMode.RUNNING);
    }

    /**
     * Testing if specified tree contains a recursive node
     * @param node tree node
     * @return {@code true} if tree has a recursive node, otherwise {@code false}
     */
    protected boolean containsRecursiveNode(TreeNode node)
    {
        return node != null && (node.getType() == NodeType.REC || containsRecursiveNode(
            node.getLeft()) || containsRecursiveNode(node.getRight()));
    }

    /**
     * Processing tree nodes in each step
     * @param node nodes to process
     */
    protected abstract void processNode(TreeNode node)
        throws NoSuchTransitionException, IllegalVariableValueException,
               UndefinedStateValueException;

    private void deleteTreeStates()
    {
        TopDownTraversing t = new TopDownDFS();

        t.initialize(tree);
        t.forEachRemaining(iterable -> iterable.forEach(TreeNode::deleteState));
    }
}
