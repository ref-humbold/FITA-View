package ref_humbold.fita_view.automaton;

import java.util.Map;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.messaging.DefaultMessageSender;

public class TransitionSender
    extends
    DefaultMessageSender<Triple<Pair<String, Map<Variable, String>>, Pair<String, Map<Variable, String>>, Pair<String, Map<Variable, String>>>>
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
