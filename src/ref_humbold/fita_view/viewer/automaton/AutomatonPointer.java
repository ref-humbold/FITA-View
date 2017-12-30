package ref_humbold.fita_view.viewer.automaton;

import ref_humbold.fita_view.automaton.NullAutomaton;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.message.DefaultMessageSender;
import ref_humbold.fita_view.message.Message;

public class AutomatonPointer
    extends DefaultMessageSender
{
    private TreeAutomaton automaton;

    public AutomatonPointer()
    {
        super();
        this.automaton = NullAutomaton.getInstance();
    }

    public TreeAutomaton get()
    {
        return this.automaton;
    }

    public void set(TreeAutomaton automaton)
    {
        this.automaton = automaton == null ? NullAutomaton.getInstance() : automaton;
        this.send(new Message<>(this, "AutomatonPointer#set", null));
    }

    public void delete()
    {
        set(null);
    }
}
