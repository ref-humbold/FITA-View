package ref_humbold.fita_view.tree;

public class RepeatVertex
    extends NodeVertex
{
    public RepeatVertex(String label)
    {
        super(label);
    }

    RepeatVertex(String label, int index)
    {
        super(label, index);
    }

    @Override
    public VertexType getType()
    {
        return VertexType.REPEAT;
    }
}
