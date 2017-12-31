package ref_humbold.fita_view.message;

import java.util.EventObject;

public class Message<T>
    extends EventObject
{
    private static final long serialVersionUID = -2769967989094098324L;

    private T param;

    public Message(Object source)
    {
        this(source, null);
    }

    public Message(Object source, T param)
    {
        super(source);

        this.param = param;
    }

    public T getParam()
    {
        return this.param;
    }

    @Override
    public String toString()
    {
        return "MESSAGE:\n  source = " + source.getClass().getSimpleName() + "\n  parameter type = "
            + param.getClass().getSimpleName() + "\n  parameter value = " + param.toString();
    }
}
