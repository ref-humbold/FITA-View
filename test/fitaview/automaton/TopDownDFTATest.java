package fitaview.automaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.automaton.transition.NoSuchTransitionException;
import fitaview.automaton.traversing.TraversingMode;
import fitaview.tree.*;
import fitaview.utils.Pair;

public class TopDownDFTATest
{
    private TopDownDFTA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private List<Map<Variable, Pair<String, Boolean>>> accepts =
            Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>());

    public TopDownDFTATest()
            throws Exception
    {
        variables = Arrays.asList(new Variable(1, "A", "B"), new Variable(2, "!", "@", "#", "$"));
        accepts.get(0).put(variables.get(0), Pair.make("A", true));
        accepts.get(0).put(variables.get(1), Pair.make("@", true));
        accepts.get(1).put(variables.get(0), Pair.make("B", true));
        accepts.get(1).put(variables.get(1), Pair.make("$", true));
        accepts.get(2).put(variables.get(0), Pair.make(Wildcard.EVERY_VALUE, true));
        accepts.get(2).put(variables.get(1), Pair.make("#", true));
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new TopDownDFTA(variables, alphabet);
        testObject.addTransition(variables.get(0), "A", "0", "A", "B");
        testObject.addTransition(variables.get(0), "A", "1", "A", "A");
        testObject.addTransition(variables.get(0), "A", "2", "B", "B");
        testObject.addTransition(variables.get(0), "A", "3", "A", "A");
        testObject.addTransition(variables.get(0), "A", "4", "B", "B");
        testObject.addTransition(variables.get(0), "B", "0", "B", "A");
        testObject.addTransition(variables.get(0), "B", "1", "B", "B");
        testObject.addTransition(variables.get(0), "B", "2", "A", "A");
        testObject.addTransition(variables.get(0), "B", "3", "B", "B");
        testObject.addTransition(variables.get(0), "B", "4", "A", "A");
        testObject.addTransition(variables.get(1), "!", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "!", "1", "!", "@");
        testObject.addTransition(variables.get(1), "!", "2", "@", "#");
        testObject.addTransition(variables.get(1), "!", "3", "#", "$");
        testObject.addTransition(variables.get(1), "!", "4", "$", "!");
        testObject.addTransition(variables.get(1), "@", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "@", "1", "@", "#");
        testObject.addTransition(variables.get(1), "@", "2", "#", "$");
        testObject.addTransition(variables.get(1), "@", "3", "$", "!");
        testObject.addTransition(variables.get(1), "@", "4", "!", "@");
        testObject.addTransition(variables.get(1), "#", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "#", "1", "#", "$");
        testObject.addTransition(variables.get(1), "#", "2", "$", "!");
        testObject.addTransition(variables.get(1), "#", "3", "!", "@");
        testObject.addTransition(variables.get(1), "#", "4", "@", "#");
        testObject.addTransition(variables.get(1), "$", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "$", "1", "$", "!");
        testObject.addTransition(variables.get(1), "$", "2", "!", "@");
        testObject.addTransition(variables.get(1), "$", "3", "@", "#");
        testObject.addTransition(variables.get(1), "$", "4", "#", "$");
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

        Assert.assertEquals("Top-down deterministic finite tree automaton", result);
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
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(testObject.tree);
        Assert.assertSame(node, testObject.tree);
    }

    @Test
    public void testSetTreeWhenEmptyTree()
    {
        try
        {
            testObject.setTree(null);
        }
        catch(TreeFinitenessException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNull(testObject.tree);
    }

    @Test(expected = TreeFinitenessException.class)
    public void testSetTreeWhenInfiniteTree()
            throws TreeFinitenessException
    {
        try
        {
            RepeatNode node2 = new RepeatNode("0", 2);
            TreeNode node4 = new StandardNode("1", 4);
            TreeNode node5 =
                    new StandardNode("3", 5, new StandardNode("1", 11), new RecNode(node2, 10));
            TreeNode node1 = new StandardNode("2", 1, new StandardNode("0", 3), node2);

            node2.setLeft(node5);
            node2.setRight(node4);

            testObject.setTree(node1);
        }
        catch(NodeHasParentException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testRun()
    {
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
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
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            node13 = new StandardNode("2", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("0", 11);
            node10 = new StandardNode("4", 10);
            node7 = new StandardNode("3", 7);
            node6 = new StandardNode("3", 6, node13, node12);
            node5 = new StandardNode("2", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("4", 3, node7, node6);
            node2 = new StandardNode("1", 2, node5, node4);
            node1 = new StandardNode("2", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        leavesResults.add(new HashMap<>());
        leavesResults.get(0).put(variables.get(0), "A");
        leavesResults.get(0).put(variables.get(1), "#");
        leavesResults.add(new HashMap<>());
        leavesResults.get(1).put(variables.get(0), "A");
        leavesResults.get(1).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(2).put(variables.get(0), "B");
        leavesResults.get(2).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(3).put(variables.get(0), "A");
        leavesResults.get(3).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(4).put(variables.get(0), "B");
        leavesResults.get(4).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(5).put(variables.get(0), "B");
        leavesResults.get(5).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(6).put(variables.get(0), "A");
        leavesResults.get(6).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(7).put(variables.get(0), "A");
        leavesResults.get(7).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(8).put(variables.get(0), "A");
        leavesResults.get(8).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(9).put(variables.get(0), "B");
        leavesResults.get(9).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(10).put(variables.get(0), "B");
        leavesResults.get(10).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(11).put(variables.get(0), "B");
        leavesResults.get(11).put(variables.get(1), "!");

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals("A", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertArrayEquals(leavesResults.toArray(), testObject.leafStates.toArray());
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
        catch(NoTreeException | IllegalVariableValueException | NoSuchTransitionException |
              UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test(expected = NoTreeException.class)
    public void testRunWhenNoTree()
            throws NoTreeException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
        }
        catch(AutomatonIsRunningException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.run();
        }
        catch(NoTraversingStrategyException | IllegalVariableValueException |
              NoSuchTransitionException | UndefinedStateValueException |
              NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testMakeStepForward()
    {
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
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
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            node13 = new StandardNode("2", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("0", 11);
            node10 = new StandardNode("4", 10);
            node7 = new StandardNode("3", 7);
            node6 = new StandardNode("3", 6, node13, node12);
            node5 = new StandardNode("2", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("4", 3, node7, node6);
            node2 = new StandardNode("1", 2, node5, node4);
            node1 = new StandardNode("2", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("A", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(1)));
        Assert.assertTrue(testObject.leafStates.isEmpty());

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("B", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(1)));
        Assert.assertTrue(testObject.leafStates.isEmpty());

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        leavesResults.add(new HashMap<>());
        leavesResults.get(0).put(variables.get(0), "A");
        leavesResults.get(0).put(variables.get(1), "#");
        leavesResults.add(new HashMap<>());
        leavesResults.get(1).put(variables.get(0), "A");
        leavesResults.get(1).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(2).put(variables.get(0), "B");
        leavesResults.get(2).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(3).put(variables.get(0), "A");
        leavesResults.get(3).put(variables.get(1), "$");

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("A", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals(4, testObject.leafStates.size());
        Assert.assertArrayEquals(leavesResults.toArray(), testObject.leafStates.toArray());

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        leavesResults.add(new HashMap<>());
        leavesResults.get(4).put(variables.get(0), "B");
        leavesResults.get(4).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(5).put(variables.get(0), "B");
        leavesResults.get(5).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(6).put(variables.get(0), "A");
        leavesResults.get(6).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(7).put(variables.get(0), "A");
        leavesResults.get(7).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(8).put(variables.get(0), "A");
        leavesResults.get(8).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(9).put(variables.get(0), "B");
        leavesResults.get(9).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(10).put(variables.get(0), "B");
        leavesResults.get(10).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(11).put(variables.get(0), "B");
        leavesResults.get(11).put(variables.get(1), "!");

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals(12, testObject.leafStates.size());
        Assert.assertArrayEquals(leavesResults.toArray(), testObject.leafStates.toArray());
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
        catch(NoTreeException | IllegalVariableValueException | NoSuchTransitionException |
              UndefinedStateValueException | NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test(expected = NoTreeException.class)
    public void testMakeStepForwardWhenNoTree()
            throws NoTreeException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
        }
        catch(AutomatonIsRunningException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(NoTraversingStrategyException | IllegalVariableValueException |
              NoSuchTransitionException | UndefinedStateValueException |
              NoNonDeterministicStrategyException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testMakeStepForwardThenRun()
    {
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
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
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            node13 = new StandardNode("2", 13);
            node12 = new StandardNode("1", 12);
            node11 = new StandardNode("0", 11);
            node10 = new StandardNode("4", 10);
            node7 = new StandardNode("3", 7);
            node6 = new StandardNode("3", 6, node13, node12);
            node5 = new StandardNode("2", 5, node11, node10);
            node4 = new StandardNode("0", 4);
            node3 = new StandardNode("4", 3, node7, node6);
            node2 = new StandardNode("1", 2, node5, node4);
            node1 = new StandardNode("2", 1, node3, node2);

            testObject.setTree(node1);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.STOPPED, testObject.runningMode);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertEquals(AutomatonRunningMode.RUNNING, testObject.runningMode);
        Assert.assertEquals("A", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node4.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node5.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node6.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node7.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node10.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node11.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node12.getStateValueOrNull(variables.get(1)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(0)));
        Assert.assertNull(node13.getStateValueOrNull(variables.get(1)));
        Assert.assertTrue(testObject.leafStates.isEmpty());

        try
        {
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        leavesResults.add(new HashMap<>());
        leavesResults.get(0).put(variables.get(0), "A");
        leavesResults.get(0).put(variables.get(1), "#");
        leavesResults.add(new HashMap<>());
        leavesResults.get(1).put(variables.get(0), "A");
        leavesResults.get(1).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(2).put(variables.get(0), "B");
        leavesResults.get(2).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(3).put(variables.get(0), "A");
        leavesResults.get(3).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(4).put(variables.get(0), "B");
        leavesResults.get(4).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(5).put(variables.get(0), "B");
        leavesResults.get(5).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(6).put(variables.get(0), "A");
        leavesResults.get(6).put(variables.get(1), "!");
        leavesResults.add(new HashMap<>());
        leavesResults.get(7).put(variables.get(0), "A");
        leavesResults.get(7).put(variables.get(1), "@");
        leavesResults.add(new HashMap<>());
        leavesResults.get(8).put(variables.get(0), "A");
        leavesResults.get(8).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(9).put(variables.get(0), "B");
        leavesResults.get(9).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(10).put(variables.get(0), "B");
        leavesResults.get(10).put(variables.get(1), "$");
        leavesResults.add(new HashMap<>());
        leavesResults.get(11).put(variables.get(0), "B");
        leavesResults.get(11).put(variables.get(1), "!");

        Assert.assertEquals(AutomatonRunningMode.FINISHED, testObject.runningMode);
        Assert.assertEquals("A", node1.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node4.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node4.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("B", node5.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("#", node5.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node6.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("@", node6.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node7.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node10.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node11.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node11.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node12.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateValueOrNull(variables.get(1)));
        Assert.assertEquals("A", node13.getStateValueOrNull(variables.get(0)));
        Assert.assertEquals("$", node13.getStateValueOrNull(variables.get(1)));
        Assert.assertArrayEquals(leavesResults.toArray(), testObject.leafStates.toArray());
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndAccepts()
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            TreeNode node = new StandardNode("0", 1,
                                             new StandardNode("3", 3, new StandardNode("0", 7),
                                                              new StandardNode("4", 6)),
                                             new StandardNode("2", 2));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertTrue(result);
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndNotAccepts()
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            TreeNode node = new StandardNode("2", 1,
                                             new StandardNode("4", 3, new StandardNode("3", 7),
                                                              new StandardNode("3", 6,
                                                                               new StandardNode("2",
                                                                                                13,
                                                                                                null,
                                                                                                null),
                                                                               new StandardNode("1",
                                                                                                12,
                                                                                                null,
                                                                                                null))),
                                             new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                       new StandardNode(
                                                                                               "0",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                       new StandardNode(
                                                                                               "4",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        boolean result = true;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedStateValueException | NoTreeException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
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
            testObject.addAcceptanceConditions(accepts.get(0));
            testObject.addAcceptanceConditions(accepts.get(1));
            testObject.addAcceptanceConditions(accepts.get(2));

            TreeNode node = new StandardNode("2", 1,
                                             new StandardNode("4", 3, new StandardNode("3", 7),
                                                              new StandardNode("3", 6,
                                                                               new StandardNode("2",
                                                                                                13,
                                                                                                null,
                                                                                                null),
                                                                               new StandardNode("1",
                                                                                                12,
                                                                                                null,
                                                                                                null))),
                                             new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                       new StandardNode(
                                                                                               "0",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                       new StandardNode(
                                                                                               "4",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | NoTreeException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test(expected = UndefinedAcceptanceException.class)
    public void testIsAcceptedWhenAutomatonHasNoAcceptingStates()
            throws UndefinedAcceptanceException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);

            TreeNode node = new StandardNode("2", 1,
                                             new StandardNode("4", 3, new StandardNode("3", 7),
                                                              new StandardNode("3", 6,
                                                                               new StandardNode("2",
                                                                                                13,
                                                                                                null,
                                                                                                null),
                                                                               new StandardNode("1",
                                                                                                12,
                                                                                                null,
                                                                                                null))),
                                             new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                       new StandardNode(
                                                                                               "0",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                       new StandardNode(
                                                                                               "4",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                              new StandardNode("0", 4)));

            testObject.setTree(node);
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedStateValueException | NoTreeException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test(expected = NoTreeException.class)
    public void testIsAcceptedWhenAutomatonHasEmptyTree()
            throws NoTreeException
    {
        try
        {
            testObject.isAccepted();
        }
        catch(UndefinedStateValueException | UndefinedAcceptanceException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testIsInAlphabetWhenValueInAlphabet()
    {
        boolean result0 = testObject.isInAlphabet("0");
        boolean result1 = testObject.isInAlphabet("1");
        boolean result2 = testObject.isInAlphabet("2");
        boolean result3 = testObject.isInAlphabet("3");
        boolean result4 = testObject.isInAlphabet("4");

        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
        Assert.assertTrue(result4);
    }

    @Test
    public void testIsInAlphabetWhenValueOutOfAlphabet()
    {
        boolean result5 = testObject.isInAlphabet("5");
        boolean resultA = testObject.isInAlphabet("A");

        Assert.assertFalse(result5);
        Assert.assertFalse(resultA);
    }

    @Test
    public void testGetTransitionWithStrings()
    {
        Map<Pair<Variable, String>, String> result = testObject.getTransitionAsStrings();
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "0"))),
                     testObject.valueToString(Pair.make("A", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "1"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "2"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "3"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "4"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "0"))),
                     testObject.valueToString(Pair.make("B", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "1"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "2"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "3"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "4"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "1"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "2"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "3"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "4"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "1"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "2"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "3"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "4"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "1"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "2"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "3"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "4"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "1"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "2"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "3"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "4"))),
                     testObject.valueToString(Pair.make("#", "$")));

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }
}
