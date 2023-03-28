package fitaview.automaton;

import fitaview.messaging.DefaultMessageSender;
import fitaview.tree.TreeNode;

public final class AutomatonCurrentNodesSender
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
