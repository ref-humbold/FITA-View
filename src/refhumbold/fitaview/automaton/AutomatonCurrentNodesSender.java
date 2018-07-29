package refhumbold.fitaview.automaton;

import refhumbold.fitaview.messaging.DefaultMessageSender;
import refhumbold.fitaview.tree.TreeNode;

public class AutomatonCurrentNodesSender
    extends DefaultMessageSender<Iterable<TreeNode>>
{
    private static AutomatonCurrentNodesSender instance = null;

    private AutomatonCurrentNodesSender()
    {
        super();
    }

    public static AutomatonCurrentNodesSender getInstance()
    {
        if(instance == null)
            instance = new AutomatonCurrentNodesSender();

        return instance;
    }
}
