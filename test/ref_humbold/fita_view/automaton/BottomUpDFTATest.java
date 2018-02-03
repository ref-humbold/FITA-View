package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.*;

public class BottomUpDFTATest
{
    private BottomUpDFTA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
    private Map<Variable, Pair<String, Boolean>> accepts = new HashMap<>();

    public BottomUpDFTATest()
        throws Exception
    {
        variables = Arrays.asList(new Variable(1, "X", "T", "F"),
                                  new Variable(2, "#", "!", "@", "$", "&"));
        accepts.put(variables.get(0), Pair.make("T", true));
        accepts.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));
    }

    @Before
    public void setUp()
        throws Exception
    {
        testObject = new BottomUpDFTA(variables, alphabet);
        testObject.addTransition(variables.get(0), "X", "X", "0", "F");
        testObject.addTransition(variables.get(0), "X", "X", "1", "T");
        testObject.addTransition(variables.get(0), "T", "T", "and", "T");
        testObject.addTransition(variables.get(0), "F", Wildcard.EVERY_VALUE, "and",
                                 Wildcard.LEFT_VALUE);
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "F", "and",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "or",
                                 Wildcard.LEFT_VALUE);
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "T", "or",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "F", "F", "or", "F");
        testObject.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "impl",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "F", Wildcard.EVERY_VALUE, "impl", "T");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "0",
                                 "!");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "1",
                                 "!");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                 "and", "&");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "or",
                                 "$");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                 "impl", "@");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testGetTypeName()
    {
        String result = testObject.getTypeName();

        Assert.assertEquals("Bottom-up deterministic finite tree automaton", result);
    }

    @Test
    public void testSetTreeWhenFiniteTree()
    {
        TreeNode node = null;

        try
        {
            node = new StandardNode("and", 1, new StandardNode("1", 3),
                                    new StandardNode("or", 2, new StandardNode("0", 5),
                                                     new StandardNode("1", 4)));

            testObject.setTree(node);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(testObject.tree);
        Assert.assertSame(node, testObject.tree);
    }

    @Test(expected = EmptyTreeException.class)
    public void testSetTreeWhenEmptyTree()
        throws EmptyTreeException
    {
        try
        {
            testObject.setTree(null);
        }
        catch(TreeFinitenessException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = TreeFinitenessException.class)
    public void testSetTreeWhenInfiniteTree()
        throws TreeFinitenessException
    {
        try
        {
            RepeatNode node2 = new RepeatNode("0", 2);
            TreeNode node4 = new StandardNode("1", 4);
            TreeNode node5 = new StandardNode("3", 5, new StandardNode("1", 11),
                                              new RecNode(node2, 10));
            TreeNode node1 = new StandardNode("2", 1, new StandardNode("0", 3), node2);

            node2.setLeft(node5);
            node2.setRight(node4);

            testObject.setTree(node1);
        }
        catch(NodeHasParentException | EmptyTreeException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testRun()
    {
        TreeNode node13 = null;
        TreeNode node12 = null;
        TreeNode node11 = null;
        TreeNode node10 = null;
        TreeNode node7 = null;
        TreeNode node6 = null;
        TreeNode node5 = null;
        TreeNode node4 = null;
        TreeNode node3 = null;
        TreeNode node2 = null;
        TreeNode node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            node13 = new StandardNode("1", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("1", 11);
            node10 = new StandardNode("0", 10);
            node7 = new StandardNode("1", 7);
            node6 = new StandardNode("or", 6, node13, node12);
            node5 = new StandardNode("and", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("and", 3, node7, node6);
            node2 = new StandardNode("or", 2, node5, node4);
            node1 = new StandardNode("impl", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals("T", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node1.getStateValueOrNull(variables.get(1)));
    }

    @Test(expected = NoTraversingStrategyException.class)
    public void testRunWhenNoTraversing()
        throws NoTraversingStrategyException
    {
        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.run();
        }
        catch(EmptyTreeException | IllegalVariableValueException | NoSuchTransitionException | UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = EmptyTreeException.class)
    public void testRunWhenNoTree()
        throws EmptyTreeException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {

            testObject.run();
        }
        catch(NoTraversingStrategyException | IllegalVariableValueException | NoSuchTransitionException | UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testMakeStepForward()
    {
        TreeNode node13 = null;
        TreeNode node12 = null;
        TreeNode node11 = null;
        TreeNode node10 = null;
        TreeNode node7 = null;
        TreeNode node6 = null;
        TreeNode node5 = null;
        TreeNode node4 = null;
        TreeNode node3 = null;
        TreeNode node2 = null;
        TreeNode node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            node13 = new StandardNode("1", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("1", 11);
            node10 = new StandardNode("0", 10);
            node7 = new StandardNode("1", 7);
            node6 = new StandardNode("or", 6, node13, node12);
            node5 = new StandardNode("and", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("and", 3, node7, node6);
            node2 = new StandardNode("or", 2, node5, node4);
            node1 = new StandardNode("impl", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("T", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(1)));

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("T", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(1)));

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("T", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(1)));

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals("F", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node1.getStateValueOrNull(variables.get(1)));
    }

    @Test(expected = NoTraversingStrategyException.class)
    public void testMakeStepForwardWhenNoTraversing()
        throws NoTraversingStrategyException
    {
        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(EmptyTreeException | IllegalVariableValueException | NoSuchTransitionException | UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = EmptyTreeException.class)
    public void testMakeStepForwardWhenNoTree()
        throws EmptyTreeException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {

            testObject.makeStepForward();
        }
        catch(NoTraversingStrategyException | IllegalVariableValueException | NoSuchTransitionException | UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testMakeStepForwardThenRun()
    {
        TreeNode node13 = null;
        TreeNode node12 = null;
        TreeNode node11 = null;
        TreeNode node10 = null;
        TreeNode node7 = null;
        TreeNode node6 = null;
        TreeNode node5 = null;
        TreeNode node4 = null;
        TreeNode node3 = null;
        TreeNode node2 = null;
        TreeNode node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            node13 = new StandardNode("1", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("1", 11);
            node10 = new StandardNode("0", 10);
            node7 = new StandardNode("1", 7);
            node6 = new StandardNode("or", 6, node13, node12);
            node5 = new StandardNode("and", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("and", 3, node7, node6);
            node2 = new StandardNode("or", 2, node5, node4);
            node1 = new StandardNode("impl", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("T", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node3.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node2.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node1.getStateValueOrNull(variables.get(1)));

        try
        {
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals("T", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("T", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("&", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("F", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node1.getStateValueOrNull(variables.get(1)));
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndAccepts()
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                         new StandardNode("1", 7,
                                                                                          null,
                                                                                          null),
                                                                         new StandardNode("or", 6,
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                             new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                        new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                        new StandardNode(
                                                                                            "1", 10,
                                                                                            null,
                                                                                            null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | EmptyTreeException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(result);
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndNotAccepts()
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                         new StandardNode("1", 7,
                                                                                          null,
                                                                                          null),
                                                                         new StandardNode("or", 6,
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                             new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                        new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                        new StandardNode(
                                                                                            "0", 10,
                                                                                            null,
                                                                                            null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | EmptyTreeException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test(expected = UndefinedStateValueException.class)
    public void testIsAcceptedWhenAutomatonHasNotRun()
        throws UndefinedStateValueException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingConditions(accepts);

            TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                         new StandardNode("1", 7,
                                                                                          null,
                                                                                          null),
                                                                         new StandardNode("or", 6,
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                             new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                        new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                        new StandardNode(
                                                                                            "0", 10,
                                                                                            null,
                                                                                            null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | EmptyTreeException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = UndefinedAcceptanceException.class)
    public void testIsAcceptedWhenAutomatonHasNoAcceptingStates()
        throws UndefinedAcceptanceException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);

            TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                         new StandardNode("1", 7,
                                                                                          null,
                                                                                          null),
                                                                         new StandardNode("or", 6,
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                          new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                             new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                        new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                        new StandardNode(
                                                                                            "1", 10,
                                                                                            null,
                                                                                            null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedStateValueException | EmptyTreeException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = EmptyTreeException.class)
    public void testIsAcceptedWhenAutomatonHasEmptyTree()
        throws EmptyTreeException
    {
        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedStateValueException | UndefinedAcceptanceException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
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

    @Test
    public void testGetTransitionWithStrings()
    {
        Map<Pair<Variable, String>, String> result = testObject.getTransitionAsStrings();
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(
            Pair.make(variables.get(0), testObject.keyToString(Triple.make("X", "X", "0"))),
            testObject.valueToString("F"));
        expected.put(
            Pair.make(variables.get(0), testObject.keyToString(Triple.make("X", "X", "1"))),
            testObject.valueToString("T"));
        expected.put(
            Pair.make(variables.get(0), testObject.keyToString(Triple.make("T", "T", "and"))),
            testObject.valueToString("T"));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make("F", Wildcard.EVERY_VALUE, "and"))),
                     testObject.valueToString(Wildcard.LEFT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, "F", "and"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make("T", Wildcard.EVERY_VALUE, "or"))),
                     testObject.valueToString(Wildcard.LEFT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, "T", "or"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(
            Pair.make(variables.get(0), testObject.keyToString(Triple.make("F", "F", "or"))),
            testObject.valueToString("F"));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make("T", Wildcard.EVERY_VALUE, "impl"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
            Triple.make("F", Wildcard.EVERY_VALUE, "impl"))), testObject.valueToString("T"));

        expected.put(Pair.make(variables.get(1), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "0"))),
                     testObject.valueToString("!"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "1"))),
                     testObject.valueToString("!"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "and"))),
                     testObject.valueToString("&"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "or"))),
                     testObject.valueToString("$"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
            Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "impl"))),
                     testObject.valueToString("@"));

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCheckEmptinessWhenNotEmpty1()
    {
        boolean result = false;

        testObject.addAcceptingConditions(accepts);

        try
        {
            result = testObject.checkEmptiness();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmptinessWhenNotEmpty2()
    {
        boolean result = false;
        Map<Variable, Pair<String, Boolean>> testAccepts = new HashMap<>();

        testAccepts.put(variables.get(0), Pair.make("F", true));
        testAccepts.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));

        testObject.addAcceptingConditions(testAccepts);

        try
        {
            result = testObject.checkEmptiness();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test
    public void testCheckEmptinessWhenEmpty()
    {
        boolean result = false;
        Map<Variable, Pair<String, Boolean>> testAccepts = new HashMap<>();

        testAccepts.put(variables.get(0), Pair.make("X", true));
        testAccepts.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));

        testObject.addAcceptingConditions(testAccepts);

        try
        {
            result = testObject.checkEmptiness();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(result);
    }

    @Test(expected = UndefinedAcceptanceException.class)
    public void testCheckEmptinessWhenNoAcceptance()
        throws UndefinedAcceptanceException
    {
        try
        {
            testObject.checkEmptiness();
        }
        catch(UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }
}
