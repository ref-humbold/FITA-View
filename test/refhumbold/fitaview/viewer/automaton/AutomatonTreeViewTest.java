package refhumbold.fitaview.viewer.automaton;

import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Pointer;
import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.*;
import refhumbold.fitaview.automaton.transition.DuplicatedTransitionException;
import refhumbold.fitaview.automaton.transition.IllegalTransitionException;
import refhumbold.fitaview.messaging.Message;

@RunWith(MockitoJUnitRunner.class)
public class AutomatonTreeViewTest
{
    @Mock
    private Pointer<TreeAutomaton> mockPointer;

    @Mock
    private TreeAutomaton mockAutomaton;

    @Mock
    private Message<Void> mockSignal;

    @Mock
    private Message<Triple<Variable, String, String>> mockMessage;

    @InjectMocks
    private AutomatonTreeView testObject;

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testReceiveSignalWhenNullAutomaton()
    {
        Mockito.when(mockPointer.get()).thenReturn(null);

        testObject.receiveSignal(mockSignal);

        DefaultMutableTreeNode rootNode = testObject.rootNode;

        Assert.assertEquals(0, rootNode.getChildCount());
        Assert.assertEquals(AutomatonTreeView.EMPTY_ROOT_TEXT, rootNode.getUserObject());
    }

    @Test
    public void testReceiveSignalWhenBottomUpDFTA()
    {
        List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
        List<Variable> variables = null;

        try
        {
            variables = Arrays.asList(new Variable(1, "X", "T", "F"),
                                      new Variable(2, "#", "!", "@", "$", "&"));
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        BottomUpDFTA automaton = new BottomUpDFTA(variables, alphabet);

        accept.put(variables.get(0), Pair.make("T", true));
        accept.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));
        automaton.addAcceptanceConditions(accept);

        try
        {
            automaton.addTransition(variables.get(0), "X", "X", "0", "F");
            automaton.addTransition(variables.get(0), "X", "X", "1", "T");
            automaton.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "and",
                                    Wildcard.RIGHT_VALUE);
            automaton.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "F", "or",
                                    Wildcard.LEFT_VALUE);
            automaton.addTransition(variables.get(0), "T", "F", "impl", "F");
            automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE,
                                    "0", "!");
            automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE,
                                    "1", "!");
            automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                    "and", "&");
            automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                    "or", "$");
            automaton.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                    "impl", "@");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        testObject.receiveSignal(mockSignal);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);
        DefaultMutableTreeNode transitionChild = (DefaultMutableTreeNode)rootNode.getChildAt(3);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);
        DefaultMutableTreeNode transition1Node = (DefaultMutableTreeNode)transitionChild.getChildAt(
            0);
        DefaultMutableTreeNode transition2Node = (DefaultMutableTreeNode)transitionChild.getChildAt(
            1);

        List<String> alphabetValues = new ArrayList<>();
        List<String> variable1Values = new ArrayList<>();
        List<String> variable2Values = new ArrayList<>();
        List<String> accept1Values = new ArrayList<>();
        List<String> transition1Values = new ArrayList<>();
        List<String> transition2Values = new ArrayList<>();

        for(int i = 0; i < alphabetChild.getChildCount(); ++i)
            alphabetValues.add(
                (String)((DefaultMutableTreeNode)alphabetChild.getChildAt(i)).getUserObject());

        for(int i = 0; i < variable1Node.getChildCount(); ++i)
            variable1Values.add(
                (String)((DefaultMutableTreeNode)variable1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < variable2Node.getChildCount(); ++i)
            variable2Values.add(
                (String)((DefaultMutableTreeNode)variable2Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < accept1Node.getChildCount(); ++i)
            accept1Values.add(
                (String)((DefaultMutableTreeNode)accept1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < transition1Node.getChildCount(); ++i)
            transition1Values.add(
                (String)((DefaultMutableTreeNode)transition1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < transition2Node.getChildCount(); ++i)
            transition2Values.add(
                (String)((DefaultMutableTreeNode)transition2Node.getChildAt(i)).getUserObject());

        List<Variable> expectedVariables = new ArrayList<>(automaton.getVariables());
        List<String> expectedVariable1Values = expectedVariables.get(0).getValuesList();
        List<String> expectedVariable2Values = expectedVariables.get(1).getValuesList();
        List<String> expectedAccept1Values = new ArrayList<>();
        List<String> expectedTransition1Values = new ArrayList<>();
        List<String> expectedTransition2Values = new ArrayList<>();

        for(int i = 0; i < expectedVariable1Values.size(); ++i)
            if(Objects.equals(expectedVariables.get(0).getInitValue(),
                              expectedVariable1Values.get(i)))
                expectedVariable1Values.set(i, expectedVariable1Values.get(i) + " [init value]");

        for(int i = 0; i < expectedVariable2Values.size(); ++i)
            if(Objects.equals(expectedVariables.get(1).getInitValue(),
                              expectedVariable2Values.get(i)))
                expectedVariable2Values.set(i, expectedVariable2Values.get(i) + " [init value]");

        for(Map<Variable, Pair<String, Boolean>> state : automaton.getAcceptanceConditions()
                                                                  .getStatesConditions())
            for(Map.Entry<Variable, Pair<String, Boolean>> entry : state.entrySet())
                expectedAccept1Values.add(AcceptanceConditions.getEntryString(entry));

        for(Map.Entry<Pair<Variable, String>, String> entry : automaton.getTransitionAsStrings()
                                                                       .entrySet())
            if(Objects.equals(entry.getKey().getFirst().getVarName(),
                              transition1Node.getUserObject()))
                expectedTransition1Values.add(
                    testObject.getTransitionEntryString(entry.getKey().getSecond(),
                                                        entry.getValue()));

        for(Map.Entry<Pair<Variable, String>, String> entry : automaton.getTransitionAsStrings()
                                                                       .entrySet())
            if(Objects.equals(entry.getKey().getFirst().getVarName(),
                              transition2Node.getUserObject()))
                expectedTransition2Values.add(
                    testObject.getTransitionEntryString(entry.getKey().getSecond(),
                                                        entry.getValue()));

        Assert.assertEquals(4, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());

        Assert.assertEquals("Alphabet", alphabetChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), alphabetChild.getChildCount());

        Assert.assertEquals("Variables", variableChild.getUserObject());
        Assert.assertEquals(expectedVariables.size(), variableChild.getChildCount());

        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());

        Assert.assertEquals(expectedVariables.get(0).getVarName(), variable1Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable1Values.toArray(), variable1Values.toArray());

        Assert.assertEquals(expectedVariables.get(1).getVarName(), variable2Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable2Values.toArray(), variable2Values.toArray());

        Assert.assertEquals("Acceptance conditions", acceptChild.getUserObject());
        Assert.assertEquals(automaton.getAcceptanceConditions().size(),
                            acceptChild.getChildCount());

        Assert.assertEquals("condition", accept1Node.getUserObject());
        Assert.assertArrayEquals(expectedAccept1Values.toArray(), accept1Values.toArray());

        Assert.assertEquals("Transition relation", transitionChild.getUserObject());
        Assert.assertEquals(2, transitionChild.getChildCount());

        Assert.assertEquals(automaton.getTransitionAsStrings().size(),
                            transition1Values.size() + transition2Values.size());
        Assert.assertArrayEquals(expectedTransition1Values.toArray(), transition1Values.toArray());
        Assert.assertArrayEquals(expectedTransition2Values.toArray(), transition2Values.toArray());
    }

    @Test
    public void testReceiveSignalWhenTopDownDFTA()
    {
        List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
        List<Variable> variables = null;

        try
        {
            variables = Arrays.asList(new Variable(1, "X", "T", "F"),
                                      new Variable(2, "#", "!", "@", "$", "&"));
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        TopDownDFTA automaton = new TopDownDFTA(variables, alphabet);

        accept.put(variables.get(0), Pair.make("X", true));
        accept.put(variables.get(1), Pair.make("!", false));
        automaton.addAcceptanceConditions(accept);

        try
        {
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
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Mockito.when(mockPointer.get()).thenReturn(automaton);
        Mockito.when(mockSignal.getSource()).thenReturn(mockPointer);

        testObject.receiveSignal(mockSignal);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);
        DefaultMutableTreeNode transitionChild = (DefaultMutableTreeNode)rootNode.getChildAt(3);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);
        DefaultMutableTreeNode transition1Node = (DefaultMutableTreeNode)transitionChild.getChildAt(
            0);
        DefaultMutableTreeNode transition2Node = (DefaultMutableTreeNode)transitionChild.getChildAt(
            1);

        List<String> alphabetValues = new ArrayList<>();
        List<String> variable1Values = new ArrayList<>();
        List<String> variable2Values = new ArrayList<>();
        List<String> accept1Values = new ArrayList<>();
        List<String> transition1Values = new ArrayList<>();
        List<String> transition2Values = new ArrayList<>();

        for(int i = 0; i < alphabetChild.getChildCount(); ++i)
            alphabetValues.add(
                (String)((DefaultMutableTreeNode)alphabetChild.getChildAt(i)).getUserObject());

        for(int i = 0; i < variable1Node.getChildCount(); ++i)
            variable1Values.add(
                (String)((DefaultMutableTreeNode)variable1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < variable2Node.getChildCount(); ++i)
            variable2Values.add(
                (String)((DefaultMutableTreeNode)variable2Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < accept1Node.getChildCount(); ++i)
            accept1Values.add(
                (String)((DefaultMutableTreeNode)accept1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < transition1Node.getChildCount(); ++i)
            transition1Values.add(
                (String)((DefaultMutableTreeNode)transition1Node.getChildAt(i)).getUserObject());

        for(int i = 0; i < transition2Node.getChildCount(); ++i)
            transition2Values.add(
                (String)((DefaultMutableTreeNode)transition2Node.getChildAt(i)).getUserObject());

        List<Variable> expectedVariables = new ArrayList<>(automaton.getVariables());
        List<String> expectedVariable1Values = expectedVariables.get(0).getValuesList();
        List<String> expectedVariable2Values = expectedVariables.get(1).getValuesList();
        List<String> expectedAccept1Values = new ArrayList<>();
        List<String> expectedTransition1Values = new ArrayList<>();
        List<String> expectedTransition2Values = new ArrayList<>();

        for(int i = 0; i < expectedVariable1Values.size(); i++)
            if(Objects.equals(expectedVariables.get(0).getInitValue(),
                              expectedVariable1Values.get(i)))
                expectedVariable1Values.set(i, expectedVariable1Values.get(i) + " [init value]");

        for(int i = 0; i < expectedVariable2Values.size(); i++)
            if(Objects.equals(expectedVariables.get(1).getInitValue(),
                              expectedVariable2Values.get(i)))
                expectedVariable2Values.set(i, expectedVariable2Values.get(i) + " [init value]");

        for(Map<Variable, Pair<String, Boolean>> state : automaton.getAcceptanceConditions()
                                                                  .getStatesConditions())
            for(Map.Entry<Variable, Pair<String, Boolean>> entry : state.entrySet())
                expectedAccept1Values.add(AcceptanceConditions.getEntryString(entry));

        for(Map.Entry<Pair<Variable, String>, String> entry : automaton.getTransitionAsStrings()
                                                                       .entrySet())
            if(Objects.equals(entry.getKey().getFirst().getVarName(),
                              transition1Node.getUserObject()))
                expectedTransition1Values.add(
                    testObject.getTransitionEntryString(entry.getKey().getSecond(),
                                                        entry.getValue()));

        for(Map.Entry<Pair<Variable, String>, String> entry : automaton.getTransitionAsStrings()
                                                                       .entrySet())
            if(Objects.equals(entry.getKey().getFirst().getVarName(),
                              transition2Node.getUserObject()))
                expectedTransition2Values.add(
                    testObject.getTransitionEntryString(entry.getKey().getSecond(),
                                                        entry.getValue()));

        Assert.assertEquals(4, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());

        Assert.assertEquals("Alphabet", alphabetChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), alphabetChild.getChildCount());

        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());

        Assert.assertEquals("Variables", variableChild.getUserObject());
        Assert.assertEquals(expectedVariables.size(), variableChild.getChildCount());

        Assert.assertEquals(expectedVariables.get(0).getVarName(), variable1Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable1Values.toArray(), variable1Values.toArray());

        Assert.assertEquals(expectedVariables.get(1).getVarName(), variable2Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable2Values.toArray(), variable2Values.toArray());

        Assert.assertEquals("Acceptance conditions", acceptChild.getUserObject());
        Assert.assertEquals(automaton.getAcceptanceConditions().size(),
                            acceptChild.getChildCount());

        Assert.assertEquals("condition", accept1Node.getUserObject());
        Assert.assertArrayEquals(expectedAccept1Values.toArray(), accept1Values.toArray());

        Assert.assertEquals("Transition relation", transitionChild.getUserObject());
        Assert.assertEquals(2, transitionChild.getChildCount());

        Assert.assertEquals(automaton.getTransitionAsStrings().size(),
                            transition1Values.size() + transition2Values.size());
        Assert.assertArrayEquals(expectedTransition1Values.toArray(), transition1Values.toArray());
        Assert.assertArrayEquals(expectedTransition2Values.toArray(), transition2Values.toArray());
    }

    @Test
    public void testReceiveSignal()
    {
        Mockito.when(mockSignal.getSource()).thenReturn(AutomatonRunningModeSender.getInstance());
        Mockito.when(mockPointer.isEmpty()).thenReturn(false);
        Mockito.when(mockPointer.get()).thenReturn(mockAutomaton);
        Mockito.when(mockAutomaton.getRunningMode()).thenReturn(AutomatonRunningMode.STOPPED);

        testObject.receiveSignal(mockSignal);

        Assert.assertTrue(testObject.lastTransitions.isEmpty());
    }

    @Test
    public void testReceiveMessage()
    {
        Triple<Variable, String, String> param = null;

        try
        {
            param = Triple.make(new Variable(0, "A", "B"), "A", "B");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Mockito.when(mockMessage.getParam()).thenReturn(param);

        testObject.receiveMessage(mockMessage);

        String expectedMapValue = testObject.getTransitionEntryString(param.getSecond(),
                                                                      param.getThird());

        Assert.assertEquals(1, testObject.lastTransitions.size());
        Assert.assertTrue(testObject.lastTransitions.containsKey(param.getFirst()));
        Assert.assertTrue(testObject.lastTransitions.containsValue(expectedMapValue));
    }
}
