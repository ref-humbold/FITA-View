package ref_humbold.fita_view.messaging;

public interface MessageReceiver<T>
{
    void receiveMessage(Message<T> message);
}
