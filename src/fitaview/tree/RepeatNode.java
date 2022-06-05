package fitaview.tree;

public class RepeatNode
        extends StandardNode
{
    public RepeatNode(String label, int index)
    {
        super(label, index);
    }

    @Override
    public NodeType getType()
    {
        return NodeType.REPEAT;
    }

    @Override
    public void setLeft(TreeNode node)
            throws NodeHasParentException
    {
        super.setLeft(node);
    }

    @Override
    public void setRight(TreeNode node)
            throws NodeHasParentException
    {
        super.setRight(node);
    }

    @Override
    public String toString()
    {
        String leftString = getLeft() == null ? "#" : getLeft().toString();
        String rightString = getRight() == null ? "#" : getRight().toString();

        return String.format("<@ '%s',%s, %s @>", getLabel(), leftString, rightString);
    }
}
