package ref_humbold.fita_view.message;

public interface MessageSender
{
    void addReceiver(MessageReceiver receiver);

    void removeReceiver(MessageReceiver receiver);

    void send(Message<Void> message);
}
