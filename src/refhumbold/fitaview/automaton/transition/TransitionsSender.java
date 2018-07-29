package refhumbold.fitaview.automaton.transition;

import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.Variable;
import refhumbold.fitaview.messaging.DefaultMessageSender;

public class TransitionsSender
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
