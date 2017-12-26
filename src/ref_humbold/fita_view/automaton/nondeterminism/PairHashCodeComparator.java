package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Comparator;

import ref_humbold.fita_view.Pair;

class PairHashCodeComparator
    implements Comparator<Pair<String, String>>
{
    @Override
    public int compare(Pair<String, String> pair1, Pair<String, String> pair2)
    {
        return Integer.compare(pair1.hashCode(), pair2.hashCode());
    }
}
