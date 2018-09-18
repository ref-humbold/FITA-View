package refhumbold.fitaview.automaton.traversing;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import refhumbold.fitaview.tree.NodeHasParentException;
import refhumbold.fitaview.tree.RecNode;
import refhumbold.fitaview.tree.RepeatNode;
import refhumbold.fitaview.tree.StandardNode;
import refhumbold.fitaview.tree.TreeNode;

public class TopDownDFSTest
{
    private TopDownDFS testObject;
    private TreeNode finiteNode13 = new StandardNode("13", 13);
    private TreeNode finiteNode12 = new StandardNode("12", 12);
    private TreeNode finiteNode11 = new StandardNode("11", 11);
    private TreeNode finiteNode10 = new StandardNode("10", 10);
    private TreeNode finiteNode7 = new StandardNode("7", 7);
    private TreeNode finiteNode6 = new StandardNode("6", 6, finiteNode13, finiteNode12);
    private TreeNode finiteNode5 = new StandardNode("5", 5, finiteNode11, finiteNode10);
    private TreeNode finiteNode4 = new StandardNode("4", 4);
    private TreeNode finiteNode3 = new StandardNode("3", 3, finiteNode7, finiteNode6);
    private TreeNode finiteNode2 = new StandardNode("2", 2, finiteNode5, finiteNode4);
    private TreeNode finiteNode1 = new StandardNode("1", 1, finiteNode3, finiteNode2);

    private TreeNode infiniteNode13 = new StandardNode("13", 13);
    private TreeNode infiniteNode12 = new StandardNode("12", 12);
    private TreeNode infiniteNode11 = new StandardNode("11", 11);
    private TreeNode infiniteNode7 = new StandardNode("7", 7);
    private TreeNode infiniteNode6 = new StandardNode("6", 6, infiniteNode13, infiniteNode12);
    private TreeNode infiniteNode3 = new StandardNode("3", 3, infiniteNode7, infiniteNode6);
    private RepeatNode infiniteNode2 = new RepeatNode("2", 2);
    private RecNode infiniteNode4 = new RecNode(infiniteNode2, 4);
    private RecNode infiniteNode10 = new RecNode(infiniteNode2, 10);
    private TreeNode infiniteNode5 = new StandardNode("5", 5, infiniteNode11, infiniteNode10);
    private TreeNode infiniteNode1 = new StandardNode("1", 1, infiniteNode3, infiniteNode2);

    public TopDownDFSTest()
        throws NodeHasParentException
    {
        infiniteNode2.setRight(infiniteNode4);
        infiniteNode2.setLeft(infiniteNode5);
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

        testObject.initialize(finiteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertEquals(1, nodes.size());

            result.add(nodes.get(0));
        }

        TreeNode[] expected = new TreeNode[]{finiteNode1, finiteNode3, finiteNode7, finiteNode6,
                                             finiteNode13, finiteNode12, finiteNode2, finiteNode5,
                                             finiteNode11, finiteNode10, finiteNode4};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testNextWhenInfiniteTree()
    {
        ArrayList<TreeNode> result = new ArrayList<>();

        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
            {
                nodes.add(v);
                testObject.addNewRecursive(v.getLeft());
                testObject.addNewRecursive(v.getRight());
            }

            Assert.assertEquals(1, nodes.size());

            result.add(nodes.get(0));
        }

        TreeNode[] expected = new TreeNode[]{infiniteNode1, infiniteNode3, infiniteNode7,
                                             infiniteNode6, infiniteNode13, infiniteNode12,
                                             infiniteNode2, infiniteNode5, infiniteNode11};

        Assert.assertArrayEquals(expected, result.toArray());
        Assert.assertFalse(testObject.hasNext());
        Assert.assertTrue(testObject.canContinue());
    }

    @Test
    public void testNextWhenRecursiveContinuation()
    {
        ArrayList<TreeNode> resultFirst = new ArrayList<>();
        ArrayList<TreeNode> resultSecond = new ArrayList<>();

        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
            {
                nodes.add(v);
                testObject.addNewRecursive(v.getLeft());
                testObject.addNewRecursive(v.getRight());
            }

            Assert.assertEquals(1, nodes.size());

            resultFirst.add(nodes.get(0));
        }

        try
        {
            testObject.continueRecursive();
        }
        catch(RecursiveContinuationException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertEquals(1, nodes.size());

            resultSecond.add(nodes.get(0));
        }

        TreeNode[] expectedFirst = new TreeNode[]{infiniteNode1, infiniteNode3, infiniteNode7,
                                                  infiniteNode6, infiniteNode13, infiniteNode12,
                                                  infiniteNode2, infiniteNode5, infiniteNode11};

        TreeNode[] expectedSecond = new TreeNode[]{infiniteNode4, infiniteNode5, infiniteNode11,
                                                   infiniteNode10, infiniteNode5, infiniteNode11};

        Assert.assertArrayEquals(expectedFirst, resultFirst.toArray());
        Assert.assertArrayEquals(expectedSecond, resultSecond.toArray());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextWhenOutOfBounds()
    {
        testObject.initialize(finiteNode1);

        while(testObject.hasNext())
        {
            testObject.next();
        }

        testObject.next();
    }

    @Test
    public void testInitialize()
    {
        testObject.initialize(finiteNode1);

        Deque<TreeNode> deque = testObject.nodeDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertTrue(deque.contains(finiteNode1));
    }

    @Test
    public void testInitializeWhenDoubleInvoke()
    {
        testObject.initialize(finiteNode1);
        testObject.initialize(finiteNode2);

        Deque<TreeNode> deque = testObject.nodeDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertFalse(deque.contains(finiteNode1));
        Assert.assertTrue(deque.contains(finiteNode2));
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
        testObject.initialize(finiteNode11);

        boolean result = testObject.hasNext();

        Assert.assertTrue(result);
    }

    @Test
    public void testCanContinueWhenTraversingEnded()
    {
        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            for(TreeNode v : testObject.next())
            {
                testObject.addNewRecursive(v.getLeft());
                testObject.addNewRecursive(v.getRight());
            }
        }

        Assert.assertFalse(testObject.hasNext());
        Assert.assertTrue(testObject.canContinue());
    }

    @Test
    public void testCanContinueWhenTraversingInProgress()
    {
        testObject.initialize(infiniteNode1);
        testObject.next();

        Assert.assertTrue(testObject.hasNext());
        Assert.assertFalse(testObject.canContinue());
    }

    @Test
    public void testContinueRecursiveWhenTraversingEnded()
    {
        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            for(TreeNode v : testObject.next())
            {
                testObject.addNewRecursive(v.getLeft());
                testObject.addNewRecursive(v.getRight());
            }
        }

        try
        {
            testObject.continueRecursive();
        }
        catch(RecursiveContinuationException e)
        {
            e.printStackTrace();
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertTrue(testObject.hasNext());
        Assert.assertFalse(testObject.canContinue());
    }

    @Test(expected = RecursiveContinuationException.class)
    public void testContinueRecursiveWhenTraversingInProgress()
        throws RecursiveContinuationException
    {
        testObject.initialize(infiniteNode1);
        testObject.next();

        testObject.continueRecursive();
    }
}
