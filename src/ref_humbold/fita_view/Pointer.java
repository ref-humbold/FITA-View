package ref_humbold.fita_view;

import ref_humbold.fita_view.message.DefaultMessageSender;
import ref_humbold.fita_view.message.Message;

public class Pointer<T>
    extends DefaultMessageSender
{
    private T element;

    public Pointer()
    {
        this(null);
    }

    public Pointer(T element)
    {
        super();

        this.element = element;
    }

    public T get()
    {
        return this.element;
    }

    public void set(T element)
    {
        this.element = element;
        send(new Message<>(this));
    }

    public void delete()
    {
        set(null);
    }
}
