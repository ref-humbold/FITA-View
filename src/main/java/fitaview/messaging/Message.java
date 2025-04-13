package fitaview.messaging;

import java.util.Objects;

public class Message<T>
{
    private final Object source;
    private final T param;

    public Message(Object source)
    {
        this(source, null);
    }

    public Message(Object source, T param)
    {
        this.source = Objects.requireNonNull(source);
        this.param = param;
    }

    public Object getSource()
    {
        return source;
    }

    public T getParam()
    {
        return param;
    }

    @Override
    public String toString()
    {
        return String.format("MESSAGE from %s: '%s'", source.getClass().getSimpleName(),
                             Objects.toString(param));
    }
}
