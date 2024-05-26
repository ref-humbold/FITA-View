package fitaview.messaging;

import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageSenderTest
{
    private DefaultMessageSender<String> testObject;

    @Mock private MessageSender<String> mockSender;

    @Mock private MessageReceiver<String> mockReceiver;

    @Before
    public void setUp()
    {
        testObject = new DefaultMessageSender<>();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testAddReceiver()
    {
        // given
        testObject.addReceiver(mockReceiver);
        // when
        Set<MessageReceiver<String>> result = testObject.receivers;
        // then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result).containsExactly(mockReceiver);
    }

    @Test
    public void testAddReceiverWhenDoubleAdd()
    {
        // given
        testObject.addReceiver(mockReceiver);
        testObject.addReceiver(mockReceiver);
        // when
        Set<MessageReceiver<String>> result = testObject.receivers;
        // then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result).containsExactly(mockReceiver);
    }

    @Test
    public void testRemoveReceiver()
    {
        // given
        testObject.addReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);
        // when
        Set<MessageReceiver<String>> result = testObject.receivers;
        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testRemoveReceiverWhenDoubleRemove()
    {
        // given
        testObject.addReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);
        // when
        Set<MessageReceiver<String>> result = testObject.receivers;
        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testRemoveReceiverWhenNotAdded()
    {
        testObject.removeReceiver(mockReceiver);
        // when
        Set<MessageReceiver<String>> result = testObject.receivers;
        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSend()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveMessage(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.send("PARAMETER");
        // when
        Message<String> result = (Message<String>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(testObject);
        Assertions.assertThat(result.getParam()).isEqualTo("PARAMETER");
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'PARAMETER'".formatted(
                          testObject.getClass().getSimpleName()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendWhenNullParam()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveMessage(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.send(null);
        // when
        Message<String> result = (Message<String>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(testObject);
        Assertions.assertThat(result.getParam()).isNull();
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'null'".formatted(
                          testObject.getClass().getSimpleName()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendMessage()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveMessage(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.sendMessage(new Message<>(mockSender, "PARAMETER"));
        // when
        Message<String> result = (Message<String>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(mockSender);
        Assertions.assertThat(result.getParam()).isEqualTo("PARAMETER");
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'PARAMETER'".formatted(
                          mockSender.getClass().getSimpleName()));
    }

    @Test
    public void testSendMessageWhenNullSource()
    {
        // when
        Exception exception = Assertions.catchException(
                () -> testObject.sendMessage(new Message<>(null, "PARAMETER")));
        // then
        Assertions.assertThat(exception).isInstanceOf(NullPointerException.class);
    }
}
