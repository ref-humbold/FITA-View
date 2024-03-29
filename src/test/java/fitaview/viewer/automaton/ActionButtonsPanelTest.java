package fitaview.viewer.automaton;

import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fitaview.automaton.AutomatonRunningMode;
import fitaview.automaton.AutomatonRunningModeSender;
import fitaview.automaton.BottomUpAutomaton;
import fitaview.automaton.InfiniteTreeAutomaton;
import fitaview.automaton.TreeAutomaton;
import fitaview.messaging.Message;
import fitaview.utils.Pointer;
import fitaview.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class ActionButtonsPanelTest
{
    @Mock private Pointer<TreeAutomaton> mockPointer;

    @Mock private TreeAutomaton mockAutomaton;

    @Mock private BottomUpAutomaton mockBottomUp;

    @Mock private InfiniteTreeAutomaton mockInfinite;

    @Mock private ActionEvent mockActionEvent;

    @Mock private Message<Void> mockMessage;

    @InjectMocks private ActionButtonsPanel testObject;

    @Before
    public void setUp()
    {
        PowerMockito.mockStatic(UserMessageBox.class);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testActionPerformedWhenRun()
    {
        try
        {
            Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RUN");
            Mockito.doNothing().when(mockAutomaton).run();

            testObject.actionPerformed(mockActionEvent);

            Mockito.verify(mockAutomaton, Mockito.times(1)).run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenStepForward()
    {
        try
        {
            Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("STEP FORWARD");
            Mockito.doNothing().when(mockAutomaton).makeStepForward();

            testObject.actionPerformed(mockActionEvent);

            Mockito.verify(mockAutomaton, Mockito.times(1)).makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenStopTraversing()
    {
        try
        {
            Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("STOP TRAVERSING");
            Mockito.doNothing().when(mockAutomaton).makeStepForward();

            testObject.actionPerformed(mockActionEvent);

            Mockito.verify(mockAutomaton, Mockito.times(1)).stopTraversing();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenContinueRun()
    {
        try
        {
            Mockito.when(mockPointer.get()).thenReturn(mockInfinite);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CONTINUE RUN");
            Mockito.doNothing().when(mockInfinite).continueRecursive();
            Mockito.doNothing().when(mockInfinite).run();

            testObject.actionPerformed(mockActionEvent);

            InOrder order = Mockito.inOrder(mockInfinite);

            order.verify(mockInfinite, Mockito.times(1)).continueRecursive();
            order.verify(mockInfinite, Mockito.times(1)).run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenContinueStepForward()
    {
        try
        {
            Mockito.when(mockPointer.get()).thenReturn(mockInfinite);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CONTINUE STEP FORWARD");
            Mockito.doNothing().when(mockInfinite).continueRecursive();
            Mockito.doNothing().when(mockInfinite).makeStepForward();

            testObject.actionPerformed(mockActionEvent);

            InOrder order = Mockito.inOrder(mockInfinite);

            order.verify(mockInfinite, Mockito.times(1)).continueRecursive();
            order.verify(mockInfinite, Mockito.times(1)).makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenCheckEmptinessIsTrue()
    {
        try
        {
            Object[] result = new Object[2];

            Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CHECK EMPTINESS");
            Mockito.when(mockBottomUp.checkEmptiness()).thenReturn(true);
            PowerMockito.doAnswer((Answer<Void>)invocation -> {
                            result[0] = invocation.getArguments()[0];
                            result[1] = invocation.getArguments()[1];
                            return null;
                        })
                        .when(UserMessageBox.class, "showWarning", ArgumentMatchers.anyString(),
                              ArgumentMatchers.anyString());

            testObject.actionPerformed(mockActionEvent);

            Assert.assertEquals("AUTOMATON IS EMPTY", result[0]);
            Assert.assertEquals("No tree can be accepted by the automaton", result[1]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testActionPerformedWhenCheckEmptinessIsFalse()
    {
        try
        {
            Object[] result = new Object[2];

            Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CHECK EMPTINESS");
            Mockito.when(mockBottomUp.checkEmptiness()).thenReturn(false);
            PowerMockito.doAnswer((Answer<Void>)invocation -> {
                            result[0] = invocation.getArguments()[0];
                            result[1] = invocation.getArguments()[1];
                            return null;
                        })
                        .when(UserMessageBox.class, "showInfo", ArgumentMatchers.anyString(),
                              ArgumentMatchers.anyString());

            testObject.actionPerformed(mockActionEvent);

            Assert.assertEquals("AUTOMATON IS NON-EMPTY", result[0]);
            Assert.assertEquals("The automaton can accept at least one tree", result[1]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testReceiveSignalWhenSourcePointerIsNotEmpty()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
        Mockito.when(mockMessage.getSource()).thenReturn(mockPointer);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.RUN, testObject.buttonsType);
        Mockito.verify(mockPointer, Mockito.times(1)).get();
    }

    @Test
    public void testReceiveSignalWhenSourceRunningModeAndPointerIsEmpty()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(true);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.NONE, testObject.buttonsType);
    }

    @Test
    public void testReceiveSignalWhenSourceRunningModeAndRunning()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.RUNNING);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.RUN, testObject.buttonsType);
    }

    @Test
    public void testReceiveSignalWhenSourceRunningModeAndStopped()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.RUN, testObject.buttonsType);
    }

    @Test
    public void testReceiveSignalWhenSourceRunningModeAndFinished()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.RUN, testObject.buttonsType);
    }

    @Test
    public void testReceiveSignalWhenSourceRunningModeAndContinuing()
    {
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.CONTINUING);

        testObject.receiveSignal(mockMessage);

        Assert.assertEquals(ActionButtonsPanel.ButtonsType.CONTINUE, testObject.buttonsType);
    }
}
