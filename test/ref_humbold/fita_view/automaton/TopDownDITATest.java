package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.tree.*;

public class TopDownDITATest
{
    private TopDownDITA testObject;
    private List<Variable> variables;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private List<Map<Variable, String>> accepts = Arrays.asList(new HashMap<>(), new HashMap<>(),
                                                                new HashMap<>());

    public TopDownDITATest()
        throws Exception
    {
        variables = Arrays.asList(new Variable(1, "A", "B"), new Variable(2, "!", "@", "#", "$"));
        accepts.get(0).put(variables.get(0), "+ A");
        accepts.get(0).put(variables.get(1), "+ @");
        accepts.get(1).put(variables.get(0), "+ B");
        accepts.get(1).put(variables.get(1), "+ $");
        accepts.get(2).put(variables.get(0), "+ " + Wildcard.EVERY_VALUE);
        accepts.get(2).put(variables.get(1), "+ #");
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
    public void testSetTreeWhenFiniteTree()
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
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        testObject.setTree(node);
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
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNull(testObject.tree);
    }

    public void testSetTreeWhenInfiniteTree()
    {
        TreeNode node = null;

        try
        {
            RepeatNode node2 = new RepeatNode("0", 2);
            TreeNode node4 = new StandardNode("1", 4);
            TreeNode node5 = new StandardNode("3", 5, new StandardNode("1", 11),
                                              new RecNode(node2, 10));
            node = new StandardNode("2", 1, new StandardNode("0", 3), node2);

            node2.setLeft(node5);
            node2.setRight(node4);

            testObject.setTree(node);
        }
        catch(TreeFinitenessException | NodeHasParentException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(testObject.tree);
        Assert.assertSame(node, testObject.tree);
    }
}
