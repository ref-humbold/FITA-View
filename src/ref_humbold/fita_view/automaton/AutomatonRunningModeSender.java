package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.messaging.DefaultMessageSender;

public class AutomatonRunningModeSender
    extends DefaultMessageSender<AutomatonRunningMode>
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
}
