package ref_humbold.fita_view.command;

import java.util.HashSet;
import java.util.Set;

public class DefaultCommandSender<T>
    implements CommandSender<T>
{
    private Set<CommandReceiver<T>> receivers = new HashSet<>();

    @Override
    public void addReceiver(CommandReceiver<T> receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(CommandReceiver<T> receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void send(Command<T> command)
    {
        for(CommandReceiver<T> r : receivers)
            r.receive(command);
    }
}
