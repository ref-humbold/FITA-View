package ref_humbold.fita_view;

public interface Tuple
{
    /**
     * @return number of elements in tuple
     */
    int size();

    /**
     * @return an array containing tuple elements in inner order
     */
    Object[] toArray();
}
