package ref_humbold.fita_view.viewer.automaton;

import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.*;
import ref_humbold.fita_view.messaging.Message;

@RunWith(MockitoJUnitRunner.class)
public class AutomatonTreeViewTest
{
    @Mock
    private Pointer<TreeAutomaton> mockPointer;

    @Mock
    private Message<Void> mockMessage;

    @InjectMocks
    private AutomatonTreeView testObject;

    @Before
    public void setUp()
    {
        Mockito.when(mockMessage.getSource()).thenReturn(mockPointer);
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

        testObject.receiveSignal(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;

        Assert.assertEquals(0, rootNode.getChildCount());
        Assert.assertEquals(AutomatonTreeView.EMPTY_ROOT_TEXT, rootNode.getUserObject());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        BottomUpDFTA automaton = new BottomUpDFTA(variables, alphabet);

        accept.put(variables.get(0), Pair.make("T", true));
        accept.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));
        automaton.addAcceptingConditions(accept);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receiveSignal(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);

        List<String> alphabetValues = new ArrayList<>();
        List<String> variable1Values = new ArrayList<>();
        List<String> variable2Values = new ArrayList<>();
        List<String> accept1Values = new ArrayList<>();

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

        List<String> expectedVariable1Values = automaton.getVariables().get(0).getValues();
        List<String> expectedVariable2Values = automaton.getVariables().get(1).getValues();
        List<String> expectedAccept1Values = new ArrayList<>();

        for(int i = 0; i < expectedVariable1Values.size(); ++i)
            if(Objects.equals(automaton.getVariables().get(0).getInitValue(),
                              expectedVariable1Values.get(i)))
                expectedVariable1Values.set(i, expectedVariable1Values.get(i) + " [init value]");

        for(int i = 0; i < expectedVariable2Values.size(); ++i)
            if(Objects.equals(automaton.getVariables().get(1).getInitValue(),
                              expectedVariable2Values.get(i)))
                expectedVariable2Values.set(i, expectedVariable2Values.get(i) + " [init value]");

        for(Map<Variable, Pair<String, Boolean>> state : automaton.getAcceptingConditions()
                                                                  .getStatesConditions())
            for(Map.Entry<Variable, Pair<String, Boolean>> entry : state.entrySet())
                expectedAccept1Values.add(AcceptingConditions.getAcceptingEntryString(entry));

        Assert.assertEquals(3, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());

        Assert.assertEquals("Alphabet", alphabetChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), alphabetChild.getChildCount());

        Assert.assertEquals("Variables", variableChild.getUserObject());
        Assert.assertEquals(automaton.getVariables().size(), variableChild.getChildCount());

        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());

        Assert.assertEquals(automaton.getVariables().get(0).getVarName(),
                            variable1Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable1Values.toArray(), variable1Values.toArray());

        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            variable2Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable2Values.toArray(), variable2Values.toArray());

        Assert.assertEquals("conditions", accept1Node.getUserObject());
        Assert.assertArrayEquals(expectedAccept1Values.toArray(), accept1Values.toArray());

        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();
        TopDownDFTA automaton = new TopDownDFTA(variables, alphabet);

        accept.put(variables.get(0), Pair.make("T", true));
        accept.put(variables.get(1), Pair.make("!", false));
        automaton.addAcceptingConditions(accept);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receiveSignal(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode alphabetChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode variableChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode acceptChild = (DefaultMutableTreeNode)rootNode.getChildAt(2);

        DefaultMutableTreeNode variable1Node = (DefaultMutableTreeNode)variableChild.getChildAt(0);
        DefaultMutableTreeNode variable2Node = (DefaultMutableTreeNode)variableChild.getChildAt(1);
        DefaultMutableTreeNode accept1Node = (DefaultMutableTreeNode)acceptChild.getChildAt(0);

        List<String> alphabetValues = new ArrayList<>();
        List<String> variable1Values = new ArrayList<>();
        List<String> variable2Values = new ArrayList<>();
        List<String> accept1Values = new ArrayList<>();

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

        List<String> expectedVariable1Values = automaton.getVariables().get(0).getValues();
        List<String> expectedVariable2Values = automaton.getVariables().get(1).getValues();
        List<String> expectedAccept1Values = new ArrayList<>();

        for(int i = 0; i < expectedVariable1Values.size(); i++)
            if(Objects.equals(automaton.getVariables().get(0).getInitValue(),
                              expectedVariable1Values.get(i)))
                expectedVariable1Values.set(i, expectedVariable1Values.get(i) + " [init value]");

        for(int i = 0; i < expectedVariable2Values.size(); i++)
            if(Objects.equals(automaton.getVariables().get(1).getInitValue(),
                              expectedVariable2Values.get(i)))
                expectedVariable2Values.set(i, expectedVariable2Values.get(i) + " [init value]");

        for(Map<Variable, Pair<String, Boolean>> state : automaton.getAcceptingConditions()
                                                                  .getStatesConditions())
            for(Map.Entry<Variable, Pair<String, Boolean>> entry : state.entrySet())
                expectedAccept1Values.add(AcceptingConditions.getAcceptingEntryString(entry));

        Assert.assertEquals(3, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());

        Assert.assertEquals("Alphabet", alphabetChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), alphabetChild.getChildCount());

        Assert.assertEquals("Variables", variableChild.getUserObject());
        Assert.assertEquals(automaton.getVariables().size(), variableChild.getChildCount());

        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());

        Assert.assertEquals(automaton.getVariables().get(0).getVarName(),
                            variable1Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable1Values.toArray(), variable1Values.toArray());

        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            variable2Node.getUserObject());
        Assert.assertArrayEquals(expectedVariable2Values.toArray(), variable2Values.toArray());

        Assert.assertEquals("conditions", accept1Node.getUserObject());
        Assert.assertArrayEquals(expectedAccept1Values.toArray(), accept1Values.toArray());

        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }
}
