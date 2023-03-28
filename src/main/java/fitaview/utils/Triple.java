package fitaview.utils;

import java.util.Objects;

public final class Triple<F, S, T>
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

    public static <F, S, T> Triple<F, S, T> make(F first, S second, T third)
    {
        return new Triple<>(first, second, third);
    }

    public F getFirst()
    {
        return first;
    }

    public S getSecond()
    {
        return second;
    }

    public T getThird()
    {
        return third;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(first, second, third);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(!(o instanceof Triple))
            return false;

        Triple<?, ?, ?> other = (Triple<?, ?, ?>)o;

        return Objects.equals(first, other.first) && Objects.equals(second, other.second)
                && Objects.equals(third, other.third);
    }

    @Override
    public String toString()
    {
        return String.format("(%s, %s, %s)", Objects.toString(first), Objects.toString(second),
                             Objects.toString(third));
    }
}
