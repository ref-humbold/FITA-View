package ref_humbold.fita_view;

import java.util.Objects;

public class Pair<F, S>
{
    private final F first;
    private final S second;

    public Pair(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> make(F first, S second)
    {
        return new Pair<>(first, second);
    }

    public F getFirst()
    {
        return first;
    }

    public S getSecond()
    {
        return second;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || !(obj instanceof Pair))
            return false;

        Pair<?, ?> other = (Pair<?, ?>)obj;

        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
    }

    @Override
    public int hashCode()
    {
        int prime = 37;
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
