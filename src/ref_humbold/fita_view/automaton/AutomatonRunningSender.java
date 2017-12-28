package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.command.DefaultCommandSender;
import ref_humbold.fita_view.command.MultiParamsCommand;
import ref_humbold.fita_view.tree.TreeNode;

public class AutomatonRunningSender
    extends DefaultCommandSender<Iterable<TreeNode>>
{
    public AutomatonRunningSender()
    {
        super();
    }

    public void send(String commandName, Iterable<TreeNode> nodes)
    {
        send(new MultiParamsCommand<>(this, commandName, nodes));
    }
}
