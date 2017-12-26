package ref_humbold.fita_view.tree;

import java.util.Map;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeVertex
{
    protected final int index;

    public TreeVertex(int index)
    {
        this.index = index;
    }

    /**
     * @return index of the vertex
     */
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

    /**
     * @param vertex new left son of the vertex
     */
    public abstract void setLeft(TreeVertex vertex);

    /**
     * @return right son of the vertex
     */
    public abstract TreeVertex getRight();

    /**
     * @param vertex new right son of the vertex
     */
    public abstract void setRight(TreeVertex vertex);

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
     * @return values of all variables in the vertex
     */
    public abstract Map<Variable, String> getFullState();

    /**
     * @param var state variable
     * @return variable value in the vertex
     * @throws UndefinedTreeStateException if variable value is undefined
     */
    public String getState(Variable var)
        throws UndefinedTreeStateException
    {
        String value = getStateOrNull(var);

        if(value == null)
            throw new UndefinedTreeStateException(
                "Node has undefined state for variable " + var + ".");

        return value;
    }

    /**
     * @param var state variable
     * @return variable value in the vertex or {@code null} if variable value is undefined
     */
    public abstract String getStateOrNull(Variable var);

    /**
     * @param var state variable
     * @param value variable value
     */
    public abstract void setState(Variable var, String value)
        throws IllegalVariableValueException;

    /**
     * Removing values of all state variables in the vertex.
     */
    public abstract void deleteFullState();

    /**
     * @return {@code true} if vertex has both left and right children, otherwise {@code false}
     */
    public boolean hasChildren()
    {
        return getLeft() != null && getRight() != null;
    }
}
