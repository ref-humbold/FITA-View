package fitaview.viewer.automaton;

import java.awt.Color;
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

import fitaview.Pointer;
import fitaview.automaton.AutomatonRunningMode;
import fitaview.automaton.AutomatonRunningModeSender;
import fitaview.automaton.NoTreeException;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.UndefinedAcceptanceException;
import fitaview.messaging.Message;
import fitaview.tree.UndefinedStateValueException;
import fitaview.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class AcceptancePanelTest
{
    @Mock
    private TreeAutomaton mockAutomaton;

    @Mock
    private Pointer<TreeAutomaton> mockPointer;

    @Mock
    private Message<Void> mockSignal;

    @InjectMocks
    private AcceptancePanel testObject;

    @Before
    public void setUp()
        throws Exception
    {
        PowerMockito.mockStatic(UserMessageBox.class);
        PowerMockito.doNothing()
                    .when(UserMessageBox.class, "showInfo", Matchers.anyString(),
                          Matchers.anyString());
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testReceiveSignalWhenStopped()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenFinishedAndAccepted()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.GREEN, testObject.getBackground());
        Assert.assertEquals(Color.BLACK, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE ACCEPTED :)", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenFinishedAndRejects()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(AcceptancePanel.DARK_RED, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE REJECTED :(", testObject.acceptanceLabel.getText());
    }

    @Test(expected = UndefinedAcceptanceException.class)
    public void testReceiveSignalWhenFinishedAndException()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        PowerMockito.doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation)
                throws Exception
            {
                throw (Exception)invocation.getArguments()[0];
            }
        }).when(UserMessageBox.class, "showException", Matchers.any(Exception.class));

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
    public void testReceiveSignalWhenRunning()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.RUNNING);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenContinuingAndAccepted()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.GREEN, testObject.getBackground());
        Assert.assertEquals(Color.BLACK, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE ACCEPTED :)", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenContinuingAndRejects()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(AcceptancePanel.DARK_RED, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("TREE REJECTED :(", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenContinuing()
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
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }

    @Test
    public void testReceiveSignalWhenAutomaton()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        testObject.receiveSignal(mockSignal);

        Assert.assertEquals(Color.DARK_GRAY, testObject.getBackground());
        Assert.assertEquals(Color.WHITE, testObject.acceptanceLabel.getForeground());
        Assert.assertEquals("------", testObject.acceptanceLabel.getText());
    }
}
