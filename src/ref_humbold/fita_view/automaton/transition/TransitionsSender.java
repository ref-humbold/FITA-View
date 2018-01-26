package ref_humbold.fita_view.automaton.transition;

import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.messaging.DefaultMessageSender;

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
