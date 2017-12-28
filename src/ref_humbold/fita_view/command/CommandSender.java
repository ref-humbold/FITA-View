package ref_humbold.fita_view.command;

public interface CommandSender<T>
{
    void addReceiver(CommandReceiver<T> receiver);

    void removeReceiver(CommandReceiver<T> receiver);

    void send(Command<T> command);
}
