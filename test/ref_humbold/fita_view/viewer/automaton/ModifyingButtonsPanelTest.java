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

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.*;
import ref_humbold.fita_view.automaton.nondeterminism.*;
import ref_humbold.fita_view.automaton.traversing.*;
import ref_humbold.fita_view.message.Message;
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
        PowerMockito.doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation)
                throws Throwable
            {
                throw (Exception)invocation.getArguments()[0];
            }
        }).when(UserMessageBox.class, "showException", Matchers.any(Exception.class));
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");

        try
        {
            testObject.actionPerformed(mockActionEvent);
        }
        catch(Exception e)
        {
            Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
            Mockito.verify(mockPointer, Mockito.times(2)).get();

            throw e;
        }
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
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testActionPerformedWhenFirstChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("FIRST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof FirstElementChoice);
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testActionPerformedWhenRandomChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RANDOM");

        testObject.actionPerformed(mockActionEvent);

        StateChoice result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof RandomChoice);
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testActionPerformedWhenLeastChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEAST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof LeastHashCodeChoice);
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testActionPerformedWhenGreatestChoice()
    {
        TopDownNFTA automaton = new TopDownNFTA(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("GREATEST");

        testObject.actionPerformed(mockActionEvent);

        StateChoice result = automaton.getChoice();

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof GreatestHashCodeChoice);
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testReceiveWhenNullAutomaton()
    {
        Mockito.when(mockPointer.get()).thenReturn(NullAutomaton.getInstance());

        testObject.receive(mockMessage);

        Assert.assertEquals(2, testObject.getComponentCount());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testReceiveWhenBottomUpDFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new BottomUpDFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receive(mockMessage);

        Assert.assertEquals(3, testObject.getComponentCount());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testReceiveWhenTopDownDFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownDFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receive(mockMessage);

        Assert.assertEquals(3, testObject.getComponentCount());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }

    @Test
    public void testReceiveWhenTopDownNFTA()
    {
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownNFTA(Collections.emptySet(), Collections.emptySet()));

        testObject.receive(mockMessage);

        Assert.assertEquals(5, testObject.getComponentCount());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }
}
