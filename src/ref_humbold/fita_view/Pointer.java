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

    /**
     * @return {@code true} if pointer contains no element, otherwise {@code false}
     */
    public boolean isEmpty()
    {
        return element == null;
    }

    /**
     * @return the element
     */
    public T get()
    {
        return this.element;
    }

    /**
     * Setting a new element and informing receivers about the change.
     * @param element new element
     */
    public void set(T element)
    {
        this.element = element;
        send();
    }

    /**
     * Removing the element and informing receivers about the change.
     */
    public void delete()
    {
        set(null);
    }
}
