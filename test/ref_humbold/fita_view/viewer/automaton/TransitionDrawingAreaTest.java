package ref_humbold.fita_view.viewer.automaton;

import java.util.Collections;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.*;
import ref_humbold.fita_view.messaging.Message;

@RunWith(MockitoJUnitRunner.class)
public class TransitionDrawingAreaTest
{
    @Mock
    private Pointer<TreeAutomaton> mockPointer;

    @Mock
    private TreeAutomaton mockAutomaton;

    @Mock
    private Message<Void> mockSignal;

    @Mock
    private Message<Triple<NodeInfoSource, String, Map<Variable, String>>> mockMessage;

    @InjectMocks
    private TransitionDrawingArea testObject;

    @Before
    public void setUp()
    {
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testReceiveSignalWhenSourcePointer()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        testObject.receiveSignal(mockSignal);

        Assert.assertNull(testObject.parentInfo);
        Assert.assertNull(testObject.leftSonInfo);
        Assert.assertNull(testObject.rightSonInfo);
    }

    @Test
    public void testReceiveSignalWhenSourceMode()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        testObject.receiveSignal(mockSignal);

        Assert.assertNull(testObject.parentInfo);
        Assert.assertNull(testObject.leftSonInfo);
        Assert.assertNull(testObject.rightSonInfo);
    }

    @Test
    public void testReceiveMessageWhenLeftSon()
    {
        Triple<NodeInfoSource, String, Map<Variable, String>> param = Triple.make(
            NodeInfoSource.LEFT_SON, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);

        testObject.receiveMessage(mockMessage);

        Assert.assertNotNull(testObject.leftSonInfo);
        Assert.assertEquals(Pair.make(param.getSecond(), param.getThird()), testObject.leftSonInfo);
        Assert.assertNull(testObject.parentInfo);
        Assert.assertNull(testObject.rightSonInfo);
    }

    @Test
    public void testReceiveMessageWhenParent()
    {
        Triple<NodeInfoSource, String, Map<Variable, String>> param = Triple.make(
            NodeInfoSource.PARENT, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);

        testObject.receiveMessage(mockMessage);

        Assert.assertNotNull(testObject.parentInfo);
        Assert.assertEquals(Pair.make(param.getSecond(), param.getThird()), testObject.parentInfo);
        Assert.assertNull(testObject.leftSonInfo);
        Assert.assertNull(testObject.rightSonInfo);
    }

    @Test
    public void testReceiveMessageWhenRightSon()
    {
        Triple<NodeInfoSource, String, Map<Variable, String>> param = Triple.make(
            NodeInfoSource.RIGHT_SON, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);

        testObject.receiveMessage(mockMessage);

        Assert.assertNotNull(testObject.rightSonInfo);
        Assert.assertEquals(Pair.make(param.getSecond(), param.getThird()),
                            testObject.rightSonInfo);
        Assert.assertNull(testObject.parentInfo);
        Assert.assertNull(testObject.leftSonInfo);
    }
}
