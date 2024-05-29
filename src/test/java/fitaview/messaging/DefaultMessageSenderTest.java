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
    public void addReceiver_ThenAdded()
    {
        // when
        testObject.addReceiver(mockReceiver);
        // then
        Set<MessageReceiver<String>> receivers = testObject.receivers;

        Assertions.assertThat(receivers).hasSize(1);
        Assertions.assertThat(receivers).containsExactly(mockReceiver);
    }

    @Test
    public void addReceiver_WhenDoubleAdd_ThenNoChanges()
    {
        // given
        testObject.addReceiver(mockReceiver);
        // when
        testObject.addReceiver(mockReceiver);
        // then
        Set<MessageReceiver<String>> receivers = testObject.receivers;

        Assertions.assertThat(receivers).hasSize(1);
        Assertions.assertThat(receivers).containsExactly(mockReceiver);
    }

    @Test
    public void removeReceiver_ThenRemoved()
    {
        // given
        testObject.addReceiver(mockReceiver);
        // when
        testObject.removeReceiver(mockReceiver);
        // then
        Assertions.assertThat(testObject.receivers).isEmpty();
    }

    @Test
    public void removeReceiver_WhenDoubleRemove_ThenNoChanges()
    {
        // given
        testObject.addReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);
        // when
        testObject.removeReceiver(mockReceiver);
        // then
        Assertions.assertThat(testObject.receivers).isEmpty();
    }

    @Test
    public void removeReceiver_WhenNotAdded_ThenNoChanges()
    {
        // when
        testObject.removeReceiver(mockReceiver);
        // then
        Assertions.assertThat(testObject.receivers).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void send_ThenReceivedFromSender()
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
    public void sendMessage_WhenSource_ThenReceivedFromSource()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveMessage(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.sendMessage(new Message<>(mockSender, null));
        // when
        Message<String> result = (Message<String>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(mockSender);
        Assertions.assertThat(result.getParam()).isNull();
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'null'".formatted(
                          mockSender.getClass().getSimpleName()));
    }

    @Test
    public void sendMessage_WhenNullSource_ThenNullPointerException()
    {
        Assertions.assertThatThrownBy(
                          () -> testObject.sendMessage(new Message<>(null, "PARAMETER")))
                  .isInstanceOf(NullPointerException.class);
    }
}
