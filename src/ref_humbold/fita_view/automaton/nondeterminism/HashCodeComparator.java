package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Comparator;

class HashCodeComparator<T>
    implements Comparator<T>
{
    @Override
    public int compare(T object1, T object2)
    {
        return Integer.compare(object1.hashCode(), object2.hashCode());
    }
}
