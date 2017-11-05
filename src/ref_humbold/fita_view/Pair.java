package ref_humbold.fita_view;

public class Pair<F extends Comparable<F>, S extends Comparable<S>>
    implements Comparable<Pair<F, S>>
{
    private final F first;
    private final S second;

    public Pair(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    public static <F extends Comparable<F>, S extends Comparable<S>> Pair<F, S> make()
    {
        return make(null, null);
    }

    public static <F extends Comparable<F>, S extends Comparable<S>> Pair<F, S> make(F first,
                                                                                     S second)
    {
        return new Pair<>(first, second);
    }

    public F getFirst()
    {
        return this.first;
    }

    public S getSecond()
    {
        return this.second;
    }

    @Override
    public int compareTo(Pair<F, S> p)
    {
        if(this.first == null)
            return p.first == null ? 0 : -1;

        int comparedFirst = this.first.compareTo(p.first);

        if(comparedFirst != 0)
            return comparedFirst;

        if(this.second == null)
            return p.getSecond() == null ? 0 : -1;

        return this.second.compareTo(p.second);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(!(obj instanceof Pair))
            return false;

        Pair<?, ?> other = (Pair<?, ?>)obj;

        if(this.first == null && other.first == null || this.second == null && other.second == null)
            return true;

        return this.first != null && this.first.equals(other.first) && this.second != null
               && this.second.equals(other.second);
    }

    @Override
    public int hashCode()
    {
        final int prime = 37;
        int result = 1;

        result = prime * result + (first == null ? 0 : first.hashCode());
        result = prime * result + (second == null ? 0 : second.hashCode());

        return result;
    }

    @Override
    public String toString()
    {
        String firstString = first == null ? "null" : first.toString();
        String secondString = second == null ? "null" : second.toString();

        return "(" + firstString + ", " + secondString + ")";
    }
}
