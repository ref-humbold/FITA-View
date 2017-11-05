package ref_humbold.fita_view;

public class Triple<F extends Comparable<F>, S extends Comparable<S>, T extends Comparable<T>>
    implements Comparable<Triple<F, S, T>>
{
    private final F first;
    private final S second;
    private final T third;

    public Triple(F first, S second, T third)
    {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <F extends Comparable<F>, S extends Comparable<S>, T extends Comparable<T>> Triple<F, S, T> make()
    {
        return make(null, null, null);
    }

    public static <F extends Comparable<F>, S extends Comparable<S>, T extends Comparable<T>> Triple<F, S, T> make(
        F first, S second, T third)
    {
        return new Triple<>(first, second, third);
    }

    public F getFirst()
    {
        return this.first;
    }

    public S getSecond()
    {
        return this.second;
    }

    public T getThird()
    {
        return this.third;
    }

    @Override
    public int compareTo(Triple<F, S, T> t)
    {
        if(this.first == null)
            return t.first == null ? 0 : -1;

        int comparedFirst = this.first.compareTo(t.first);

        if(comparedFirst != 0)
            return comparedFirst;

        if(this.second == null)
            return t.getSecond() == null ? 0 : -1;

        int comparedSecond = this.second.compareTo(t.second);

        if(comparedSecond != 0)
            return comparedSecond;

        if(this.third == null)
            return t.getThird() == null ? 0 : -1;

        return this.third.compareTo(t.third);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null)
            return false;

        if(!(obj instanceof Triple))
            return false;

        Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;

        if(this.first == null && other.first == null || this.second == null && other.second == null
           || this.third == null && other.third == null)
            return true;

        return this.first != null && this.first.equals(other.first) && this.second != null
               && this.second.equals(other.second) && this.third != null && this.third.equals(
            other.third);
    }

    @Override
    public int hashCode()
    {
        final int prime = 37;
        int result = 1;

        result = prime * result + (first == null ? 0 : first.hashCode());
        result = prime * result + (second == null ? 0 : second.hashCode());
        result = prime * result + (third == null ? 0 : third.hashCode());

        return result;
    }

    @Override
    public String toString()
    {
        String firstString = first == null ? "null" : first.toString();
        String secondString = second == null ? "null" : second.toString();
        String thirdString = third == null ? "null" : third.toString();

        return "(" + firstString + ", " + secondString + ", " + thirdString + ")";
    }
}
