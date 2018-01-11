package ref_humbold.fita_view.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public class StandardNode
    extends TreeNode
{
    private TreeNode left = null;
    private TreeNode right = null;
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
        if(node != null && node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        if(this.left != null)
            this.left.setParent(null);

        this.left = node;

        if(this.left != null)
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
        if(node != null && node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        if(this.right != null)
            this.right.setParent(null);

        this.right = node;

        if(this.right != null)
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
    public Map<Variable, String> getState()
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
    public void setStateInitValue(Variable var)
    {
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
        String leftString = left == null ? "#" : left.toString();
        String rightString = right == null ? "#" : right.toString();

        return "<$ \'" + label + "\', " + leftString + ", " + rightString + " $>";
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
        return index * 37 + label.hashCode();
    }
}
