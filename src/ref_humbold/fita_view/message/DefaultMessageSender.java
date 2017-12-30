package ref_humbold.fita_view.message;

import java.util.HashSet;
import java.util.Set;

public class DefaultMessageSender
    implements MessageSender
{
    private Set<MessageReceiver> receivers = new HashSet<>();

    @Override
    public void addReceiver(MessageReceiver receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(MessageReceiver receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void send(Message<Void> message)
    {
        for(MessageReceiver r : receivers)
            r.receive(message);
    }
}
