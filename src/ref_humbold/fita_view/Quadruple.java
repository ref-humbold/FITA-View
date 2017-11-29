package ref_humbold.fita_view;

import java.util.Objects;

public class Quadruple<F, S, T, U>
    implements Tuple
{
    private final F first;
    private final S second;
    private final T third;
    private final U fourth;

    public Quadruple(F first, S second, T third, U fourth)
    {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static <F, S, T, U> Quadruple<F, S, T, U> make(F first, S second, T third, U fourth)
    {
        return new Quadruple<>(first, second, third, fourth);
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

    public U getFourth()
    {
        return fourth;
    }

    @Override
    public int getArity()
    {
        return 4;
    }

    @Override
    public Object[] toArray()
    {
        return new Object[]{first, second, third, fourth};
    }

    @Override
    public String toString()
    {
        String firstString = first == null ? "null" : first.toString();
        String secondString = second == null ? "null" : second.toString();
        String thirdString = third == null ? "null" : third.toString();
        String fourthString = fourth == null ? "null" : fourth.toString();

        return "(" + firstString + ", " + secondString + ", " + thirdString + ", " + fourthString
            + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || !(obj instanceof Quadruple))
            return false;

        Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>)obj;

        return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second)
            && Objects.equals(this.third, other.third) && Objects.equals(this.fourth, other.fourth);
    }

    @Override
    public int hashCode()
    {
        int prime = 37;
        int result = 1;

        result = prime * result + (first == null ? 0 : first.hashCode());
        result = prime * result + (second == null ? 0 : second.hashCode());
        result = prime * result + (third == null ? 0 : third.hashCode());
        result = prime * result + (fourth == null ? 0 : fourth.hashCode());

        return result;
    }
}
