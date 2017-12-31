package ref_humbold.fita_view;

import ref_humbold.fita_view.message.DefaultMessageSender;
import ref_humbold.fita_view.message.Message;

public class Pointer<T>
    extends DefaultMessageSender
{
    private final T nullElement;
    private T element;

    public Pointer()
    {
        this(null);
    }

    public Pointer(T nullElement)
    {
        super();

        this.nullElement = nullElement;
        this.element = nullElement;
    }

    public T get()
    {
        return this.element;
    }

    public void set(T element)
    {
        this.element = element == null ? nullElement : element;
        this.send(new Message<>(this));
    }

    public void delete()
    {
        set(null);
    }
}
