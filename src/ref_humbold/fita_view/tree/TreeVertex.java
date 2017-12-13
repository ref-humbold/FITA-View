package ref_humbold.fita_view.tree;

import java.util.Map;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeVertex
{
    final int index;

    TreeVertex(int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return this.index;
    }

    /**
     * @return type of the vertex
     */
    public abstract VertexType getType();

    /**
     * @return left son of the vertex
     */
    public abstract TreeVertex getLeft();

    protected abstract void setLeft(TreeVertex vertex);

    /**
     * @return right son of the vertex
     */
    public abstract TreeVertex getRight();

    protected abstract void setRight(TreeVertex vertex);

    /**
     * @return parent of the vertex
     */
    public abstract TreeVertex getParent();

    protected abstract void setParent(TreeVertex vertex);

    /**
     * @return label of the vertex
     */
    public abstract String getLabel();

    /**
     * @param var state variable
     * @return variable value in node
     */
    public abstract String getState(Variable var);

    /**
     * @param var state variable
     * @param value variable value
     */
    public abstract void setState(Variable var, String value)
        throws IllegalVariableValueException;

    /**
     * @return values of all variables in node
     */
    public abstract Map<Variable, String> getFullState();

    /**
     * @return {@code true} if vertex has both left and right children, otherwise {@code false}
     */
    public boolean hasChildren()
    {
        return getLeft() != null && getRight() != null;
    }
}
