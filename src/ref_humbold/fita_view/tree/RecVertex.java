package ref_humbold.fita_view.tree;

import java.util.Map;

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

    public RecVertex(TreeVertex recursive, int index)
    {
        super(index);

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
    public void setLeft(TreeVertex vertex)
    {
        recursive.setLeft(vertex);
    }

    @Override
    public TreeVertex getRight()
    {
        return recursive.getRight();
    }

    @Override
    public void setRight(TreeVertex vertex)
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
    public Map<Variable, String> getFullState()
    {
        return recursive.getFullState();
    }

    @Override
    public String getStateOrNull(Variable var)
    {
        return recursive.getStateOrNull(var);
    }

    @Override
    public void setState(Variable var, String value)
        throws IllegalVariableValueException
    {
        recursive.setState(var, value);
    }

    @Override
    public void deleteFullState()
    {
        recursive.deleteFullState();
    }

    @Override
    public String toString()
    {
        return "<$ REC $>";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof RecVertex))
            return false;

        RecVertex other = (RecVertex)o;

        return this.recursive.index == other.recursive.index;
    }

    @Override
    public int hashCode()
    {
        return recursive.index * 37 + getLabel().hashCode();
    }
}
