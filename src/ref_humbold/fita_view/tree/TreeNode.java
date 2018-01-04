package ref_humbold.fita_view.tree;

import java.util.Map;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeNode
{
    protected final int index;

    public TreeNode(int index)
    {
        this.index = index;
    }

    /**
     * @return index of the node
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * @return type of the node
     */
    public abstract NodeType getType();

    /**
     * @return left son of the node
     */
    public abstract TreeNode getLeft();

    /**
     * @return right son of the node
     */
    public abstract TreeNode getRight();

    /**
     * @return parent of the node
     */
    public abstract TreeNode getParent();

    protected abstract void setParent(TreeNode node);

    /**
     * @return label of the node
     */
    public abstract String getLabel();

    /**
     * @return values of all variables in the node
     */
    public abstract Map<Variable, String> getState();

    /**
     * @param var state variable
     */
    public abstract void setStateInitValue(Variable var);

    /**
     * @param var state variable
     * @return variable value in the node
     * @throws UndefinedTreeStateException if variable value is undefined
     */
    public String getStateValue(Variable var)
        throws UndefinedTreeStateException
    {
        String value = getStateValueOrNull(var);

        if(value == null)
            throw new UndefinedTreeStateException(
                "Node has undefined state for variable " + var + ".");

        return value;
    }

    /**
     * @param var state variable
     * @return variable value in the node or {@code null} if variable value is undefined
     */
    public abstract String getStateValueOrNull(Variable var);

    /**
     * @param var state variable
     * @param value variable value
     */
    public abstract void setStateValue(Variable var, String value)
        throws IllegalVariableValueException;

    /**
     * Removing values of all state variables in the node.
     */
    public abstract void deleteState();

    /**
     * @return {@code true} if node has both left and right children, otherwise {@code false}
     */
    public boolean hasChildren()
    {
        return getLeft() != null && getRight() != null;
    }
}
