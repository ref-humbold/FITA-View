package ref_humbold.fita_view.message;

import java.util.HashSet;
import java.util.Set;

public class DefaultParameterizedMessageSender<T>
    implements ParameterizedMessageSender<T>
{
    private Set<ParameterizedMessageReceiver<T>> receivers = new HashSet<>();

    @Override
    public void addReceiver(ParameterizedMessageReceiver<T> receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(ParameterizedMessageReceiver<T> receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void send(Message<T> message)
    {
        for(ParameterizedMessageReceiver<T> r : receivers)
            r.receiveParameterized(message);
    }
}
