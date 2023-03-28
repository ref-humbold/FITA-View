package fitaview.messaging;

public interface SignalSender
{
    void addReceiver(SignalReceiver receiver);

    void removeReceiver(SignalReceiver receiver);

    void sendSignal(Message<Void> signal);

    default void send()
    {
        sendSignal(new Message<>(this));
    }
}
