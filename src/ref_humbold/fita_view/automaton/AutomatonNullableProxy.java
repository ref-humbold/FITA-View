package ref_humbold.fita_view.automaton;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.command.Command;
import ref_humbold.fita_view.command.CommandReceiver;
import ref_humbold.fita_view.command.DefaultCommandSender;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public class AutomatonNullableProxy
    extends DefaultCommandSender<Void>
    implements TreeAutomaton
{
    private static AutomatonNullableProxy instance = null;

    private TreeAutomaton automaton;
    private Set<CommandReceiver<Void>> receivers = new HashSet<>();

    private AutomatonNullableProxy()
    {
        super();
    }

    public static AutomatonNullableProxy getInstance()
    {
        if(instance == null)
            instance = new AutomatonNullableProxy();

        return instance;
    }

    @Override
    public List<Variable> getVariables()
    {
        return automaton == null ? Collections.emptyList() : automaton.getVariables();
    }

    @Override
    public Set<String> getAlphabet()
    {
        return automaton == null ? Collections.emptySet() : automaton.getAlphabet();
    }

    @Override
    public boolean isAccepted()
        throws UndefinedAcceptanceException, UndefinedTreeStateException
    {
        return automaton == null ? false : automaton.isAccepted();
    }

    @Override
    public void setTraversing(TraversingMode mode)
        throws IncorrectTraversingException
    {
        if(automaton != null)
            automaton.setTraversing(mode);
    }

    @Override
    public void setTree(TreeNode tree)
        throws EmptyTreeException, TreeFinitenessException
    {
        if(automaton != null)
            automaton.setTree(tree);
    }

    public void setAutomaton(TreeAutomaton automaton)
    {
        this.automaton = automaton;
        send(new Command<>(this, "setAutomaton", null));
    }

    @Override
    public boolean isInAlphabet(String label)
    {
        return automaton == null ? false : automaton.isInAlphabet(label);
    }

    @Override
    public void run()
        throws NoTraversingException, NoSuchTransitionException, UndefinedTreeStateException,
               IllegalVariableValueException
    {
        if(automaton != null)
            automaton.run();
    }

    @Override
    public void makeStepForward()
        throws NoTraversingException, NoSuchTransitionException, UndefinedTreeStateException,
               IllegalVariableValueException
    {
        if(automaton != null)
            automaton.makeStepForward();
    }

    @Override
    public TreeNode generateTree()
    {
        return automaton == null ? null : automaton.generateTree();
    }
}
