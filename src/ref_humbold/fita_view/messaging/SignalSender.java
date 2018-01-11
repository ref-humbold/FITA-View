package ref_humbold.fita_view.messaging;

public interface SignalSender
{
    void addReceiver(SignalReceiver receiver);

    void removeReceiver(SignalReceiver receiver);

    void sendSignal(Message<Void> signal);

    default void send()
    {
        this.sendSignal(new Message<>(this));
    }
}
