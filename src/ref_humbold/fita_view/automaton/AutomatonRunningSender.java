package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.message.DefaultParameterizedMessageSender;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.tree.TreeNode;

public class AutomatonRunningSender
    extends DefaultParameterizedMessageSender<Iterable<TreeNode>>
{
    private static AutomatonRunningSender instance = null;

    private AutomatonRunningSender()
    {
        super();
    }

    public static AutomatonRunningSender getInstance()
    {
        if(instance == null)
            instance = new AutomatonRunningSender();

        return instance;
    }

    public void send(Iterable<TreeNode> nodes)
    {
        send(new Message<>(this, nodes));
    }
}
