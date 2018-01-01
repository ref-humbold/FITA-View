package ref_humbold.fita_view.viewer.automaton;

import java.awt.event.ActionEvent;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.BottomUpDFTA;
import ref_humbold.fita_view.automaton.NullAutomaton;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class TraversingRadioButtonPanelTest
{
    private TraversingRadioButtonPanel testObject;
    private Pointer<TreeAutomaton> pointer = new Pointer<>(NullAutomaton.getInstance());
    private ActionEvent mockActionEvent = PowerMockito.mock(ActionEvent.class);

    @Before
    public void setUp()
    {
        testObject = new TraversingRadioButtonPanel(pointer);
        PowerMockito.mockStatic(UserMessageBox.class);
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
        pointer.set(new BottomUpDFTA(Collections.emptySet(), Collections.emptySet()));

        PowerMockito.doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation)
                throws Throwable
            {
                throw (Exception)invocation.getArguments()[0];
            }
        }).when(UserMessageBox.class, "showException", Matchers.any(Exception.class));
        PowerMockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");

        testObject.actionPerformed(mockActionEvent);
    }
}
