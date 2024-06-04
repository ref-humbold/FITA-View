package fitaview.viewer.automaton;

import java.awt.event.ActionEvent;
import java.util.Collections;
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

import fitaview.TestUtils;
import fitaview.automaton.BottomUpDfta;
import fitaview.automaton.TopDownDfta;
import fitaview.automaton.TopDownNfta;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.nondeterminism.FirstElementChoice;
import fitaview.automaton.nondeterminism.GreatestHashCodeChoice;
import fitaview.automaton.nondeterminism.LeastHashCodeChoice;
import fitaview.automaton.nondeterminism.RandomChoice;
import fitaview.automaton.traversing.*;
import fitaview.messaging.Message;
import fitaview.utils.Pointer;
import fitaview.viewer.UserMessageBox;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.swing.*")
@PrepareForTest(UserMessageBox.class)
public class ModifyingRadioButtonsPanelTest
{
    @Mock private Pointer<TreeAutomaton> mockPointer;

    @Mock private ActionEvent mockActionEvent;

    @Mock private Message<Void> mockMessage;

    @InjectMocks private ModifyingRadioButtonsPanel testObject;

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

    @Test
    public void actionPerformed_WhenBottomUpDFS()
    {
        // given
        TestUtils.failOnException(() -> {
            Mockito.when(mockPointer.get())
                   .thenReturn(new BottomUpDfta(Collections.emptySet(), Collections.emptySet()));
            Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");
            PowerMockito.doAnswer((Answer<Void>)invocation -> {
                throw (Exception)invocation.getArguments()[0];
            }).when(UserMessageBox.class, "showException", ArgumentMatchers.any(Exception.class));
        });
        // then
        Assertions.assertThatThrownBy(() -> testObject.actionPerformed(mockActionEvent))
                  .isInstanceOf(IncorrectTraversingException.class);
    }

    @Test
    public void actionPerformed_WhenBottomUpBFS()
    {
        // given
        BottomUpDfta automaton = new BottomUpDfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("BFS");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getTraversing())
                  .isNotNull()
                  .isInstanceOf(BottomUpBfs.class);
    }

    @Test
    public void actionPerformed_WhenBottomUpLevel()
    {
        // given
        BottomUpDfta automaton = new BottomUpDfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEVEL");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getTraversing())
                  .isNotNull()
                  .isInstanceOf(BottomUpLevel.class);
    }

    @Test
    public void actionPerformed_WhenTopDownDFS()
    {
        // given
        TopDownDfta automaton = new TopDownDfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DFS");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getTraversing()).isNotNull().isInstanceOf(TopDownDfs.class);
    }

    @Test
    public void actionPerformed_WhenTopDownBFS()
    {
        // given
        TopDownDfta automaton = new TopDownDfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("BFS");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getTraversing()).isNotNull().isInstanceOf(TopDownBfs.class);
    }

    @Test
    public void actionPerformed_WhenTopDownLevel()
    {
        // given
        TopDownDfta automaton = new TopDownDfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEVEL");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getTraversing())
                  .isNotNull()
                  .isInstanceOf(TopDownLevel.class);
    }

    @Test
    public void actionPerformed_WhenFirstChoice()
    {
        // given
        TopDownNfta automaton = new TopDownNfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("FIRST");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getChoice())
                  .isNotNull()
                  .isInstanceOf(FirstElementChoice.class);
    }

    @Test
    public void actionPerformed_WhenRandomChoice()
    {
        // given
        TopDownNfta automaton = new TopDownNfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RANDOM");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getChoice()).isNotNull().isInstanceOf(RandomChoice.class);
    }

    @Test
    public void actionPerformed_WhenLeastChoice()
    {
        // given
        TopDownNfta automaton = new TopDownNfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEAST");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getChoice())
                  .isNotNull()
                  .isInstanceOf(LeastHashCodeChoice.class);
    }

    @Test
    public void actionPerformed_WhenGreatestChoice()
    {
        // given
        TopDownNfta automaton = new TopDownNfta(Collections.emptySet(), Collections.emptySet());

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("GREATEST");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Assertions.assertThat(automaton.getChoice())
                  .isNotNull()
                  .isInstanceOf(GreatestHashCodeChoice.class);
    }

    @Test
    public void receiveSignal_WhenNullAutomaton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(null);
        // when
        testObject.receiveSignal(mockMessage);
        // then
        Assertions.assertThat(testObject.getComponentCount()).isEqualTo(2);
    }

    @Test
    public void receiveSignal_WhenBottomUpDFTA()
    {
        // given
        Mockito.when(mockPointer.get())
               .thenReturn(new BottomUpDfta(Collections.emptySet(), Collections.emptySet()));
        // when
        testObject.receiveSignal(mockMessage);
        // then
        Assertions.assertThat(testObject.getComponentCount()).isEqualTo(3);
    }

    @Test
    public void receiveSignal_WhenTopDownDFTA()
    {
        // given
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownDfta(Collections.emptySet(), Collections.emptySet()));
        // when
        testObject.receiveSignal(mockMessage);
        // then
        Assertions.assertThat(testObject.getComponentCount()).isEqualTo(3);
    }

    @Test
    public void receiveSignal_WhenTopDownNFTA()
    {
        // given
        Mockito.when(mockPointer.get())
               .thenReturn(new TopDownNfta(Collections.emptySet(), Collections.emptySet()));
        // when
        testObject.receiveSignal(mockMessage);
        // then
        Assertions.assertThat(testObject.getComponentCount()).isEqualTo(5);
    }
}
