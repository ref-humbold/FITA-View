package fitaview.utils;

import java.util.Objects;

public final class Pair<F, S>
{
    private final F first;
    private final S second;

    public Pair(F first, S second)
    {
        this.first = first;
        this.second = second;
    }

    public F getFirst()
    {
        return first;
    }

    public S getSecond()
    {
        return second;
    }

    public static <F, S> Pair<F, S> make(F first, S second)
    {
        return new Pair<>(first, second);
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

        if(!(obj instanceof Pair<?, ?> other))
            return false;

        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

    @Override
    public String toString()
    {
        return String.format("(%s, %s)", Objects.toString(first), Objects.toString(second));
    }
}
