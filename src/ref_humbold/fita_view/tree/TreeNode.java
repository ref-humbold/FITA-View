package ref_humbold.fita_view.tree;

import java.util.Collection;
import java.util.Map;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeNode
{
    public static final int MAX_HEIGHT = 15;

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
     * @throws UndefinedStateValueException if variable value is undefined
     */
    public Map<Variable, String> getState()
        throws UndefinedStateValueException
    {
        Map<Variable, String> state = getStateWithNulls();

        for(Map.Entry<Variable, String> entry : state.entrySet())
        {
            if(entry.getValue() == null)
                throw new UndefinedStateValueException(
                    "Node has undefined state for variable " + entry.getKey() + ".");
        }

        return state;
    }

    /**
     * @param state new state for the node
     */
    public abstract void setState(Map<Variable, String> state)
        throws IllegalVariableValueException;

    /**
     * @return values of all variables in the node
     */
    public abstract Map<Variable, String> getStateWithNulls();

    /**
     * @param vars state variables
     */
    public abstract void setInitialState(Collection<Variable> vars);

    /**
     * @param var state variable
     * @return variable value in the node
     * @throws UndefinedStateValueException if variable value is undefined
     */
    public String getStateValue(Variable var)
        throws UndefinedStateValueException
    {
        String value = getStateValueOrNull(var);

        if(value == null)
            throw new UndefinedStateValueException(
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
