package refhumbold.fitaview.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import refhumbold.fitaview.automaton.IllegalVariableValueException;
import refhumbold.fitaview.automaton.Variable;

public class StandardNode
    extends TreeNode
{
    private TreeNode left = new NullNode();
    private TreeNode right = new NullNode();
    private TreeNode parent = null;
    private String label;
    private Map<Variable, String> state = new HashMap<>();

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
        this.setLeft(left);
        this.setRight(right);
    }

    @Override
    public NodeType getType()
    {
        return NodeType.NODE;
    }

    @Override
    public TreeNode getLeft()
    {
        return this.left;
    }

    protected void setLeft(TreeNode node)
        throws NodeHasParentException
    {
        if(node == null)
            node = new NullNode();

        if(node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        this.left.setParent(null);
        this.left = node;
        this.left.setParent(this);
    }

    @Override
    public TreeNode getRight()
    {
        return this.right;
    }

    protected void setRight(TreeNode node)
        throws NodeHasParentException
    {
        if(node == null)
            node = new NullNode();

        if(node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        this.right.setParent(null);
        this.right = node;
        this.right.setParent(this);
    }

    @Override
    public TreeNode getParent()
    {
        return this.parent;
    }

    @Override
    protected void setParent(TreeNode node)
    {
        this.parent = node;
    }

    @Override
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public Map<Variable, String> getStateWithNulls()
    {
        return this.state;
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
            this.state.put(var, var.getInitValue());
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

        this.state.put(var, value);
    }

    @Override
    public void deleteState()
    {
        state.clear();
    }

    @Override
    public String toString()
    {
        return "<$ \'" + label + "\', " + left.toString() + ", " + right.toString() + " $>";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof StandardNode))
            return false;

        StandardNode other = (StandardNode)o;

        return Objects.equals(this.label, other.label) && Objects.equals(this.left, other.left)
            && Objects.equals(this.right, other.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index, label);
    }
}
