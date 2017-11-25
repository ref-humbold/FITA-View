package ref_humbold.fita_view.tree;

import ref_humbold.fita_view.automaton.Variable;

public abstract class TreeVertex
{
    final int id;

    TreeVertex(int id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return getLabel().hashCode();
    }

    /**
     * @return nazwa rodzaju węzła
     */
    public abstract String getTypename();

    public abstract TreeVertex getLeft();

    protected abstract void setLeft(TreeVertex vertex);

    public abstract TreeVertex getRight();

    protected abstract void setRight(TreeVertex vertex);

    public abstract TreeVertex getParent();

    protected abstract void setParent(TreeVertex vertex);

    /**
     * @return etykieta węzła
     */
    public abstract String getLabel();

    /**
     * @param var zmienna stanu
     * @return wartość zmiennej
     */
    public abstract String getState(Variable var);

    /**
     * @param var zmienna stanu
     * @param value wartość zmiennej
     */
    public abstract void setState(Variable var, String value);
}
