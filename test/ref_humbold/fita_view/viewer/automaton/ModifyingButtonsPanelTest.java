package ref_humbold.fita_view.viewer.automaton;

import java.awt.event.ActionEvent;
import java.util.Collections;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.BottomUpDFTA;
import ref_humbold.fita_view.automaton.TopDownDFTA;
import ref_humbold.fita_view.automaton.TopDownNFTA;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.nondeterminism.*;
import ref_humbold.fita_view.automaton.traversing.*;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class ModifyingButtonsPanelTest
{
    @Mock
    private Pointer<TreeAutomaton> mockPointer;

    @Mock
    private ActionEvent mockActionEvent;

    @Mock
    private Message<Void> mockMessage;

    @InjectMocks
    private ModifyingButtonsPanel testObject;

    @Before
    public void setUp()
    {
        PowerMockito.mockStatic(UserMessageBox.class);
        Mockito.when(mockMessage.getSource()).thenReturn(mockPointer);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test(expected = IncorrectTraversingException.class)
    public void testActionPerformedWhenBottomUpDFS()
        throws Exception
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new BottomUpDFTA(Collections.emptySet(), Collections.emptySet()));
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");
        PowerMockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation)
                throws Exception
            {
                throw (Exception)invocation.getArguments()[0];
            }
        }).when(UserMessageBox.class, "showException", Matchers.any(Exception.class));

        testObject.actionPerformed(mockActionEvent);
    }

    @Test
    public void testActionPerformedWhenBottomUpBFS()
    {
        BottomUpDFTA automaton = new BottomUpDFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("BFS");

        testObject.actionPerformed(mockActionEvent);

        TreeTraversing result = automaton.getTraversing();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpBFS);
    }

    @Test
    public void testActionPerformedWhenBottomUpLevel()
    {
        BottomUpDFTA automaton = new BottomUpDFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEVEL");

        testObject.actionPerformed(mockActionEvent);

        TreeTraversing result = automaton.getTraversing();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpLevel);
    }

    @Test
    public void testActionPerformedWhenTopDownDFS()
    {
        TopDownDFTA automaton = new TopDownDFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");

        testObject.actionPerformed(mockActionEvent);

        TreeTraversing result = automaton.getTraversing();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFS);
    }

    @Test
    public void testActionPerformedWhenTopDownBFS()
    {
        TopDownDFTA automaton = new TopDownDFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("BFS");

        testObject.actionPerformed(mockActionEvent);

        TreeTraversing result = automaton.getTraversing();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownBFS);
    }

    @Test
    public void testActionPerformedWhenTopDownLevel()
    {
        TopDownDFTA automaton = new TopDownDFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEVEL");

        testObject.actionPerformed(mockActionEvent);

        TreeTraversing result = automaton.getTraversing();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownLevel);
    }

    @Test
    public void testActionPerformedWhenFirstChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("FIRST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice<Pair<String, String>, Pair<String, String>> result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof FirstElementChoice);
    }

    @Test
    public void testActionPerformedWhenRandomChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RANDOM");

        testObject.actionPerformed(mockActionEvent);

        StateChoice<Pair<String, String>, Pair<String, String>> result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof RandomChoice);
    }

    @Test
    public void testActionPerformedWhenLeastChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEAST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice<Pair<String, String>, Pair<String, String>> result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof LeastHashCodeChoice);
    }

    @Test
    public void testActionPerformedWhenGreatestChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("GREATEST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice<Pair<String, String>, Pair<String, String>> result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof GreatestHashCodeChoice);
    }

    @Test
    public void testReceiveSignalWhenNullAutomaton()
    {
        Mockito.when(mockPointer.get()).thenReturn(null);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(2, testObject.getComponentCount());
    }

    @Test
    public void testReceiveSignalWhenBottomUpDFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new BottomUpDFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(3, testObject.getComponentCount());
    }

    @Test
    public void testReceiveSignalWhenTopDownDFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownDFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(3, testObject.getComponentCount());
    }

    @Test
    public void testReceiveSignalWhenTopDownNFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownNFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(5, testObject.getComponentCount());
    }
}
