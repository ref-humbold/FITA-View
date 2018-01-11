package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.Deque;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.tree.*;

public class TopDownLevelTest
{
    private TopDownLevel testObject;
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

    public TopDownLevelTest()
        throws NodeHasParentException
    {
        infiniteNode2.setRight(infiniteNode4);
        infiniteNode2.setLeft(infiniteNode5);
    }

    @Before
    public void setUp()
    {
        testObject = new TopDownLevel();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testNextWhenFiniteTree()
    {
        ArrayList<Object[]> result = new ArrayList<>();

        testObject.initialize(finiteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertTrue(nodes.size() > 0);

            result.add(nodes.toArray());
        }

        TreeNode[][] expected = {{finiteNode1}, {finiteNode3, finiteNode2},
                                 {finiteNode7, finiteNode6, finiteNode5, finiteNode4},
                                 {finiteNode13, finiteNode12, finiteNode11, finiteNode10}};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testNextWhenInfiniteTree()
    {
        ArrayList<Object[]> result = new ArrayList<>();

        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertTrue(nodes.size() > 0);

            result.add(nodes.toArray());
        }

        TreeNode[][] expected = {{infiniteNode1}, {infiniteNode3, infiniteNode2},
                                 {infiniteNode7, infiniteNode6, infiniteNode5},
                                 {infiniteNode13, infiniteNode12, infiniteNode11}};

        Assert.assertArrayEquals(expected, result.toArray());
        Assert.assertFalse(testObject.hasNext());
        Assert.assertTrue(testObject.canContinue());
    }

    @Test
    public void testNextWhenRecursiveContinuation()
    {

        ArrayList<Object[]> resultFirst = new ArrayList<>();
        ArrayList<Object[]> resultSecond = new ArrayList<>();

        testObject.initialize(infiniteNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertTrue(nodes.size() > 0);

            resultFirst.add(nodes.toArray());
        }

        try
        {
            testObject.continueRecursive();
        }
        catch(RecursiveContinuationException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        while(testObject.hasNext())
        {
            ArrayList<TreeNode> nodes = new ArrayList<>();

            for(TreeNode v : testObject.next())
                nodes.add(v);

            Assert.assertTrue(nodes.size() > 0);

            resultSecond.add(nodes.toArray());
        }

        TreeNode[][] expectedFirst = {{infiniteNode1}, {infiniteNode3, infiniteNode2},
                                      {infiniteNode7, infiniteNode6, infiniteNode5},
                                      {infiniteNode13, infiniteNode12, infiniteNode11}};

        TreeNode[][] expectedSecond = {{infiniteNode4}, {infiniteNode5}, {infiniteNode11},
                                       {infiniteNode10}, {infiniteNode5}, {infiniteNode11}};

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
            testObject.next();
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
            testObject.next();
        }

        try
        {
            testObject.continueRecursive();
        }
        catch(RecursiveContinuationException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
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
