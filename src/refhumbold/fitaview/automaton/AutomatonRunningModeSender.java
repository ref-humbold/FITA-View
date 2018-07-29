package refhumbold.fitaview.automaton;

import refhumbold.fitaview.messaging.DefaultSignalSender;

public class AutomatonRunningModeSender
    extends DefaultSignalSender
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
