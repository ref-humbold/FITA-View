package ref_humbold.fita_view.messaging;

import java.util.Objects;

public class Message<T>
{
    private Object source;
    private T param;

    public Message(Object source)
    {
        this(source, null);
    }

    public Message(Object source, T param)
    {
        if(source == null)
            throw new IllegalArgumentException("Source is null!");

        this.source = source;
        this.param = param;
    }

    public Object getSource()
    {
        return this.source;
    }

    public T getParam()
    {
        return this.param;
    }

    @Override
    public String toString()
    {
        return "MESSAGE from " + source.getClass().getSimpleName() + ": \'" + Objects.toString(
            param) + "\'";
    }
}
