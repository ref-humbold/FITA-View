package refhumbold.fitaview.messaging;

public interface MessageReceiver<T>
{
    void receiveMessage(Message<T> message);
}
