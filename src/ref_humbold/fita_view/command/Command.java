package ref_humbold.fita_view.command;

import java.util.EventObject;

public class Command<T>
    extends EventObject
{
    private static final long serialVersionUID = -2769967989094098324L;

    private String name;
    private T param;

    public Command(Object source, String name, T param)
    {
        super(source);

        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("null command name");

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
}
