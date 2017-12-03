package ref_humbold.fita_view.tree;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public class RecVertex
    extends TreeVertex
{
    private TreeVertex recursive;
    private TreeVertex parent;

    public RecVertex(TreeVertex recursive)
    {
        this(recursive, 0);
    }

    RecVertex(TreeVertex recursive, int id)
    {
        super(id);

        if(recursive == null)
            throw new IllegalArgumentException("Recursive node is null");

        this.recursive = recursive;
    }

    @Override
    public VertexType getType()
    {
        return VertexType.REC;
    }

    @Override
    public TreeVertex getLeft()
    {
        return recursive.getLeft();
    }

    @Override
    protected void setLeft(TreeVertex vertex)
    {
        recursive.setLeft(vertex);
    }

    @Override
    public TreeVertex getRight()
    {
        return recursive.getRight();
    }

    @Override
    protected void setRight(TreeVertex vertex)
    {
        recursive.setRight(vertex);
    }

    @Override
    public TreeVertex getParent()
    {
        return parent;
    }

    @Override
    protected void setParent(TreeVertex vertex)
    {
        parent = vertex;
    }

    @Override
    public String getLabel()
    {
        return recursive.getLabel();
    }

    @Override
    public String getState(Variable var)
    {
        return recursive.getState(var);
    }

    @Override
    public void setState(Variable var, String value)
        throws IllegalVariableValueException
    {
        recursive.setState(var, value);
    }

    @Override
    public String toString()
    {
        return "{{REC}}";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof RecVertex))
            return false;

        RecVertex other = (RecVertex)o;

        return this.recursive.id == other.recursive.id;
    }

    @Override
    public int hashCode()
    {
        return recursive.id * 37 + getLabel().hashCode();
    }
}
