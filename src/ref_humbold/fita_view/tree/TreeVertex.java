package ref_humbold.fita_view.tree;

import java.util.Map;

import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeVertex
{
    final int id;

    TreeVertex(int id)
    {
        this.id = id;
    }

    /**
     * @return type of the node
     */
    public abstract VertexType getType();

    public abstract TreeVertex getLeft();

    protected abstract void setLeft(TreeVertex vertex);

    public abstract TreeVertex getRight();

    protected abstract void setRight(TreeVertex vertex);

    public abstract TreeVertex getParent();

    protected abstract void setParent(TreeVertex vertex);

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

    public abstract Map<Variable, String> getFullState();
}
