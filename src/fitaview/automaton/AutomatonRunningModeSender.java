package fitaview.automaton;

import fitaview.messaging.DefaultSignalSender;

public final class AutomatonRunningModeSender
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
