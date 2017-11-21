package ref_humbold.fita_view.automaton;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFTA
    extends TopDownTreeAutomaton
{
    private Map<Triple<Variable, String, String>, Pair<String, String>> transitions;

    public TopDownDFTA(Set<String> alphabet, Variable... vars)
    {
        super(alphabet, vars);
        this.transitions = new HashMap<>();
    }

    @Override
    public void run()
    {
    }

    @Override
    public TreeVertex generateTree()
    {
        return null;
    }

    @Override
    protected void addTransition(Variable var, String value, String label, String leftResult,
                                 String rightResult)
    {
        transitions.put(Triple.make(var, value, label), Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> doTransition(Variable var, String value, String label)
    {
        return transitions.get(Triple.make(var, value, label));
    }
}
