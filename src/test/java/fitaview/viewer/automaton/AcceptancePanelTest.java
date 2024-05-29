package fitaview.viewer.automaton;

import java.awt.Color;
import org.junit.After;
import org.junit.Assert;
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
import fitaview.automaton.NoTreeException;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.UndefinedAcceptanceException;
import fitaview.messaging.Message;
import fitaview.tree.UndefinedStateValueException;
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
    public void receiveSignal_WhenStopped()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenFinishedAndAccepted()
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted()).thenReturn(true);
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.GREEN, testObject.getBackground());
        Assert.assertEquals(Color.BLACK, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE ACCEPTED :)", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenFinishedAndRejects()
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted()).thenReturn(false);
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(AcceptancePanel.DARK_RED, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE REJECTED :(", testObject.acceptanceLabel.getText());
    }

    @Test(expected = UndefinedAcceptanceException.class)
    public void receiveSignal_WhenFinishedAndException()
            throws Exception
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.FINISHED);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted())
                   .thenThrow(new UndefinedAcceptanceException(""));
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        PowerMockito.doAnswer((Answer<Void>)invocation -> {
            throw (Exception)invocation.getArguments()[0];
        }).when(UserMessageBox.class, "showException", ArgumentMatchers.any(Exception.class));

        try
        {
            testObject.receiveSignal(mockSignal);
        }
        catch(Exception e)
        {
            Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
            Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
            Assert.assertEquals("------", testObject.acceptanceLabel.getText());

            throw e;
        }
    }

    @Test
    public void receiveSignal_WhenRunning()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.RUNNING);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenContinuingAndAccepted()
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode())
                   .thenReturn(AutomatonRunningMode.CONTINUING);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted()).thenReturn(true);
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.GREEN, testObject.getBackground());
        Assert.assertEquals(Color.BLACK, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE ACCEPTED :)", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenContinuingAndRejects()
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode())
                   .thenReturn(AutomatonRunningMode.CONTINUING);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted()).thenReturn(false);
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(AcceptancePanel.DARK_RED, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE REJECTED :(", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenContinuing()
    {
        try
        {
            Mockito.when(mockSignal.getSource())
                   .thenReturn(AutomatonRunningModeSender.getInstance());
            Mockito.when(mockAutomaton.getRunningMode())
                   .thenReturn(AutomatonRunningMode.CONTINUING);
            Mockito.when(mockPointer.isEmpty()).thenReturn(false);
            Mockito.when(mockAutomaton.isAccepted()).thenReturn(null);
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void receiveSignal_WhenAutomaton()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }
}
