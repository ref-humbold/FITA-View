package fitaview.viewer.automaton;

import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fitaview.automaton.AutomatonRunningMode;
import fitaview.automaton.AutomatonRunningModeSender;
import fitaview.automaton.NodeInfoSource;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.Variable;
import fitaview.messaging.Message;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.utils.Triple;

@RunWith(MockitoJUnitRunner.class)
public class TransitionDrawingAreaTest
{
    @Mock private Pointer<TreeAutomaton> mockPointer;

    @Mock private TreeAutomaton mockAutomaton;

    @Mock private Message<Void> mockSignal;

    @Mock private Message<Triple<NodeInfoSource, String, Map<Variable, String>>> mockMessage;

    @InjectMocks private TransitionDrawingArea testObject;

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
    public void receiveSignal_WhenSourcePointer()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);
        // when
        testObject.receiveSignal(mockSignal);
        // then
        Assertions.assertThat(testObject.parentInfo).isNull();
        Assertions.assertThat(testObject.leftSonInfo).isNull();
        Assertions.assertThat(testObject.rightSonInfo).isNull();
    }

    @Test
    public void receiveSignal_WhenSourceMode()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);
        // when
        testObject.receiveSignal(mockSignal);
        // then
        Assertions.assertThat(testObject.parentInfo).isNull();
        Assertions.assertThat(testObject.leftSonInfo).isNull();
        Assertions.assertThat(testObject.rightSonInfo).isNull();
    }

    @Test
    public void receiveMessage_WhenLeftSon()
    {
        // given
        Triple<NodeInfoSource, String, Map<Variable, String>> param =
                Triple.make(NodeInfoSource.LEFT_SON, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);
        // when
        testObject.receiveMessage(mockMessage);
        // then
        Assertions.assertThat(testObject.leftSonInfo)
                  .isNotNull()
                  .isEqualTo(Pair.make(param.getSecond(), param.getThird()));
        Assertions.assertThat(testObject.parentInfo).isNull();
        Assertions.assertThat(testObject.rightSonInfo).isNull();
    }

    @Test
    public void receiveMessage_WhenParent()
    {
        // given
        Triple<NodeInfoSource, String, Map<Variable, String>> param =
                Triple.make(NodeInfoSource.PARENT, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);
        // when
        testObject.receiveMessage(mockMessage);
        // then
        Assertions.assertThat(testObject.parentInfo)
                  .isNotNull()
                  .isEqualTo(Pair.make(param.getSecond(), param.getThird()));
        Assertions.assertThat(testObject.leftSonInfo).isNull();
        Assertions.assertThat(testObject.rightSonInfo).isNull();
    }

    @Test
    public void receiveMessage_WhenRightSon()
    {
        // given
        Triple<NodeInfoSource, String, Map<Variable, String>> param =
                Triple.make(NodeInfoSource.RIGHT_SON, "LABEL", Collections.emptyMap());

        Mockito.when(mockMessage.getParam()).thenReturn(param);
        // when
        testObject.receiveMessage(mockMessage);
        // then
        Assertions.assertThat(testObject.rightSonInfo)
                  .isNotNull()
                  .isEqualTo(Pair.make(param.getSecond(), param.getThird()));
        Assertions.assertThat(testObject.parentInfo).isNull();
        Assertions.assertThat(testObject.leftSonInfo).isNull();
    }
}
