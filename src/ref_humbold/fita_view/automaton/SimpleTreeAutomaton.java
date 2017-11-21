package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeVertex;

public abstract class SimpleTreeAutomaton
    implements TreeAutomaton
{
    protected List<Variable> variables;
    protected Set<String> alphabet;
    protected TreeVertex tree;
    protected TreeTraversing traversing;

    public SimpleTreeAutomaton(Set<String> alphabet, Variable... vars)
    {
        this.alphabet = alphabet;
        this.variables = Arrays.asList(vars);
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

    protected abstract void initTree();
}
