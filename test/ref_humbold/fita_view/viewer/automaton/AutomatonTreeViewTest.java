package ref_humbold.fita_view.viewer.automaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        BottomUpDFTA automaton = new BottomUpDFTA(variables, alphabet);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receiveSignal(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode secondChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode firstVariableNode = (DefaultMutableTreeNode)secondChild.getChildAt(
            0);
        DefaultMutableTreeNode secondVariableNode = (DefaultMutableTreeNode)secondChild.getChildAt(
            1);
        List<String> alphabetValues = new ArrayList<>();
        List<String> firstVariableValues = new ArrayList<>();
        List<String> secondVariableValues = new ArrayList<>();
        List<String> expectedFirstValues = automaton.getVariables().get(0).getValues();
        List<String> expectedSecondValues = automaton.getVariables().get(1).getValues();

        for(int i = 0; i < expectedFirstValues.size(); i++)
        {
            if(Objects.equals(automaton.getVariables().get(0).getInitValue(),
                              expectedFirstValues.get(i)))
                expectedFirstValues.set(i, expectedFirstValues.get(i) + " [init value]");
        }

        for(int i = 0; i < expectedSecondValues.size(); i++)
        {
            if(Objects.equals(automaton.getVariables().get(1).getInitValue(),
                              expectedSecondValues.get(i)))
                expectedSecondValues.set(i, expectedSecondValues.get(i) + " [init value]");
        }

        for(int i = 0; i < firstChild.getChildCount(); ++i)
            alphabetValues.add(
                (String)((DefaultMutableTreeNode)firstChild.getChildAt(i)).getUserObject());

        for(int i = 0; i < firstVariableNode.getChildCount(); ++i)
            firstVariableValues.add(
                (String)((DefaultMutableTreeNode)firstVariableNode.getChildAt(i)).getUserObject());

        for(int i = 0; i < secondVariableNode.getChildCount(); ++i)
            secondVariableValues.add(
                (String)((DefaultMutableTreeNode)secondVariableNode.getChildAt(i)).getUserObject());

        Assert.assertEquals(2, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());
        Assert.assertEquals("Alphabet", firstChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), firstChild.getChildCount());
        Assert.assertEquals("Variables", secondChild.getUserObject());
        Assert.assertEquals(automaton.getVariables().size(), secondChild.getChildCount());
        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(0).getVarName(),
                            firstVariableNode.getUserObject());
        Assert.assertArrayEquals(expectedFirstValues.toArray(), firstVariableValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            secondVariableNode.getUserObject());
        Assert.assertArrayEquals(expectedSecondValues.toArray(), secondVariableValues.toArray());
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

        TopDownDFTA automaton = new TopDownDFTA(variables, alphabet);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receiveSignal(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode secondChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);
        DefaultMutableTreeNode firstVariableNode = (DefaultMutableTreeNode)secondChild.getChildAt(
            0);
        DefaultMutableTreeNode secondVariableNode = (DefaultMutableTreeNode)secondChild.getChildAt(
            1);
        List<String> alphabetValues = new ArrayList<>();
        List<String> firstVariableValues = new ArrayList<>();
        List<String> secondVariableValues = new ArrayList<>();
        List<String> expectedFirstValues = automaton.getVariables().get(0).getValues();
        List<String> expectedSecondValues = automaton.getVariables().get(1).getValues();

        for(int i = 0; i < expectedFirstValues.size(); i++)
        {
            if(Objects.equals(automaton.getVariables().get(0).getInitValue(),
                              expectedFirstValues.get(i)))
                expectedFirstValues.set(i, expectedFirstValues.get(i) + " [init value]");
        }

        for(int i = 0; i < expectedSecondValues.size(); i++)
        {
            if(Objects.equals(automaton.getVariables().get(1).getInitValue(),
                              expectedSecondValues.get(i)))
                expectedSecondValues.set(i, expectedSecondValues.get(i) + " [init value]");
        }

        for(int i = 0; i < firstChild.getChildCount(); ++i)
            alphabetValues.add(
                (String)((DefaultMutableTreeNode)firstChild.getChildAt(i)).getUserObject());

        for(int i = 0; i < firstVariableNode.getChildCount(); ++i)
            firstVariableValues.add(
                (String)((DefaultMutableTreeNode)firstVariableNode.getChildAt(i)).getUserObject());

        for(int i = 0; i < secondVariableNode.getChildCount(); ++i)
            secondVariableValues.add(
                (String)((DefaultMutableTreeNode)secondVariableNode.getChildAt(i)).getUserObject());

        Assert.assertEquals(2, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());
        Assert.assertEquals("Alphabet", firstChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), firstChild.getChildCount());
        Assert.assertEquals("Variables", secondChild.getUserObject());
        Assert.assertEquals(automaton.getVariables().size(), secondChild.getChildCount());
        Assert.assertArrayEquals(automaton.getAlphabet().toArray(), alphabetValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(0).getVarName(),
                            firstVariableNode.getUserObject());
        Assert.assertArrayEquals(expectedFirstValues.toArray(), firstVariableValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            secondVariableNode.getUserObject());
        Assert.assertArrayEquals(expectedSecondValues.toArray(), secondVariableValues.toArray());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(2)).get();
    }
}
