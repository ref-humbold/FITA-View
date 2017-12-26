package ref_humbold.fita_view.tree;

public class RepeatVertex
    extends NodeVertex
{
    public RepeatVertex(String label)
    {
        super(label);
    }

    public RepeatVertex(String label, int index)
    {
        super(label, index);
    }

    @Override
    public VertexType getType()
    {
        return VertexType.REPEAT;
    }

    @Override
    public String toString()
    {
        String leftString = getLeft() == null ? "#" : getLeft().toString();
        String rightString = getRight() == null ? "#" : getRight().toString();

        return "<& " + getLabel() + "," + leftString + ", " + rightString + " &>";
    }
}
