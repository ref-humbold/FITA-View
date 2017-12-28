package ref_humbold.fita_view.command;

public class MultiParamsCommand<T>
    extends Command<Iterable<T>>
{
    private static final long serialVersionUID = -8305141235822968500L;

    public MultiParamsCommand(Object source, String name, Iterable<T> params)
    {
        super(source, name, params);
    }
}
