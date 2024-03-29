package fitaview.messaging;

import java.util.HashSet;
import java.util.Set;

public class DefaultSignalSender
        implements SignalSender
{
    final Set<SignalReceiver> receivers = new HashSet<>();

    @Override
    public void addReceiver(SignalReceiver receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(SignalReceiver receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void sendSignal(Message<Void> signal)
    {
        receivers.forEach(r -> r.receiveSignal(signal));
    }
}
