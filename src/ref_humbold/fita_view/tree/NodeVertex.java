package ref_humbold.fita_view.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.automaton.IncorrectValueException;
import ref_humbold.fita_view.automaton.Variable;

public class NodeVertex
    extends TreeVertex
{
    private TreeVertex left = null;
    private TreeVertex right = null;
    private TreeVertex parent = null;
    private String label;
    private Map<Variable, String> state = new HashMap<>();

    public NodeVertex(String label)
    {
        this(label, 0);
    }

    NodeVertex(String label, int id)
    {
        super(id);

        if(label == null)
            throw new IllegalArgumentException("Label is null");

        this.label = label;
    }

    NodeVertex(String label, TreeVertex left, TreeVertex right)
    {
        this(label);
        this.setLeft(left);
        this.setRight(right);
    }

    NodeVertex(String label, int id, TreeVertex left, TreeVertex right)
    {
        this(label, id);
        this.setLeft(left);
        this.setRight(right);
    }

    @Override
    public String getTypename()
    {
        return "node";
    }

    @Override
    public TreeVertex getLeft()
    {
        return this.left;
    }

    @Override
    protected void setLeft(TreeVertex vertex)
    {
        this.left = vertex;
        this.left.setParent(this);
    }

    @Override
    public TreeVertex getRight()
    {
        return this.right;
    }

    @Override
    protected void setRight(TreeVertex vertex)
    {
        this.right = vertex;
        this.left.setParent(this);
    }

    @Override
    public TreeVertex getParent()
    {
        return this.parent;
    }

    @Override
    protected void setParent(TreeVertex vertex)
    {
        this.parent = vertex;
    }

    @Override
    public String getLabel()
    {
        return this.label;
    }

    @Override
    public String getState(Variable var)
    {
        return state.get(var);
    }

    @Override
    public void setState(Variable var, String value)
    {
        if(!var.isValue(value))
            throw new IncorrectValueException(value);

        state.put(var, value);
    }

    @Override
    public String toString()
    {
        String leftString = left == null ? "#" : left.toString();
        String rightString = right == null ? "#" : right.toString();

        return "{{ " + label + ", " + leftString + ", " + rightString + " }}";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || !(o instanceof NodeVertex))
            return false;

        NodeVertex other = (NodeVertex)o;

        return this.label.equals(other.label) && Objects.equals(this.left, other.left)
            && Objects.equals(this.right, other.right);
    }
}
