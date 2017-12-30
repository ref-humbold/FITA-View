package ref_humbold.fita_view.message;

import java.util.EventObject;

public class Message<T>
    extends EventObject
{
    private static final long serialVersionUID = -2769967989094098324L;

    private String name;
    private T param;

    public Message(Object source, String name, T param)
    {
        super(source);

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Message name is null or empty.");

        this.name = name;
        this.param = param;
    }

    public String getName()
    {
        return this.name;
    }

    public T getParam()
    {
        return this.param;
    }

    @Override
    public String toString()
    {
        return "MESSAGE:\n  source = " + source.getClass().getSimpleName() + "\n  name = \"" + name
            + "\"\n  parameter = " + param.toString();
    }
}
