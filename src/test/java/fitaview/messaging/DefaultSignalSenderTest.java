package fitaview.messaging;

import java.util.Set;
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
    public void testAddReceiver()
    {
        // given
        testObject.addReceiver(mockReceiver);
        // when
        Set<SignalReceiver> result = testObject.receivers;
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
        Set<SignalReceiver> result = testObject.receivers;
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
        Set<SignalReceiver> result = testObject.receivers;
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
        Set<SignalReceiver> result = testObject.receivers;
        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void testRemoveReceiverWhenNotAdded()
    {
        // given
        testObject.removeReceiver(mockReceiver);
        // when
        Set<SignalReceiver> result = testObject.receivers;
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
        }).when(mockReceiver).receiveSignal(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.send();
        // when
        Message<Void> result = (Message<Void>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(testObject);
        Assert.assertNull(result.getParam());
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'null'".formatted(
                          testObject.getClass().getSimpleName()));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendSignal()
    {
        // given
        Object[] arg = new Object[1];

        Mockito.doAnswer((Answer<Void>)invocation -> {
            arg[0] = invocation.getArguments()[0];
            return null;
        }).when(mockReceiver).receiveSignal(ArgumentMatchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.sendSignal(new Message<>(mockSender));
        // when
        Message<Void> result = (Message<Void>)arg[0];
        // then
        Assertions.assertThat(result.getSource()).isSameAs(mockSender);
        Assertions.assertThat(result.getParam()).isNull();
        Assertions.assertThat(result.toString())
                  .isEqualTo("MESSAGE from %s: 'null'".formatted(
                          mockSender.getClass().getSimpleName()));
    }

    @Test
    public void testSendSignalWhenNullSource()
    {
        // when
        Exception exception =
                Assertions.catchException(() -> testObject.sendSignal(new Message<>(null)));
        // then
        Assertions.assertThat(exception).isInstanceOf(NullPointerException.class);
    }
}
