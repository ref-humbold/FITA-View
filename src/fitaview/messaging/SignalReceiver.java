package fitaview.messaging;

public interface SignalReceiver
{
    void receiveSignal(Message<Void> signal);
}
