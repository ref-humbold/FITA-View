package fitaview.viewer.automaton;

import java.util.*;
import java.util.stream.IntStream;
import javax.swing.tree.DefaultMutableTreeNode;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fitaview.automaton.*;
import fitaview.messaging.Message;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.utils.Triple;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AutomatonTreeViewTest
{
    @Mock private Pointer<TreeAutomaton> mockPointer;

    @Mock private TreeAutomaton mockAutomaton;

    @Mock private Message<Void> mockSignal;

    @Mock private Message<Triple<Variable, String, String>> mockMessage;

    @InjectMocks private AutomatonTreeView testObject;

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void receiveSignal_WhenPointerOfNull_ThenEmptyAutomaton()
    {
        // given
        Mockito.when(mockPointer.get()).thenReturn(null);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.rootNode.getChildCount()).isEqualTo(0);
        Assertions.assertThat(testObject.rootNode.getUserObject())
                  .isEqualTo(AutomatonTreeView.EMPTY_ROOT_TEXT);
    }

    @Test
    public void receiveSignal_WhenPointerOfBottomUpDfta_ThenAutomatonInitialized()
            throws Exception
    {
        // given
        List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
        List<Variable> variables = Arrays.asList(new Variable(1, "X", "T", "F"),
                                                 new Variable(2, "#", "!", "@", "$", "&"));

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        var automaton = new BottomUpDfta(variables, alphabet);

        accept.put(variables.get(0), Pair.make("T", true));
        accept.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));
        automaton.addAcceptanceConditions(accept);
        automaton.addTransition(variables.get(0), "X", "X", "0", "F");
        automaton.addTransition(variables.get(0), "X", "X", "1", "T");
        automaton.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "and",
                                Wildcard.RIGHT_VALUE);
        automaton.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "F", "or",
                                Wildcard.LEFT_VALUE);
        automaton.addTransition(variables.get(0), "T", "F", "impl", "F");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE, "0",
                                "!");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE, "1",
                                "!");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "and",
                                "&");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "or",
                                "$");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                "impl", "@");

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);
        DefaultMutableTreeNode transitionChild = (DefaultMutableTreeNode)rootNode.getChildAt(3);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);
        DefaultMutableTreeNode transition1Node =
                (DefaultMutableTreeNode)transitionChild.getChildAt(0);
        DefaultMutableTreeNode transition2Node =
                (DefaultMutableTreeNode)transitionChild.getChildAt(1);

        List<String> alphabetValues = IntStream.range(0, alphabetChild.getChildCount())
                                               .mapToObj(
                                                       i -> (String)((DefaultMutableTreeNode)alphabetChild.getChildAt(
                                                               i)).getUserObject())
                                               .toList();

        List<String> variable1Values = IntStream.range(0, variable1Node.getChildCount())
                                                .mapToObj(
                                                        i -> (String)((DefaultMutableTreeNode)variable1Node.getChildAt(
                                                                i)).getUserObject())
                                                .toList();

        List<String> transition1Values = IntStream.range(0, transition1Node.getChildCount())
                                                  .mapToObj(
                                                          i -> (String)((DefaultMutableTreeNode)transition1Node.getChildAt(
                                                                  i)).getUserObject())
                                                  .toList();

        List<String> transition2Values = IntStream.range(0, transition2Node.getChildCount())
                                                  .mapToObj(
                                                          i -> (String)((DefaultMutableTreeNode)transition2Node.getChildAt(
                                                                  i)).getUserObject())
                                                  .toList();

        List<Variable> expectedVariables = new ArrayList<>(automaton.getVariables());
        List<String> expectedVariable1Values = expectedVariables.get(0).getValuesList();
        List<String> expectedVariable2Values = expectedVariables.get(1).getValuesList();

        IntStream.range(0, expectedVariable1Values.size())
                 .filter(i -> Objects.equals(expectedVariables.get(0).getInitValue(),
                                             expectedVariable1Values.get(i)))
                 .forEach(i -> expectedVariable1Values.set(i, "%s [init value]".formatted(
                         expectedVariable1Values.get(i))));

        IntStream.range(0, expectedVariable2Values.size())
                 .filter(i -> Objects.equals(expectedVariables.get(1).getInitValue(),
                                             expectedVariable2Values.get(i)))
                 .forEach(i -> expectedVariable2Values.set(i, "%s [init value]".formatted(
                         expectedVariable2Values.get(i))));

        Assertions.assertThat(rootNode.getChildCount()).isEqualTo(4);
        Assertions.assertThat(rootNode.getUserObject()).isEqualTo(automaton.getTypeName());

        Assertions.assertThat(alphabetChild.getUserObject()).isEqualTo("Alphabet");
        Assertions.assertThat(alphabetChild.getChildCount())
                  .isEqualTo(automaton.getAlphabet().size());

        Assertions.assertThat(variableChild.getUserObject()).isEqualTo("Variables");
        Assertions.assertThat(variableChild.getChildCount()).isEqualTo(expectedVariables.size());

        Assertions.assertThat(alphabetValues).containsExactlyElementsOf(automaton.getAlphabet());

        Assertions.assertThat(variable1Node.getUserObject())
                  .isEqualTo(expectedVariables.get(0).getVarName());
        Assertions.assertThat(variable1Values).containsExactlyElementsOf(expectedVariable1Values);

        Assertions.assertThat(variable2Node.getUserObject())
                  .isEqualTo(expectedVariables.get(1).getVarName());
        Assertions.assertThat(IntStream.range(0, variable2Node.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)variable2Node.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(expectedVariable2Values);

        Assertions.assertThat(acceptChild.getUserObject()).isEqualTo("Acceptance conditions");
        Assertions.assertThat(acceptChild.getChildCount())
                  .isEqualTo(automaton.getAcceptanceConditions().size());

        Assertions.assertThat(accept1Node.getUserObject()).isEqualTo("condition");
        Assertions.assertThat(IntStream.range(0, accept1Node.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)accept1Node.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(automaton.getAcceptanceConditions()
                                                      .getStatesConditions()
                                                      .stream()
                                                      .flatMap(state -> state.entrySet().stream())
                                                      .map(AcceptanceConditions::getEntryString)
                                                      .toList());

        Assertions.assertThat(transitionChild.getUserObject()).isEqualTo("Transition relation");
        Assertions.assertThat(transitionChild.getChildCount()).isEqualTo(2);

        Assertions.assertThat(transition1Values.size() + transition2Values.size())
                  .isEqualTo(automaton.getTransitionAsStrings().size());
        Assertions.assertThat(transition1Values)
                  .containsExactlyElementsOf(automaton.getTransitionAsStrings()
                                                      .entrySet()
                                                      .stream()
                                                      .filter(entry -> Objects.equals(entry.getKey()
                                                                                           .getFirst()
                                                                                           .getVarName(),
                                                                                      transition1Node.getUserObject()))
                                                      .map(entry -> testObject.getTransitionEntryString(
                                                              entry.getKey().getSecond(),
                                                              entry.getValue()))
                                                      .toList());
        Assertions.assertThat(transition2Values)
                  .containsExactlyElementsOf(automaton.getTransitionAsStrings()
                                                      .entrySet()
                                                      .stream()
                                                      .filter(entry -> Objects.equals(entry.getKey()
                                                                                           .getFirst()
                                                                                           .getVarName(),
                                                                                      transition2Node.getUserObject()))
                                                      .map(entry -> testObject.getTransitionEntryString(
                                                              entry.getKey().getSecond(),
                                                              entry.getValue()))
                                                      .toList());
    }

    @Test
    public void receiveSignal_WhenPointerOfTopDownDfta_ThenAutomatonInitialized()
            throws Exception
    {
        // given
        List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
        List<Variable> variables = Arrays.asList(new Variable(1, "X", "T", "F"),
                                                 new Variable(2, "#", "!", "@", "$", "&"));

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        var automaton = new TopDownDfta(variables, alphabet);

        accept.put(variables.get(0), Pair.make("X", true));
        accept.put(variables.get(1), Pair.make("!", false));
        automaton.addAcceptanceConditions(accept);
        automaton.addTransition(variables.get(0), "F", "0", "X", "X");
        automaton.addTransition(variables.get(0), "T", "1", "X", "X");
        automaton.addTransition(variables.get(0), "T", "and", Wildcard.SAME_VALUE,
                                Wildcard.SAME_VALUE);
        automaton.addTransition(variables.get(0), "F", "or", Wildcard.SAME_VALUE,
                                Wildcard.SAME_VALUE);
        automaton.addTransition(variables.get(0), "F", "impl", "T", "F");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, "0", "!", "!");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, "1", "!", "!");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, "and", "&", "&");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, "or", "$", "$");
        automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, "impl", "@", "@");

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);
        DefaultMutableTreeNode transitionChild = (DefaultMutableTreeNode)rootNode.getChildAt(3);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);
        DefaultMutableTreeNode transition1Node =
                (DefaultMutableTreeNode)transitionChild.getChildAt(0);
        DefaultMutableTreeNode transition2Node =
                (DefaultMutableTreeNode)transitionChild.getChildAt(1);

        List<String> transition1Values = IntStream.range(0, transition1Node.getChildCount())
                                                  .mapToObj(
                                                          i -> (String)((DefaultMutableTreeNode)transition1Node.getChildAt(
                                                                  i)).getUserObject())
                                                  .toList();

        List<String> transition2Values = IntStream.range(0, transition2Node.getChildCount())
                                                  .mapToObj(
                                                          i -> (String)((DefaultMutableTreeNode)transition2Node.getChildAt(
                                                                  i)).getUserObject())
                                                  .toList();

        List<Variable> expectedVariables = new ArrayList<>(automaton.getVariables());
        List<String> expectedVariable1Values = expectedVariables.get(0).getValuesList();
        List<String> expectedVariable2Values = expectedVariables.get(1).getValuesList();

        IntStream.range(0, expectedVariable1Values.size())
                 .filter(i -> Objects.equals(expectedVariables.get(0).getInitValue(),
                                             expectedVariable1Values.get(i)))
                 .forEach(i -> expectedVariable1Values.set(i, "%s [init value]".formatted(
                         expectedVariable1Values.get(i))));

        IntStream.range(0, expectedVariable2Values.size())
                 .filter(i -> Objects.equals(expectedVariables.get(1).getInitValue(),
                                             expectedVariable2Values.get(i)))
                 .forEach(i -> expectedVariable2Values.set(i, "%s [init value]".formatted(
                         expectedVariable2Values.get(i))));

        Assertions.assertThat(rootNode.getChildCount()).isEqualTo(4);
        Assertions.assertThat(rootNode.getUserObject()).isEqualTo(automaton.getTypeName());

        Assertions.assertThat(alphabetChild.getUserObject()).isEqualTo("Alphabet");
        Assertions.assertThat(alphabetChild.getChildCount())
                  .isEqualTo(automaton.getAlphabet().size());

        Assertions.assertThat(IntStream.range(0, alphabetChild.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)alphabetChild.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(automaton.getAlphabet());

        Assertions.assertThat(variableChild.getUserObject()).isEqualTo("Variables");
        Assertions.assertThat(variableChild.getChildCount()).isEqualTo(expectedVariables.size());

        Assertions.assertThat(variable1Node.getUserObject())
                  .isEqualTo(expectedVariables.get(0).getVarName());
        Assertions.assertThat(IntStream.range(0, variable1Node.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)variable1Node.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(expectedVariable1Values);

        Assertions.assertThat(variable2Node.getUserObject())
                  .isEqualTo(expectedVariables.get(1).getVarName());
        Assertions.assertThat(IntStream.range(0, variable2Node.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)variable2Node.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(expectedVariable2Values);

        Assertions.assertThat(acceptChild.getUserObject()).isEqualTo("Acceptance conditions");
        Assertions.assertThat(acceptChild.getChildCount())
                  .isEqualTo(automaton.getAcceptanceConditions().size());

        Assertions.assertThat(accept1Node.getUserObject()).isEqualTo("condition");
        Assertions.assertThat(IntStream.range(0, accept1Node.getChildCount())
                                       .mapToObj(
                                               i -> (String)((DefaultMutableTreeNode)accept1Node.getChildAt(
                                                       i)).getUserObject())
                                       .toList())
                  .containsExactlyElementsOf(automaton.getAcceptanceConditions()
                                                      .getStatesConditions()
                                                      .stream()
                                                      .flatMap(state -> state.entrySet().stream())
                                                      .map(AcceptanceConditions::getEntryString)
                                                      .toList());

        Assertions.assertThat(transitionChild.getUserObject()).isEqualTo("Transition relation");
        Assertions.assertThat(transitionChild.getChildCount()).isEqualTo(2);

        Assertions.assertThat(transition1Values.size() + transition2Values.size())
                  .isEqualTo(automaton.getTransitionAsStrings().size());
        Assertions.assertThat(transition1Values)
                  .containsExactlyElementsOf(automaton.getTransitionAsStrings()
                                                      .entrySet()
                                                      .stream()
                                                      .filter(entry -> Objects.equals(entry.getKey()
                                                                                           .getFirst()
                                                                                           .getVarName(),
                                                                                      transition1Node.getUserObject()))
                                                      .map(entry -> testObject.getTransitionEntryString(
                                                              entry.getKey().getSecond(),
                                                              entry.getValue()))
                                                      .toList());
        Assertions.assertThat(transition2Values)
                  .containsExactlyElementsOf(automaton.getTransitionAsStrings()
                                                      .entrySet()
                                                      .stream()
                                                      .filter(entry -> Objects.equals(entry.getKey()
                                                                                           .getFirst()
                                                                                           .getVarName(),
                                                                                      transition2Node.getUserObject()))
                                                      .map(entry -> testObject.getTransitionEntryString(
                                                              entry.getKey().getSecond(),
                                                              entry.getValue()))
                                                      .toList());
    }

    @Test
    public void receiveSignal_WhenStopped_ThenEmptyTransitions()
    {
        // given
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        // when
        testObject.receiveSignal(mockSignal);

        // then
        Assertions.assertThat(testObject.lastTransitions).isEmpty();
    }

    @Test
    public void receiveMessage_ThenApplyTransition()
            throws Exception
    {
        // given
        Triple<Variable, String, String> param = Triple.make(new Variable(0, "A", "B"), "A", "B");

        Mockito.when(mockMessage.getParam()).thenReturn(param);

        // when
        testObject.receiveMessage(mockMessage);

        // then
        Assertions.assertThat(testObject.lastTransitions)
                  .hasSize(1)
                  .containsKey(param.getFirst())
                  .containsValue(
                          testObject.getTransitionEntryString(param.getSecond(), param.getThird()));
    }
}
