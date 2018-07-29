package refhumbold.fitaview.automaton;

import java.util.Map;

import refhumbold.fitaview.Triple;
import refhumbold.fitaview.messaging.DefaultMessageSender;

public class TransitionSender
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
