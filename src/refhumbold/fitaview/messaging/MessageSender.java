package refhumbold.fitaview.messaging;

public interface MessageSender<T>
{
    void addReceiver(MessageReceiver<T> receiver);

    void removeReceiver(MessageReceiver<T> receiver);

    void sendMessage(Message<T> message);

    default void send(T param)
    {
        this.sendMessage(new Message<>(this, param));
    }
}
