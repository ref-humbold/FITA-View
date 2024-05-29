package fitaview.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.tree.NodeHasParentException;
import fitaview.tree.RecNode;
import fitaview.tree.RepeatNode;
import fitaview.tree.StandardNode;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;

public class TopDownDITATest
{
    private TopDownDITA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private List<Map<Variable, Pair<String, Boolean>>> accepts =
            Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>());

    public TopDownDITATest()
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
        testObject = new TopDownDITA(variables, alphabet);
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
        testObject.addBuchiAcceptanceConditions(accepts.get(0));
        testObject.addBuchiAcceptanceConditions(accepts.get(1));
        testObject.addBuchiAcceptanceConditions(accepts.get(2));
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

        Assert.assertEquals("Top-down deterministic infinite tree automaton", result);
    }

    @Test(expected = TreeFinitenessException.class)
    public void setTree_WhenFiniteTree()
            throws TreeFinitenessException
    {
        TreeNode node = null;

        try
        {
            node = new StandardNode("and", 1, new StandardNode("1", 3),
                                    new StandardNode("or", 2, new StandardNode("0", 5),
                                                     new StandardNode("1", 4)));
        }
        catch(Exception e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject.setTree(node);
    }

    @Test
    public void setTree_WhenEmptyTree()
    {
        try
        {
            testObject.setTree(null);
        }
        catch(TreeFinitenessException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNull(testObject.tree);
    }

    @Test
    public void setTree_WhenInfiniteTree()
    {
        TreeNode node = null;

        try
        {
            RepeatNode node2 = new RepeatNode("0", 2);
            TreeNode node4 = new StandardNode("1", 4);
            TreeNode node5 =
                    new StandardNode("3", 5, new StandardNode("1", 11), new RecNode(node2, 10));
            node = new StandardNode("2", 1, new StandardNode("0", 3), node2);

            node2.setLeft(node5);
            node2.setRight(node4);

            testObject.setTree(node);
        }
        catch(TreeFinitenessException | NodeHasParentException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(testObject.tree);
        Assert.assertSame(node, testObject.tree);
    }
}
