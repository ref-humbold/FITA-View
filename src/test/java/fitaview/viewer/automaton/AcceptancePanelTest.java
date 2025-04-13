package fitaview.viewer.automaton;

import java.awt.Color;
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
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.UndefinedAcceptanceException;
import fitaview.messaging.Message;
import fitaview.utils.Pointer;
import fitaview.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class AcceptancePanelTest
{
    @Mock private TreeAutomaton mockAutomaton;

    @Mock private Pointer<TreeAutomaton> mockPointer;

    @Mock private Message<Void> mockSignal;

    @InjectMocks private AcceptancePanel testObject;

    @Before
    public void setUp()
            throws Exception
    {
        PowerMockito.mockStatic(UserMessageBox.class);
        PowerMockito.doNothing()
                    .when(UserMessageBox.class, "showInfo", ArgumentMatchers.anyString(),
                          ArgumentMatchers.anyString());
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void receiveSignal_WhenStopped_ThenGray()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.DARK_GRAY);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("------");
    }

    @Test
    public void receiveSignal_WhenFinishedAndAccepted_ThenGreen()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenReturn(true);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.GREEN);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.BLACK);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("TREE ACCEPTED :)");
    }

    @Test
    public void receiveSignal_WhenFinishedAndRejects_ThenRed()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenReturn(false);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(AcceptancePanel.DARK_RED);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("TREE REJECTED :(");
    }

    @Test
    public void receiveSignal_WhenFinishedAndException_ThenGray()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenThrow(new UndefinedAcceptanceException(""));

        PowerMockito.doAnswer((Answer<Void>)invocation -> {
            throw (Exception)invocation.getArguments()[0];
        }).when(UserMessageBox.class, "showException", ArgumentMatchers.any(Exception.class));

        // then
        Assertions.assertThatThrownBy(() -> testObject.receiveSignal(mockSignal))
                  .isInstanceOf(UndefinedAcceptanceException.class);
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.DARK_GRAY);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("------");
    }

    @Test
    public void receiveSignal_WhenRunning_ThenGray()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.RUNNING);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.DARK_GRAY);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("------");
    }

    @Test
    public void receiveSignal_WhenContinuingAndAccepted_ThenGreen()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.CONTINUING);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenReturn(true);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.GREEN);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.BLACK);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("TREE ACCEPTED :)");
    }

    @Test
    public void receiveSignal_WhenContinuingAndRejects_ThenRed()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.CONTINUING);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenReturn(false);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(AcceptancePanel.DARK_RED);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("TREE REJECTED :(");
    }

    @Test
    public void receiveSignal_WhenContinuing_thenGray()
            throws Exception
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.CONTINUING);
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockAutomaton.isAccepted()).thenReturn(null);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.DARK_GRAY);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("------");
    }

    @Test
    public void receiveSignal_WhenAutomaton_ThenGray()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.getBackground()).isEqualTo(Color.DARK_GRAY);
        Assertions.assertThat(testObject.acceptanceLabel.getForeground()).isEqualTo(Color.WHITE);
        Assertions.assertThat(testObject.acceptanceLabel.getText()).isEqualTo("------");
    }
}
