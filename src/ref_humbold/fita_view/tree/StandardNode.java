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
        return left;
    }

    protected void setLeft(TreeNode node)
        throws NodeHasParentException
    {
        if(node != null && node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        if(left != null)
            left.setParent(null);

        left = node;

        if(left != null)
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
        if(node != null && node.getParent() != null)
            throw new NodeHasParentException(
                "Node has already got a parent, so it cannot be assigned as a child.");

        if(right != null)
            right.setParent(null);

        right = node;

        if(right != null)
            right.setParent(this);
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
        return label;
    }

    @Override
    public Map<Variable, String> getState()
    {
        return state;
    }

    @Override
    public void setStateInitValue(Variable var)
    {
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
