package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.message.DefaultParameterizedMessageSender;
import ref_humbold.fita_view.message.Message;

public class AutomatonRunningModeSender
    extends DefaultParameterizedMessageSender<AutomatonRunningMode>
{
    private static AutomatonRunningModeSender instance = null;

    private AutomatonRunningModeSender()
    {
        super();
    }

    public static AutomatonRunningModeSender getInstance()
    {
        if(instance == null)
            instance = new AutomatonRunningModeSender();

        return instance;
    }

    public void send(AutomatonRunningMode mode)
    {
        send(new Message<>(this, mode));
    }
}
