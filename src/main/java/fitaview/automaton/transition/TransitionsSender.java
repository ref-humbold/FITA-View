package fitaview.automaton.transition;

import fitaview.automaton.Variable;
import fitaview.messaging.DefaultMessageSender;
import fitaview.utils.Triple;

public final class TransitionsSender
        extends DefaultMessageSender<Triple<Variable, String, String>>
{
    private static TransitionsSender instance = null;

    private TransitionsSender()
    {
        super();
    }

    public static TransitionsSender getInstance()
    {
        if(instance == null)
            instance = new TransitionsSender();

        return instance;
    }
}
