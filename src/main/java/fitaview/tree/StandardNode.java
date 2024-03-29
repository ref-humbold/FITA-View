package fitaview.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;

public class StandardNode
        extends TreeNode
{
    private TreeNode left = new NullNode();
    private TreeNode right = new NullNode();
    private TreeNode parent = null;
    private final String label;
    private final Map<Variable, String> state = new HashMap<>();

    public StandardNode(String label, int index)
    {
        super(index);

        if(label == null || label.isEmpty())
            throw new IllegalArgumentException("Label is null");

        this.label = label;
    }

    public StandardNode(String label, int index, TreeNode left, TreeNode right)
            throws NodeHasParentException
    {
        this(label, index);
        setLeft(left);
        setRight(right);
    }

    @Override
    public NodeType getType()
    {
        return NodeType.NODE;
    }

    @Override
    public TreeNode getLeft()
    {
        return left;
    }

    protected void setLeft(TreeNode node)
            throws NodeHasParentException
    {
        if(node == null)
            node = new NullNode();

        if(node.getParent() != null)
            throw new NodeHasParentException(
                    "Node has already got a parent, so it cannot be assigned as a child");

        left.setParent(null);
        left = node;
        left.setParent(this);
    }

    @Override
    public TreeNode getRight()
    {
        return right;
    }

    protected void setRight(TreeNode node)
            throws NodeHasParentException
    {
        if(node == null)
            node = new NullNode();

        if(node.getParent() != null)
            throw new NodeHasParentException(
                    "Node has already got a parent, so it cannot be assigned as a child");

        right.setParent(null);
        right = node;
        right.setParent(this);
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
        return label;
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
        return String.format("<$ '%s', %s, %s $>", label, left.toString(), right.toString());
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof StandardNode))
            return false;

        StandardNode other = (StandardNode)o;

        return Objects.equals(label, other.label) && Objects.equals(left, other.left)
                && Objects.equals(right, other.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index, label);
    }
}
