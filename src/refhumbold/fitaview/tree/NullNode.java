package refhumbold.fitaview.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import refhumbold.fitaview.automaton.IllegalVariableValueException;
import refhumbold.fitaview.automaton.Variable;

public class NullNode
    extends TreeNode
{
    private TreeNode parent;
    private Map<Variable, String> state = new HashMap<>();

    public NullNode()
    {
        super(-1);
    }

    @Override
    public NodeType getType()
    {
        return NodeType.NULL;
    }

    @Override
    public TreeNode getLeft()
    {
        return null;
    }

    @Override
    public TreeNode getRight()
    {
        return null;
    }

    @Override
    public TreeNode getParent()
    {
        return parent;
    }

    @Override
    protected void setParent(TreeNode parent)
    {
        this.parent = parent;
    }

    @Override
    public String getLabel()
    {
        return "";
    }

    @Override
    public Map<Variable, String> getStateWithNulls()
    {
        return state;
    }

    @Override
    public void setState(Map<Variable, String> state)
        throws IllegalVariableValueException
    {
        for(Map.Entry<Variable, String> entry : state.entrySet())
            setStateValue(entry.getKey(), entry.getValue());
    }

    @Override
    public void setInitialState(Collection<Variable> vars)
    {
        for(Variable var : vars)
            state.put(var, var.getInitValue());
    }

    @Override
    public String getStateValueOrNull(Variable var)
    {
        return state.get(var);
    }

    @Override
    public void setStateValue(Variable var, String value)
        throws IllegalVariableValueException
    {
        if(!var.contains(value))
            throw new IllegalVariableValueException(value);

        state.put(var, value);
    }

    @Override
    public void deleteState()
    {
        state.clear();
    }

    @Override
    public String toString()
    {
        return "#";
    }

    @Override
    public boolean equals(Object o)
    {
        return this == o || o instanceof NullNode;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index);
    }
}
