package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.message.DefaultParameterizedMessageSender;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.tree.TreeNode;

public class AutomatonCurrentNodesSender
    extends DefaultParameterizedMessageSender<Iterable<TreeNode>>
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

    public void send(Iterable<TreeNode> nodes)
    {
        send(new Message<>(this, nodes));
    }
}
