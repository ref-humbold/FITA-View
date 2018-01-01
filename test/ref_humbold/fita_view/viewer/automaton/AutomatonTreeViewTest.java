package ref_humbold.fita_view.viewer.automaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.*;
import ref_humbold.fita_view.message.Message;

@RunWith(MockitoJUnitRunner.class)
public class AutomatonTreeViewTest
{
    private TreeAutomaton automaton;

    @Spy
    private Pointer<TreeAutomaton> mockPointer = new Pointer<>(NullAutomaton.getInstance());

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
        automaton = null;
    }

    @Test
    public void testReceiveWhenNullAutomaton()
    {
        automaton = NullAutomaton.getInstance();

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receive(mockMessage);

        DefaultMutableTreeNode rootNode = testObject.rootNode;
        DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode)rootNode.getChildAt(0);
        DefaultMutableTreeNode secondChild = (DefaultMutableTreeNode)rootNode.getChildAt(1);

        Assert.assertEquals(2, rootNode.getChildCount());
        Assert.assertEquals(automaton.getTypeName(), rootNode.getUserObject());
        Assert.assertEquals("Alphabet", firstChild.getUserObject());
        Assert.assertEquals(automaton.getAlphabet().size(), firstChild.getChildCount());
        Assert.assertEquals("Variables", secondChild.getUserObject());
        Assert.assertEquals(automaton.getVariables().size(), secondChild.getChildCount());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(6)).get();
    }

    @Test
    public void testReceiveWhenBottomUpDFTA()
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

        automaton = new BottomUpDFTA(variables, alphabet);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receive(mockMessage);

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
        Assert.assertArrayEquals(automaton.getVariables().get(0).getValues().toArray(),
                                 firstVariableValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            secondVariableNode.getUserObject());
        Assert.assertArrayEquals(automaton.getVariables().get(1).getValues().toArray(),
                                 secondVariableValues.toArray());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(6)).get();
    }

    @Test
    public void testReceiveWhenTopDownDFTA()
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

        automaton = new TopDownDFTA(variables, alphabet);

        Mockito.when(mockPointer.get()).thenReturn(automaton);

        testObject.receive(mockMessage);

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
        Assert.assertArrayEquals(automaton.getVariables().get(0).getValues().toArray(),
                                 firstVariableValues.toArray());
        Assert.assertEquals(automaton.getVariables().get(1).getVarName(),
                            secondVariableNode.getUserObject());
        Assert.assertArrayEquals(automaton.getVariables().get(1).getValues().toArray(),
                                 secondVariableValues.toArray());
        Mockito.verify(mockPointer, Mockito.never()).set(Matchers.any());
        Mockito.verify(mockPointer, Mockito.times(6)).get();
    }
}
