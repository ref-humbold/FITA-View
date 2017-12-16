package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTATest
{
    private BottomUpDFTA testObject;
    private Variable variable;
    private List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");

    private TreeVertex finiteTreeNode13a = new NodeVertex("1", 13, null, null);
    private TreeVertex finiteTreeNode12a = new NodeVertex("1", 12, null, null);
    private TreeVertex finiteTreeNode11a = new NodeVertex("1", 11, null, null);
    private TreeVertex finiteTreeNode10a = new NodeVertex("0", 10, null, null);
    private TreeVertex finiteTreeNode7a = new NodeVertex("1", 7, null, null);
    private TreeVertex finiteTreeNode6a =
        new NodeVertex("or", 6, finiteTreeNode13a, finiteTreeNode12a);
    private TreeVertex finiteTreeNode5a =
        new NodeVertex("and", 5, finiteTreeNode11a, finiteTreeNode10a);
    private TreeVertex finiteTreeNode4a = new NodeVertex("0", 4, null, null);
    private TreeVertex finiteTreeNode3a =
        new NodeVertex("and", 3, finiteTreeNode7a, finiteTreeNode6a);
    private TreeVertex finiteTreeNode2a =
        new NodeVertex("or", 2, finiteTreeNode5a, finiteTreeNode4a);
    private TreeVertex finiteTreeNode1a =
        new NodeVertex("impl", 1, finiteTreeNode3a, finiteTreeNode2a);

    private TreeVertex finiteTreeNode13b = new NodeVertex("1", 13, null, null);
    private TreeVertex finiteTreeNode12b = new NodeVertex("1", 12, null, null);
    private TreeVertex finiteTreeNode11b = new NodeVertex("1", 11, null, null);
    private TreeVertex finiteTreeNode10b = new NodeVertex("1", 10, null, null);
    private TreeVertex finiteTreeNode7b = new NodeVertex("1", 7, null, null);
    private TreeVertex finiteTreeNode6b =
        new NodeVertex("or", 6, finiteTreeNode13b, finiteTreeNode12b);
    private TreeVertex finiteTreeNode5b =
        new NodeVertex("and", 5, finiteTreeNode11b, finiteTreeNode10b);
    private TreeVertex finiteTreeNode4b = new NodeVertex("0", 4, null, null);
    private TreeVertex finiteTreeNode3b =
        new NodeVertex("and", 3, finiteTreeNode7b, finiteTreeNode6b);
    private TreeVertex finiteTreeNode2b =
        new NodeVertex("or", 2, finiteTreeNode5b, finiteTreeNode4b);
    private TreeVertex finiteTreeNode1b =
        new NodeVertex("impl", 1, finiteTreeNode3b, finiteTreeNode2b);

    public BottomUpDFTATest()
        throws Exception
    {
        variable = new Variable("X", "T", "F");
    }

    @Before
    public void setUp()
        throws Exception
    {
        testObject = new BottomUpDFTA(alphabet, Collections.singletonList(variable));
        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addTransition(variable, "X", "X", "0", "F");
        testObject.addTransition(variable, "X", "X", "1", "T");
        testObject.addTransition(variable, "T", "T", "and", "T");
        testObject.addTransition(variable, "F", "(*)", "and", "F");
        testObject.addTransition(variable, "(*)", "F", "and", "F");
        testObject.addTransition(variable, "T", "(*)", "or", "T");
        testObject.addTransition(variable, "(*)", "T", "or", "T");
        testObject.addTransition(variable, "F", "F", "or", "F");
        testObject.addTransition(variable, "T", "(*)", "impl", "(=^)");
        testObject.addTransition(variable, "F", "(*)", "impl", "T");
        testObject.addAcceptingState(Collections.singletonMap(variable, "T"));
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testRun()
    {
        testObject.setTree(finiteTreeNode1a);

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.run();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(testObject.isRunning);
        Assert.assertEquals("T", finiteTreeNode13a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode12a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode11a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode10a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode7a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode6a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode5a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode4a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode3a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode2a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode1a.getState(variable));
    }

    @Test
    public void testMakeStepForward()
    {
        testObject.setTree(finiteTreeNode1a);

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.makeStepForward();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(testObject.isRunning);
        Assert.assertEquals("T", finiteTreeNode13a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode12a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode11a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode10a.getState(variable));

        try
        {
            testObject.makeStepForward();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(testObject.isRunning);
        Assert.assertEquals("T", finiteTreeNode7a.getState(variable));
        Assert.assertEquals("T", finiteTreeNode6a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode5a.getState(variable));
        Assert.assertEquals("F", finiteTreeNode4a.getState(variable));
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndNotAccepts()
    {
        testObject.setTree(finiteTreeNode1a);

        try
        {
            testObject.run();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndAccepts()
    {
        testObject.setTree(finiteTreeNode1b);

        try
        {
            testObject.run();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(result);
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasNotRun()
    {
        testObject.setTree(finiteTreeNode1a);

        boolean result = true;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test
    public void testIsInAlphabetWhenValueInAlphabet()
    {
        boolean result0 = testObject.isInAlphabet("0");
        boolean result1 = testObject.isInAlphabet("1");
        boolean resultAnd = testObject.isInAlphabet("and");
        boolean resultOr = testObject.isInAlphabet("or");
        boolean resultImpl = testObject.isInAlphabet("impl");

        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(resultAnd);
        Assert.assertTrue(resultOr);
        Assert.assertTrue(resultImpl);
    }

    @Test
    public void testIsInAlphabetWhenValueOutOfAlphabet()
    {
        boolean result2 = testObject.isInAlphabet("2");
        boolean resultIff = testObject.isInAlphabet("iff");

        Assert.assertFalse(result2);
        Assert.assertFalse(resultIff);
    }
}
