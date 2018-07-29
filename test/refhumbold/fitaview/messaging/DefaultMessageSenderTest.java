package refhumbold.fitaview.messaging;

import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageSenderTest
{
    private DefaultMessageSender<String> testObject;

    @Mock
    private MessageSender<String> mockSender;

    @Mock
    private MessageReceiver<String> mockReceiver;

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
        testObject.addReceiver(mockReceiver);

        Set<MessageReceiver<String>> result = testObject.receivers;

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(mockReceiver));
    }

    @Test
    public void testAddReceiverWhenDoubleAdd()
    {
        testObject.addReceiver(mockReceiver);
        testObject.addReceiver(mockReceiver);

        Set<MessageReceiver<String>> result = testObject.receivers;

        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(mockReceiver));
    }

    @Test
    public void testRemoveReceiver()
    {
        testObject.addReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);

        Set<MessageReceiver<String>> result = testObject.receivers;

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testRemoveReceiverWhenDoubleRemove()
    {
        testObject.addReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);
        testObject.removeReceiver(mockReceiver);

        Set<MessageReceiver<String>> result = testObject.receivers;

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testRemoveReceiverWhenNotAdded()
    {
        testObject.removeReceiver(mockReceiver);

        Set<MessageReceiver<String>> result = testObject.receivers;

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSend()
    {
        Object[] arg = new Object[1];

        Mockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation)
            {
                arg[0] = invocation.getArguments()[0];
                return null;
            }
        }).when(mockReceiver).receiveMessage(Matchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.send("PARAMETER");

        Message<String> result = (Message<String>)arg[0];
        String expected = "MESSAGE from " + testObject.getClass().getSimpleName()
            + ": \'PARAMETER\'";

        Assert.assertSame(testObject, result.getSource());
        Assert.assertEquals("PARAMETER", result.getParam());
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendWhenNullParam()
    {
        Object[] arg = new Object[1];

        Mockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation)
            {
                arg[0] = invocation.getArguments()[0];
                return null;
            }
        }).when(mockReceiver).receiveMessage(Matchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.send(null);

        Message<String> result = (Message<String>)arg[0];
        String expected = "MESSAGE from " + testObject.getClass().getSimpleName() + ": \'null\'";

        Assert.assertSame(testObject, result.getSource());
        Assert.assertNull(result.getParam());
        Assert.assertEquals(expected, result.toString());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSendMessage()
    {
        Object[] arg = new Object[1];

        Mockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation)
            {
                arg[0] = invocation.getArguments()[0];
                return null;
            }
        }).when(mockReceiver).receiveMessage(Matchers.any());

        testObject.addReceiver(mockReceiver);
        testObject.sendMessage(new Message<>(mockSender, "PARAMETER"));

        Message<String> result = (Message<String>)arg[0];
        String expected = "MESSAGE from " + mockSender.getClass().getSimpleName()
            + ": \'PARAMETER\'";

        Assert.assertSame(mockSender, result.getSource());
        Assert.assertEquals("PARAMETER", result.getParam());
        Assert.assertEquals(expected, result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendMessageWhenNullSource()
    {
        testObject.sendMessage(new Message<>(null, "PARAMETER"));
    }
}
