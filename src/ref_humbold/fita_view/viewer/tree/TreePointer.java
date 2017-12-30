package ref_humbold.fita_view.viewer.tree;

import ref_humbold.fita_view.message.DefaultMessageSender;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.tree.TreeNode;

public class TreePointer
    extends DefaultMessageSender
{
    private TreeNode tree;

    public TreePointer()
    {
        super();
        this.tree = null;
    }

    public boolean isTreeEmpty()
    {
        return tree == null;
    }

    public TreeNode get()
    {
        return this.tree;
    }

    public void set(TreeNode tree)
    {
        this.tree = tree;
        this.send(new Message<>(this, "TreePointer#set", null));
    }

    public void delete()
    {
        set(null);
    }
}
