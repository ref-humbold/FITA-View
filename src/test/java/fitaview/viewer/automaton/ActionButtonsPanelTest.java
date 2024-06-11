package fitaview.viewer.automaton;

import java.awt.event.ActionEvent;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
    public void actionPerformed_WhenRun_ThenCalledOnce()
            throws Exception
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RUN");
        Mockito.doNothing().when(mockAutomaton).run();

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Mockito.verify(mockAutomaton, Mockito.times(1)).run();
    }

    @Test
    public void actionPerformed_WhenStepForward_ThenCalledOnce()
            throws Exception
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("STEP FORWARD");
        Mockito.doNothing().when(mockAutomaton).makeStepForward();

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Mockito.verify(mockAutomaton, Mockito.times(1)).makeStepForward();
    }

    @Test
    public void actionPerformed_WhenStopTraversing_ThenCalledOnce()
            throws Exception
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("STOP TRAVERSING");
        Mockito.doNothing().when(mockAutomaton).makeStepForward();

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Mockito.verify(mockAutomaton, Mockito.times(1)).stopTraversing();
    }

    @Test
    public void actionPerformed_WhenContinueAndRun_ThenEachCalledOnce()
            throws Exception
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockInfinite);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CONTINUE RUN");
        Mockito.doNothing().when(mockInfinite).continueRecursive();
        Mockito.doNothing().when(mockInfinite).run();

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Mockito.inOrder(mockInfinite).verify(mockInfinite, Mockito.times(1)).continueRecursive();
        Mockito.inOrder(mockInfinite).verify(mockInfinite, Mockito.times(1)).run();
    }

    @Test
    public void actionPerformed_WhenContinueAndStepForward_ThenEachCalledOnce()
            throws Exception
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockInfinite);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CONTINUE STEP FORWARD");
        Mockito.doNothing().when(mockInfinite).continueRecursive();
        Mockito.doNothing().when(mockInfinite).makeStepForward();

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Mockito.inOrder(mockInfinite).verify(mockInfinite, Mockito.times(1)).continueRecursive();
        Mockito.inOrder(mockInfinite).verify(mockInfinite, Mockito.times(1)).makeStepForward();
    }

    @Test
    public void actionPerformed_WhenCheckEmptinessIsTrue_ThenWarning()
            throws Exception
    {
        // given
        Object[] warning = new Object[2];

        Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CHECK EMPTINESS");
        Mockito.when(mockBottomUp.checkEmptiness()).thenReturn(true);
        PowerMockito.doAnswer((Answer<Void>)invocation -> {
                        warning[0] = invocation.getArguments()[0];
                        warning[1] = invocation.getArguments()[1];
                        return null;
                    })
                    .when(UserMessageBox.class, "showWarning", ArgumentMatchers.anyString(),
                          ArgumentMatchers.anyString());

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Assertions.assertThat(warning)
                  .containsExactly("AUTOMATON IS EMPTY",
                                   "No tree can be accepted by the automaton");
    }

    @Test
    public void actionPerformed_WhenCheckEmptinessIsFalse_ThenInfo()
            throws Exception
    {
        // given
        Object[] info = new Object[2];

        Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CHECK EMPTINESS");
        Mockito.when(mockBottomUp.checkEmptiness()).thenReturn(false);
        PowerMockito.doAnswer((Answer<Void>)invocation -> {
                        info[0] = invocation.getArguments()[0];
                        info[1] = invocation.getArguments()[1];
                        return null;
                    })
                    .when(UserMessageBox.class, "showInfo", ArgumentMatchers.anyString(),
                          ArgumentMatchers.anyString());

        // when
        testObject.actionPerformed(mockActionEvent);

        // then
        Assertions.assertThat(info)
                  .containsExactly("AUTOMATON IS NON-EMPTY",
                                   "The automaton can accept at least one tree");
    }

    @Test
    public void receiveSignal_WhenSourcePointerIsNotEmpty_ThenRunButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockBottomUp);
        Mockito.when(mockMessage.getSource()).thenReturn(mockPointer);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType).isEqualTo(ActionButtonsPanel.ButtonsType.RUN);
        Mockito.verify(mockPointer, Mockito.times(1)).get();
    }

    @Test
    public void receiveSignal_WhenSourceRunningModeAndPointerIsEmpty_ThenNoneButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(true);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType)
                  .isEqualTo(ActionButtonsPanel.ButtonsType.NONE);
    }

    @Test
    public void receiveSignal_WhenSourceRunningModeAndRunning_ThenRunButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.RUNNING);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType).isEqualTo(ActionButtonsPanel.ButtonsType.RUN);
    }

    @Test
    public void receiveSignal_WhenSourceRunningModeAndStopped_ThenRunButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType).isEqualTo(ActionButtonsPanel.ButtonsType.RUN);
    }

    @Test
    public void receiveSignal_WhenSourceRunningModeAndFinished_ThenRunButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType).isEqualTo(ActionButtonsPanel.ButtonsType.RUN);
    }

    @Test
    public void receiveSignal_WhenSourceRunningModeAndContinuing_ThenContinueButton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockMessage.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.CONTINUING);

        // when
        testObject.receiveSignal(mockMessage);

        // then
        Assertions.assertThat(testObject.buttonsType)
                  .isEqualTo(ActionButtonsPanel.ButtonsType.CONTINUE);
    }
}
