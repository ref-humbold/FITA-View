package fitaview.automaton;

import java.util.Map;

import fitaview.Triple;
import fitaview.messaging.DefaultMessageSender;

public final class TransitionSender
        extends DefaultMessageSender<Triple<NodeInfoSource, String, Map<Variable, String>>>
{
    private static TransitionSender instance = null;

    private TransitionSender()
    {
        super();
    }

    public static TransitionSender getInstance()
    {
        if(instance == null)
            instance = new TransitionSender();

        return instance;
    }
}
