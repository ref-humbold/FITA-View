package fitaview.messaging;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class DefaultSignalSenderTest
{
    private DefaultSignalSender testObject;

    @Mock private SignalSender mockSender;

    @Mock private SignalReceiver mockReceiver;

    @Before
    public void setUp()
    {
        testObject = new DefaultSignalSender();
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
        Assertions.assertThat(testObject.receivers).containsExactly(mockReceiver);
    }

    @Test
    public void addReceiver_WhenDoubleAdd_ThenNoChanges()
    {
        // given
        testObject.addReceiver(mockReceiver);
        // when
        testObject.addReceiver(mockReceiver);
        // then
        Assertions.assertThat(testObject.receivers).containsExactly(mockReceiver);
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
        }).when(mockReceiver).receiveSignal(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        // when
        testObject.send();
        // then
        Message<Void> message = (Message<Void>)arg[0];

        Assertions.assertThat(message.getSource()).isSameAs(testObject);
        Assert.assertNull(message.getParam());
        Assertions.assertThat(message)
                  .hasToString("MESSAGE from %s: 'null'".formatted(
                          testObject.getClass().getSimpleName()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void sendSignal_WhenSource_ThenReceivedFromSource()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveSignal(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        // when
        testObject.sendSignal(new Message<>(mockSender));
        // then
        Message<Void> message = (Message<Void>)arg[0];

        Assertions.assertThat(message.getSource()).isSameAs(mockSender);
        Assertions.assertThat(message.getParam()).isNull();
        Assertions.assertThat(message)
                  .hasToString("MESSAGE from %s: 'null'".formatted(
                          mockSender.getClass().getSimpleName()));
    }

    @Test
    public void sendSignal_WhenNullSource_ThenNullPointerException()
    {
        Assertions.assertThatThrownBy(() -> testObject.sendSignal(new Message<>(null)))
                  .isInstanceOf(NullPointerException.class);
    }
}
