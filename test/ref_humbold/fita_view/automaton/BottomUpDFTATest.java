package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTATest
{
    private BottomUpDFTA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");

    public BottomUpDFTATest()
        throws Exception
    {
        variables =
            Arrays.asList(new Variable("X", "T", "F"), new Variable("#", "!", "@", "$", "&"));
    }

    @Before
    public void setUp()
        throws Exception
    {
        Map<Variable, String> accepts = new HashMap<>();

        accepts.put(variables.get(0), "T");
        accepts.put(variables.get(1), Wildcard.EVERY_VALUE);

        testObject = new BottomUpDFTA(alphabet, variables);
        testObject.setTraversing(TraversingFactory.Mode.LEVEL);
        testObject.addAcceptingState(accepts);
        testObject.addTransition(variables.get(0), "X", "X", "0", "F");
        testObject.addTransition(variables.get(0), "X", "X", "1", "T");
        testObject.addTransition(variables.get(0), "T", "T", "and", "T");
        testObject.addTransition(variables.get(0), "F", Wildcard.EVERY_VALUE, "and", "F");
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "F", "and", "F");
        testObject.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "or", "T");
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "T", "or", "T");
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
    public void testRun()
    {
        TreeVertex node13 = new NodeVertex("1", 13, null, null);
        TreeVertex node12 = new NodeVertex("1", 12, null, null);
        TreeVertex node11 = new NodeVertex("1", 11, null, null);
        TreeVertex node10 = new NodeVertex("0", 10, null, null);
        TreeVertex node7 = new NodeVertex("1", 7, null, null);
        TreeVertex node6 = new NodeVertex("or", 6, node13, node12);
        TreeVertex node5 = new NodeVertex("and", 5, node11, node10);
        TreeVertex node4 = new NodeVertex("0", 4, null, null);
        TreeVertex node3 = new NodeVertex("and", 3, node7, node6);
        TreeVertex node2 = new NodeVertex("or", 2, node5, node4);
        TreeVertex node1 = new NodeVertex("impl", 1, node3, node2);

        testObject.setTree(node1);

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
        Assert.assertEquals("T", node13.getState(variables.get(0)));
        Assert.assertEquals("!", node13.getState(variables.get(1)));
        Assert.assertEquals("T", node12.getState(variables.get(0)));
        Assert.assertEquals("!", node12.getState(variables.get(1)));
        Assert.assertEquals("T", node11.getState(variables.get(0)));
        Assert.assertEquals("!", node11.getState(variables.get(1)));
        Assert.assertEquals("F", node10.getState(variables.get(0)));
        Assert.assertEquals("!", node10.getState(variables.get(1)));
        Assert.assertEquals("T", node7.getState(variables.get(0)));
        Assert.assertEquals("!", node7.getState(variables.get(1)));
        Assert.assertEquals("T", node6.getState(variables.get(0)));
        Assert.assertEquals("$", node6.getState(variables.get(1)));
        Assert.assertEquals("F", node5.getState(variables.get(0)));
        Assert.assertEquals("&", node5.getState(variables.get(1)));
        Assert.assertEquals("F", node4.getState(variables.get(0)));
        Assert.assertEquals("!", node4.getState(variables.get(1)));
        Assert.assertEquals("T", node3.getState(variables.get(0)));
        Assert.assertEquals("&", node3.getState(variables.get(1)));
        Assert.assertEquals("F", node2.getState(variables.get(0)));
        Assert.assertEquals("$", node2.getState(variables.get(1)));
        Assert.assertEquals("F", node1.getState(variables.get(0)));
        Assert.assertEquals("@", node1.getState(variables.get(1)));
    }

    @Test
    public void testMakeStepForward()
    {
        TreeVertex node13 = new NodeVertex("1", 13, null, null);
        TreeVertex node12 = new NodeVertex("1", 12, null, null);
        TreeVertex node11 = new NodeVertex("1", 11, null, null);
        TreeVertex node10 = new NodeVertex("0", 10, null, null);
        TreeVertex node7 = new NodeVertex("1", 7, null, null);
        TreeVertex node6 = new NodeVertex("or", 6, node13, node12);
        TreeVertex node5 = new NodeVertex("and", 5, node11, node10);
        TreeVertex node4 = new NodeVertex("0", 4, null, null);
        TreeVertex node3 = new NodeVertex("and", 3, node7, node6);
        TreeVertex node2 = new NodeVertex("or", 2, node5, node4);
        TreeVertex node1 = new NodeVertex("impl", 1, node3, node2);

        testObject.setTree(node1);

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
        Assert.assertEquals("T", node13.getState(variables.get(0)));
        Assert.assertEquals("!", node13.getState(variables.get(1)));
        Assert.assertEquals("T", node12.getState(variables.get(0)));
        Assert.assertEquals("!", node12.getState(variables.get(1)));
        Assert.assertEquals("T", node11.getState(variables.get(0)));
        Assert.assertEquals("!", node11.getState(variables.get(1)));
        Assert.assertEquals("F", node10.getState(variables.get(0)));
        Assert.assertEquals("!", node10.getState(variables.get(1)));
        Assert.assertNull(node7.getState(variables.get(0)));
        Assert.assertNull(node7.getState(variables.get(1)));
        Assert.assertNull(node6.getState(variables.get(0)));
        Assert.assertNull(node6.getState(variables.get(1)));
        Assert.assertNull(node5.getState(variables.get(0)));
        Assert.assertNull(node5.getState(variables.get(1)));
        Assert.assertNull(node4.getState(variables.get(0)));
        Assert.assertNull(node4.getState(variables.get(1)));
        Assert.assertNull(node3.getState(variables.get(0)));
        Assert.assertNull(node3.getState(variables.get(1)));
        Assert.assertNull(node2.getState(variables.get(0)));
        Assert.assertNull(node2.getState(variables.get(1)));
        Assert.assertNull(node1.getState(variables.get(0)));
        Assert.assertNull(node1.getState(variables.get(1)));

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
        Assert.assertEquals("T", node7.getState(variables.get(0)));
        Assert.assertEquals("!", node7.getState(variables.get(1)));
        Assert.assertEquals("T", node6.getState(variables.get(0)));
        Assert.assertEquals("$", node6.getState(variables.get(1)));
        Assert.assertEquals("F", node5.getState(variables.get(0)));
        Assert.assertEquals("&", node5.getState(variables.get(1)));
        Assert.assertEquals("F", node4.getState(variables.get(0)));
        Assert.assertEquals("!", node4.getState(variables.get(1)));
        Assert.assertNull(node3.getState(variables.get(0)));
        Assert.assertNull(node3.getState(variables.get(1)));
        Assert.assertNull(node2.getState(variables.get(0)));
        Assert.assertNull(node2.getState(variables.get(1)));
        Assert.assertNull(node1.getState(variables.get(0)));
        Assert.assertNull(node1.getState(variables.get(1)));
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndNotAccepts()
    {
        TreeVertex node = new NodeVertex("impl", 1, new NodeVertex("and", 3,
                                                                   new NodeVertex("1", 7, null,
                                                                                  null),
                                                                   new NodeVertex("or", 6,
                                                                                  new NodeVertex(
                                                                                      "1", 13, null,
                                                                                      null),
                                                                                  new NodeVertex(
                                                                                      "1", 12, null,
                                                                                      null))),
                                         new NodeVertex("or", 2, new NodeVertex("and", 5,
                                                                                new NodeVertex("1",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                new NodeVertex("0",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                        new NodeVertex("0", 4, null, null)));

        testObject.setTree(node);

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
        TreeVertex node = new NodeVertex("impl", 1, new NodeVertex("and", 3,
                                                                   new NodeVertex("1", 7, null,
                                                                                  null),
                                                                   new NodeVertex("or", 6,
                                                                                  new NodeVertex(
                                                                                      "1", 13, null,
                                                                                      null),
                                                                                  new NodeVertex(
                                                                                      "1", 12, null,
                                                                                      null))),
                                         new NodeVertex("or", 2, new NodeVertex("and", 5,
                                                                                new NodeVertex("1",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                new NodeVertex("1",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                        new NodeVertex("0", 4, null, null)));

        testObject.setTree(node);

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
        TreeVertex node = new NodeVertex("impl", 1, new NodeVertex("and", 3,
                                                                   new NodeVertex("1", 7, null,
                                                                                  null),
                                                                   new NodeVertex("or", 6,
                                                                                  new NodeVertex(
                                                                                      "1", 13, null,
                                                                                      null),
                                                                                  new NodeVertex(
                                                                                      "1", 12, null,
                                                                                      null))),
                                         new NodeVertex("or", 2, new NodeVertex("and", 5,
                                                                                new NodeVertex("1",
                                                                                               11,
                                                                                               null,
                                                                                               null),
                                                                                new NodeVertex("0",
                                                                                               10,
                                                                                               null,
                                                                                               null)),
                                                        new NodeVertex("0", 4, null, null)));

        testObject.setTree(node);

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
