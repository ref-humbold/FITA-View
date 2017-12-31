package ref_humbold.fita_view.automaton;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.TreeNode;

public class NullAutomaton
    implements TreeAutomaton
{
    private static NullAutomaton instance = null;

    private NullAutomaton()
    {
        super();
    }

    public static NullAutomaton getInstance()
    {
        if(instance == null)
            instance = new NullAutomaton();

        return instance;
    }

    @Override
    public String getTypeName()
    {
        return "Automaton";
    }

    @Override
    public List<Variable> getVariables()
    {
        return Collections.emptyList();
    }

    @Override
    public Set<String> getAlphabet()
    {
        return Collections.emptySet();
    }

    @Override
    public boolean isAccepted()
    {
        return false;
    }

    @Override
    public void setSendingMessages(boolean sendingMessages)
    {
    }

    @Override
    public void setTraversing(TraversingMode mode)
    {
    }

    @Override
    public void setTree(TreeNode tree)
    {
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return false;
    }

    @Override
    public void run()
    {
    }

    @Override
    public void makeStepForward()
    {
    }

    @Override
    public TreeNode generateTree()
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "Null automaton";
    }
}
