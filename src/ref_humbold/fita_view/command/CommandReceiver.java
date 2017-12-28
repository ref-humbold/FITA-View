package ref_humbold.fita_view.command;

public interface CommandReceiver<T>
{
    void receive(Command<T> command);
}
