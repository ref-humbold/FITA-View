package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.tree.NodeHasParentException;
import ref_humbold.fita_view.tree.StandardNode;
import ref_humbold.fita_view.tree.TreeNode;

public class TopDownDFSTest
{
    private TopDownDFS testObject;
    private TreeNode node13 = new StandardNode("13", 13);
    private TreeNode node12 = new StandardNode("12", 12);
    private TreeNode node11 = new StandardNode("11", 11);
    private TreeNode node10 = new StandardNode("10", 10);
    private TreeNode node7 = new StandardNode("7", 7);
    private TreeNode node6 = new StandardNode("6", 6, node13, node12);
    private TreeNode node5 = new StandardNode("5", 5, node11, node10);
    private TreeNode node4 = new StandardNode("4", 4);
    private TreeNode node3 = new StandardNode("3", 3, node7, node6);
    private TreeNode node2 = new StandardNode("2", 2, node5, node4);
    private TreeNode node1 = new StandardNode("1", 1, node3, node2);

    public TopDownDFSTest()
        throws NodeHasParentException
    {
    }

    @Before
    public void setUp()
    {
        testObject = new TopDownDFS();
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

        testObject.initialize(node1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertEquals(1, nodes.size());

            result.add(nodes.get(0));
        }

        TreeNode[] expected = new TreeNode[]{node1, node3, node7, node6, node13, node12, node2,
                                             node5, node11, node10, node4};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenOutOfBounds()
    {
        testObject.initialize(node1);

        while(true)
        {
            testObject.next();
        }
    }

    @Test
    public void testInitialize()
    {
        testObject.initialize(node1);

        Deque<TreeNode> deque = testObject.nodeDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertTrue(deque.contains(node1));
    }

    @Test
    public void testInitializeWhenDoubleInvoke()
    {
        testObject.initialize(node1);
        testObject.initialize(node2);

        Deque<TreeNode> deque = testObject.nodeDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertFalse(deque.contains(node1));
        Assert.assertTrue(deque.contains(node2));
    }

    @Test
    public void testHasNextWhenEmpty()
    {
        boolean result = testObject.hasNext();

        Assert.assertFalse(result);
    }

    @Test
    public void testHasNextWhenNotEmpty()
    {
        testObject.initialize(node11);

        boolean result = testObject.hasNext();

        Assert.assertTrue(result);
    }
}
