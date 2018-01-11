package ref_humbold.fita_view;

import ref_humbold.fita_view.messaging.DefaultSignalSender;

public class Pointer<T>
    extends DefaultSignalSender
{
    private T element = null;

    public Pointer()
    {
        super();
    }

    public T get()
    {
        return this.element;
    }

    public void set(T element)
    {
        this.element = element;
        send();
    }

    public void delete()
    {
        set(null);
    }
}
