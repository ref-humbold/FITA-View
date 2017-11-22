package ref_humbold.fita_view.automaton;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoice;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownNFTA
    extends TopDownTreeAutomaton
    implements NonDeterministicAutomaton
{
    private StateChoice choice;
    private Map<Triple<Variable, String, String>, Set<Pair<String, String>>> transitions;

    public TopDownNFTA(Set<String> alphabet, List<Variable> variables)
    {
        super(alphabet, variables);
        this.transitions = new HashMap<>();
    }

    @Override
    public StateChoice getChoice()
    {
        return this.choice;
    }

    @Override
    public void setChoice(StateChoice choice)
    {
        this.choice = choice;
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
        Triple<Variable, String, String> key = Triple.make(var, value, label);

        if(!transitions.containsKey(key))
            transitions.put(Triple.make(var, value, label), new HashSet<>());

        transitions.get(key).add(Pair.make(leftResult, rightResult));
    }

    @Override
    protected Pair<String, String> doTransition(Variable var, String value, String label)
    {
        return choice.chooseState(transitions.get(Triple.make(var, value, label)));
    }
}
