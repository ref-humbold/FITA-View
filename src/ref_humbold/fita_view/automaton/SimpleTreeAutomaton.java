package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TopDownTraversing;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeVertex;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;
import ref_humbold.fita_view.tree.VertexType;

public abstract class SimpleTreeAutomaton
    implements TreeAutomaton
{
    protected Set<String> alphabet;
    protected List<Variable> variables;
    protected TreeVertex tree;
    protected boolean isRunning = false;

    public SimpleTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        this.variables = new ArrayList<>(variables);
        this.alphabet = new HashSet<>(alphabet);
    }

    /**
     * @return current traversing strategy of the automaton
     */
    protected abstract TreeTraversing getTraversing();

    @Override
    public void setTree(TreeVertex tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(tree == null)
            throw new EmptyTreeException("Tree is empty.");

        this.tree = tree;
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return alphabet.contains(label);
    }

    @Override
    public void run()
        throws IllegalVariableValueException, NoSuchTransitionException, NoTraversingException,
               UndefinedTreeStateException
    {
        if(getTraversing() == null)
            throw new NoTraversingException("Automaton has no traversing strategy.");

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
    public int hashCode()
    {
        return alphabet.hashCode() * 37 + variables.hashCode();
    }

    /**
     * Initializing automaton and tree before running on tree.
     */
    protected void initialize()
        throws IllegalVariableValueException
    {
        TopDownTraversing t =
            TraversingFactory.getInstance().getTopDownTraversing(TraversingFactory.Mode.DFS);

        t.initialize(tree);

        while(t.hasNext())
            for(TreeVertex v : t.next())
                v.deleteFullState();

        isRunning = true;
    }

    /**
     * Testing if specified tree contains a recursive node.
     * @param node tree node
     * @return {@code true} if tree has a recursive node, otherwise {@code false}
     */
    protected boolean containsRecursiveNode(TreeVertex node)
    {
        return node != null && (node.getType() == VertexType.REC || containsRecursiveNode(
            node.getLeft()) || containsRecursiveNode(node.getRight()));
    }
}
