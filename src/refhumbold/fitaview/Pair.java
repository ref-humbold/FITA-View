package refhumbold.fitaview;

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

    public int size()
    {
        return 2;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second);
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
    public String toString()
    {
        return "(" + Objects.toString(first) + ", " + Objects.toString(second) + ")";
    }
}
