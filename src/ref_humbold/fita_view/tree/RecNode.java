package ref_humbold.fita_view.tree;

import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public class RecNode
    extends TreeNode
{
    private RepeatNode recursive;
    private TreeNode parent;

    public RecNode(RepeatNode recursive, int index)
    {
        super(index);

        if(recursive == null)
            throw new IllegalArgumentException("Recursive node is null");

        this.recursive = recursive;
    }

    @Override
    public NodeType getType()
    {
        return NodeType.REC;
    }

    @Override
    public TreeNode getLeft()
    {
        return recursive.getLeft();
    }

    @Override
    public TreeNode getRight()
    {
        return recursive.getRight();
    }

    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    @Override
    protected void setParent(TreeNode node)
    {
        parent = node;
    }

    @Override
    public String getLabel()
    {
        return recursive.getLabel();
    }

    @Override
    public Map<Variable, String> getState()
    {
        return recursive.getState();
    }

    @Override
    public void setState(Map<Variable, String> state)
        throws IllegalVariableValueException
    {
        recursive.setState(state);
    }

    @Override
    public void setStateInitValue(Variable var)
    {
        recursive.setStateInitValue(var);
    }

    @Override
    public String getStateValueOrNull(Variable var)
    {
        return recursive.getStateValueOrNull(var);
    }

    @Override
    public void setStateValue(Variable var, String value)
        throws IllegalVariableValueException
    {
        recursive.setStateValue(var, value);
    }

    @Override
    public void deleteState()
    {
        recursive.deleteState();
    }

    @Override
    public String toString()
    {
        return "<@ REC @>";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof RecNode))
            return false;

        RecNode other = (RecNode)o;

        return this.recursive.index == other.recursive.index;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(recursive.index, getLabel());
    }
}
