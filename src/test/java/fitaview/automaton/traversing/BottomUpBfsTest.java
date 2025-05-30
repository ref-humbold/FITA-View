package fitaview.automaton.traversing;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Queue;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.tree.NodeHasParentException;
import fitaview.tree.StandardNode;
import fitaview.tree.TreeNode;

public class BottomUpBfsTest
{
    private BottomUpBfs testObject;
    private final TreeNode node13 = new StandardNode("13", 13);
    private final TreeNode node12 = new StandardNode("12", 12);
    private final TreeNode node11 = new StandardNode("11", 11);
    private final TreeNode node10 = new StandardNode("10", 10);
    private final TreeNode node7 = new StandardNode("7", 7);
    private final TreeNode node4 = new StandardNode("4", 4);
    private final TreeNode node6 = new StandardNode("6", 6, node13, node12);
    private final TreeNode node3 = new StandardNode("3", 3, node7, node6);
    private final TreeNode node5 = new StandardNode("5", 5, node11, node10);
    private final TreeNode node2 = new StandardNode("2", 2, node5, node4);
    private final TreeNode node1 = new StandardNode("1", 1, node3, node2);

    public BottomUpBfsTest()
            throws NodeHasParentException
    {
    }

    @Before
    public void setUp()
    {
        testObject = new BottomUpBfs();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testNext()
    {
        ArrayList<TreeNode> result = new ArrayList<>();

        testObject.initialize(node4, node7, node10, node11, node12, node13);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertEquals(1, nodes.size());

            result.add(nodes.get(0));
        }

        TreeNode[] expected =
                new TreeNode[]{node13, node12, node11, node10, node7, node6, node5, node4, node3,
                               node2, node1};

        Assert.assertArrayEquals(expected, result.toArray());
        Assert.assertFalse(testObject.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void next_WhenOutOfBounds()
    {
        testObject.initialize(node4, node7, node10, node11, node12, node13);

        while(testObject.hasNext())
        {
            testObject.next();
        }

        testObject.next();
    }

    @Test
    public void testInitialize()
    {
        testObject.initialize(node7, node4);

        Queue<TreeNode> queue = testObject.nodeQueue;

        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(queue.contains(node7));
        Assert.assertTrue(queue.contains(node4));
    }

    @Test
    public void initialize_WhenDoubleInvoke()
    {
        testObject.initialize(node7, node4);
        testObject.initialize(node10, node11, node12);

        Queue<TreeNode> queue = testObject.nodeQueue;

        Assert.assertEquals(3, queue.size());
        Assert.assertFalse(queue.contains(node7));
        Assert.assertFalse(queue.contains(node4));
        Assert.assertTrue(queue.contains(node12));
        Assert.assertTrue(queue.contains(node11));
        Assert.assertTrue(queue.contains(node10));
    }

    @Test
    public void hasNext_WhenEmpty()
    {
        boolean result = testObject.hasNext();

        Assert.assertFalse(result);
    }

    @Test
    public void hasNext_WhenNotEmpty()
    {
        testObject.initialize(node13);

        boolean result = testObject.hasNext();

        Assert.assertTrue(result);
    }
}
