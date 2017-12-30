package ref_humbold.fita_view.message;

public interface ParameterizedMessageSender<T>
{
    void addReceiver(ParameterizedMessageReceiver<T> receiver);

    void removeReceiver(ParameterizedMessageReceiver<T> receiver);

    void send(Message<T> message);
}
