package ref_humbold.fita_view.message;

public interface ParameterizedMessageReceiver<T>
{
    void receiveParameterized(Message<T> message);
}
