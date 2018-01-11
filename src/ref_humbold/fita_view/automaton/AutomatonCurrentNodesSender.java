package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.messaging.DefaultMessageSender;
import ref_humbold.fita_view.tree.TreeNode;

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
