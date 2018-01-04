package ref_humbold.fita_view.automaton;

import java.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.tree.*;

public class TopDownDFTATest
{
    private TopDownDFTA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private List<Map<Variable, String>> accepts = Arrays.asList(new HashMap<>(), new HashMap<>(),
                                                                new HashMap<>());

    public TopDownDFTATest()
        throws Exception
    {
        variables = Arrays.asList(new Variable(1, "A", "B"), new Variable(2, "!", "@", "#", "$"));
        accepts.get(0).put(variables.get(0), "A");
        accepts.get(0).put(variables.get(1), "@");
        accepts.get(1).put(variables.get(0), "B");
        accepts.get(1).put(variables.get(1), "$");
        accepts.get(2).put(variables.get(0), Wildcard.EVERY_VALUE);
        accepts.get(2).put(variables.get(1), "#");
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
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
        TreeNode node13 = null, node12 = null, node11 = null, node10 = null, node7 = null, node6 = null, node5 = null, node4 = null, node3 = null, node2 = null, node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
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

        Assert.assertFalse(testObject.isRunning);
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

    @Test(expected = NoTraversingException.class)
    public void testRunWhenNoTraversing()
        throws NoTraversingException
    {
        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.run();
        }
        catch(EmptyTreeException | IllegalVariableValueException | NoSuchTransitionException | UndefinedTreeStateException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = EmptyTreeException.class)
    public void testRunWhenNoTree()
        throws EmptyTreeException
    {
        testObject.setTraversing(TraversingMode.LEVEL);

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.run();
        }
        catch(NoTraversingException | IllegalVariableValueException | NoSuchTransitionException | UndefinedTreeStateException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testMakeStepForward()
    {
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
        TreeNode node13 = null, node12 = null, node11 = null, node10 = null, node7 = null, node6 = null, node5 = null, node4 = null, node3 = null, node2 = null, node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(testObject.isRunning);
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(testObject.isRunning);
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
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

        Assert.assertTrue(testObject.isRunning);
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
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

        Assert.assertFalse(testObject.isRunning);
        Assert.assertEquals(12, testObject.leafStates.size());
        Assert.assertArrayEquals(leavesResults.toArray(), testObject.leafStates.toArray());
    }

    @Test(expected = NoTraversingException.class)
    public void testMakeStepForwardWhenNoTraversing()
        throws NoTraversingException
    {
        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.makeStepForward();
        }
        catch(EmptyTreeException | IllegalVariableValueException | NoSuchTransitionException | UndefinedTreeStateException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = EmptyTreeException.class)
    public void testMakeStepForwardWhenNoTree()
        throws EmptyTreeException
    {
        testObject.setTraversing(TraversingMode.LEVEL);

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.makeStepForward();
        }
        catch(NoTraversingException | IllegalVariableValueException | NoSuchTransitionException | UndefinedTreeStateException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testMakeStepForwardThenRun()
    {
        List<Map<Variable, String>> leavesResults = new ArrayList<>();
        TreeNode node13 = null, node12 = null, node11 = null, node10 = null, node7 = null, node6 = null, node5 = null, node4 = null, node3 = null, node2 = null, node1 = null;

        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(testObject.isRunning);

        try
        {
            testObject.makeStepForward();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertTrue(testObject.isRunning);
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
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

        Assert.assertFalse(testObject.isRunning);
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
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = false;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedTreeStateException e)
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
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
                                                                                           "0", 11,
                                                                                           null,
                                                                                           null),
                                                                                       new StandardNode(
                                                                                           "4", 10,
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

        boolean result = true;

        try
        {
            result = testObject.isAccepted();
        }
        catch(UndefinedAcceptanceException | UndefinedTreeStateException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertFalse(result);
    }

    @Test(expected = UndefinedTreeStateException.class)
    public void testIsAcceptedWhenAutomatonHasNotRun()
        throws UndefinedTreeStateException
    {
        try
        {
            testObject.setTraversing(TraversingMode.LEVEL);
            testObject.addAcceptingState(accepts.get(0));
            testObject.addAcceptingState(accepts.get(1));
            testObject.addAcceptingState(accepts.get(2));

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
                                                                                           "0", 11,
                                                                                           null,
                                                                                           null),
                                                                                       new StandardNode(
                                                                                           "4", 10,
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
        catch(UndefinedAcceptanceException e)
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
            testObject.isAccepted();
        }
        catch(UndefinedTreeStateException e)
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
}
