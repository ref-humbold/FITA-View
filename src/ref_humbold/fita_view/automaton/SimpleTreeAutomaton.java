package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeVertex;

public abstract class SimpleTreeAutomaton
    implements TreeAutomaton
{
    protected Set<String> alphabet;
    protected List<Variable> variables;
    protected TreeVertex tree;
    protected TreeTraversing traversing;

    public SimpleTreeAutomaton(Collection<String> alphabet, Collection<Variable> variables)
    {
        this.alphabet = new HashSet<>(alphabet);
        this.variables = new ArrayList<>(variables);
    }

    @Override
    public void setTree(TreeVertex tree)
    {
        this.tree = tree;
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return alphabet.contains(label);
    }

    @Override
    public void makeStepForward()
    {
        traversing.moveForward();
    }

    @Override
    public void makeStepBackward()
    {
        traversing.moveBackward();
    }

    @Override
    public abstract void run();

    @Override
    public abstract TreeVertex generateTree();

    /**
     * Setting inital state of variables in tree.
     */
    protected abstract void initTree()
        throws IncorrectValueException;
}
