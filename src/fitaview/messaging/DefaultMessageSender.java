package fitaview.messaging;

import java.util.HashSet;
import java.util.Set;

public class DefaultMessageSender<T>
    implements MessageSender<T>
{
    Set<MessageReceiver<T>> receivers = new HashSet<>();

    @Override
    public void addReceiver(MessageReceiver<T> receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(MessageReceiver<T> receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void sendMessage(Message<T> message)
    {
        receivers.forEach(r -> r.receiveMessage(message));
    }
}
