package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public class TopDownDFTATest
{
    private TopDownDFTA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");

    public TopDownDFTATest()
        throws Exception
    {
        variables = Arrays.asList(new Variable("A", "B"), new Variable("!", "@", "#", "$"));
    }

    @Before
    public void setUp()
        throws Exception
    {
        List<Map<Variable, String>> accepts =
            Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>());

        accepts.get(0).put(variables.get(0), "A");
        accepts.get(0).put(variables.get(1), "@");
        accepts.get(1).put(variables.get(0), "B");
        accepts.get(1).put(variables.get(1), "#");
        accepts.get(2).put(variables.get(0), Wildcard.EVERY_VALUE);
        accepts.get(2).put(variables.get(1), "$");

        testObject = new TopDownDFTA(alphabet, variables);
        testObject.setTraversing(TraversingFactory.Mode.LEVEL);
        testObject.addAcceptingState(accepts.get(0));
        testObject.addAcceptingState(accepts.get(1));
        testObject.addAcceptingState(accepts.get(2));
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
    public void testRun()
    {
        TreeVertex node13 = new NodeVertex("2", 13, null, null);
        TreeVertex node12 = new NodeVertex("1", 12, null, null);
        TreeVertex node11 = new NodeVertex("0", 11, null, null);
        TreeVertex node10 = new NodeVertex("4", 10, null, null);
        TreeVertex node7 = new NodeVertex("3", 7, null, null);
        TreeVertex node6 = new NodeVertex("3", 6, node13, node12);
        TreeVertex node5 = new NodeVertex("2", 5, node11, node10);
        TreeVertex node4 = new NodeVertex("0", 4, null, null);
        TreeVertex node3 = new NodeVertex("4", 3, node7, node6);
        TreeVertex node2 = new NodeVertex("1", 2, node5, node4);
        TreeVertex node1 = new NodeVertex("2", 1, node3, node2);

        testObject.setTree(node1);

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

        Assert.assertFalse(testObject.isRunning);
        Assert.assertEquals("A", node1.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node4.getStateOrNull(variables.get(0)));
        Assert.assertEquals("$", node4.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node5.getStateOrNull(variables.get(0)));
        Assert.assertEquals("#", node5.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node6.getStateOrNull(variables.get(0)));
        Assert.assertEquals("@", node6.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node7.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node10.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node10.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node11.getStateOrNull(variables.get(0)));
        Assert.assertEquals("$", node11.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node12.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node12.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node13.getStateOrNull(variables.get(0)));
        Assert.assertEquals("$", node13.getStateOrNull(variables.get(1)));
    }

    @Test
    public void testMakeStepForward()
    {
        TreeVertex node13 = new NodeVertex("2", 13, null, null);
        TreeVertex node12 = new NodeVertex("1", 12, null, null);
        TreeVertex node11 = new NodeVertex("0", 11, null, null);
        TreeVertex node10 = new NodeVertex("4", 10, null, null);
        TreeVertex node7 = new NodeVertex("3", 7, null, null);
        TreeVertex node6 = new NodeVertex("3", 6, node13, node12);
        TreeVertex node5 = new NodeVertex("2", 5, node11, node10);
        TreeVertex node4 = new NodeVertex("0", 4, null, null);
        TreeVertex node3 = new NodeVertex("4", 3, node7, node6);
        TreeVertex node2 = new NodeVertex("1", 2, node5, node4);
        TreeVertex node1 = new NodeVertex("2", 1, node3, node2);

        testObject.setTree(node1);

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
        Assert.assertEquals("A", node1.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateOrNull(variables.get(1)));
        Assert.assertNull(node4.getStateOrNull(variables.get(0)));
        Assert.assertNull(node4.getStateOrNull(variables.get(1)));
        Assert.assertNull(node5.getStateOrNull(variables.get(0)));
        Assert.assertNull(node5.getStateOrNull(variables.get(1)));
        Assert.assertNull(node6.getStateOrNull(variables.get(0)));
        Assert.assertNull(node6.getStateOrNull(variables.get(1)));
        Assert.assertNull(node7.getStateOrNull(variables.get(0)));
        Assert.assertNull(node7.getStateOrNull(variables.get(1)));
        Assert.assertNull(node10.getStateOrNull(variables.get(0)));
        Assert.assertNull(node10.getStateOrNull(variables.get(1)));
        Assert.assertNull(node11.getStateOrNull(variables.get(0)));
        Assert.assertNull(node11.getStateOrNull(variables.get(1)));
        Assert.assertNull(node12.getStateOrNull(variables.get(0)));
        Assert.assertNull(node12.getStateOrNull(variables.get(1)));
        Assert.assertNull(node13.getStateOrNull(variables.get(0)));
        Assert.assertNull(node13.getStateOrNull(variables.get(1)));

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
        Assert.assertEquals("A", node1.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node1.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node2.getStateOrNull(variables.get(0)));
        Assert.assertEquals("#", node2.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node3.getStateOrNull(variables.get(0)));
        Assert.assertEquals("@", node3.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node4.getStateOrNull(variables.get(0)));
        Assert.assertEquals("$", node4.getStateOrNull(variables.get(1)));
        Assert.assertEquals("B", node5.getStateOrNull(variables.get(0)));
        Assert.assertEquals("#", node5.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node6.getStateOrNull(variables.get(0)));
        Assert.assertEquals("@", node6.getStateOrNull(variables.get(1)));
        Assert.assertEquals("A", node7.getStateOrNull(variables.get(0)));
        Assert.assertEquals("!", node7.getStateOrNull(variables.get(1)));
        Assert.assertNull(node10.getStateOrNull(variables.get(0)));
        Assert.assertNull(node10.getStateOrNull(variables.get(1)));
        Assert.assertNull(node11.getStateOrNull(variables.get(0)));
        Assert.assertNull(node11.getStateOrNull(variables.get(1)));
        Assert.assertNull(node12.getStateOrNull(variables.get(0)));
        Assert.assertNull(node12.getStateOrNull(variables.get(1)));
        Assert.assertNull(node13.getStateOrNull(variables.get(0)));
        Assert.assertNull(node13.getStateOrNull(variables.get(1)));
    }

    @Ignore
    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndAccepts()
    {
        TreeVertex node13 = new NodeVertex("2", 13, null, null);
        TreeVertex node12 = new NodeVertex("1", 12, null, null);
        TreeVertex node11 = new NodeVertex("0", 11, null, null);
        TreeVertex node10 = new NodeVertex("4", 10, null, null);
        TreeVertex node7 = new NodeVertex("3", 7, null, null);
        TreeVertex node6 = new NodeVertex("3", 6, node13, node12);
        TreeVertex node5 = new NodeVertex("2", 5, node11, node10);
        TreeVertex node4 = new NodeVertex("0", 4, null, null);
        TreeVertex node3 = new NodeVertex("4", 3, node7, node6);
        TreeVertex node2 = new NodeVertex("1", 2, node5, node4);
        TreeVertex node = new NodeVertex("2", 1, node3, node2);

        testObject.setTree(node);

        try
        {
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

        Assert.assertFalse(result);
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasRunAndNotAccepts()
    {
        TreeVertex node = new NodeVertex("2", 1,
                                         new NodeVertex("4", 3, new NodeVertex("3", 7, null, null),
                                                        new NodeVertex("3", 6,
                                                                       new NodeVertex("2", 13, null,
                                                                                      null),
                                                                       new NodeVertex("1", 12, null,
                                                                                      null))),
                                         new NodeVertex("1", 2, new NodeVertex("2", 5,
                                                                               new NodeVertex("0",
                                                                                              11,
                                                                                              null,
                                                                                              null),
                                                                               new NodeVertex("4",
                                                                                              10,
                                                                                              null,
                                                                                              null)),
                                                        new NodeVertex("0", 4, null, null)));

        testObject.setTree(node);

        try
        {
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

        Assert.assertFalse(result);
    }

    @Test(expected = UndefinedTreeStateException.class)
    public void testIsAcceptedWhenAutomatonHasNotRun()
        throws UndefinedTreeStateException
    {
        TreeVertex node = new NodeVertex("2", 1,
                                         new NodeVertex("4", 3, new NodeVertex("3", 7, null, null),
                                                        new NodeVertex("3", 6,
                                                                       new NodeVertex("2", 13, null,
                                                                                      null),
                                                                       new NodeVertex("1", 12, null,
                                                                                      null))),
                                         new NodeVertex("1", 2, new NodeVertex("2", 5,
                                                                               new NodeVertex("0",
                                                                                              11,
                                                                                              null,
                                                                                              null),
                                                                               new NodeVertex("4",
                                                                                              10,
                                                                                              null,
                                                                                              null)),
                                                        new NodeVertex("0", 4, null, null)));

        testObject.setTree(node);

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
