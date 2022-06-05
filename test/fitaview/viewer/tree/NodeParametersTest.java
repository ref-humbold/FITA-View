package fitaview.viewer.tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.tree.NodeHasParentException;
import fitaview.tree.StandardNode;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;

public class NodeParametersTest
{
    private NodeParameters testObject;
    private StandardNode node7 = new StandardNode("7", 7);
    private StandardNode node6 = new StandardNode("6", 6);
    private StandardNode node5 = new StandardNode("5", 5);
    private StandardNode node4 = new StandardNode("4", 4);
    private TreeNode node3 = new StandardNode("3", 3, node7, node6);
    private TreeNode node2 = new StandardNode("2", 2, node5, node4);
    private TreeNode node1 = new StandardNode("1", 1, node3, node2);

    public NodeParametersTest()
            throws NodeHasParentException
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testGetDistance()
    {
        testObject = new NodeParameters(node1, 3);

        Pair<Integer, Integer> result = testObject.getDistance();

        Assert.assertEquals(Pair.make(0, 0), result);
    }

    @Test
    public void testGetLeftParamsWhenRoot()
    {
        testObject = new NodeParameters(node1, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(-3, 1), result.getDistance());
        Assert.assertSame(node3, result.getNode());
        Assert.assertEquals(1, result.getInvertedDepth());
        Assert.assertEquals(2, result.getLeavesNumber());
    }

    @Test
    public void testGetRightParamsWhenRoot()
    {
        testObject = new NodeParameters(node1, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(3, 1), result.getDistance());
        Assert.assertSame(node2, result.getNode());
        Assert.assertEquals(1, result.getInvertedDepth());
        Assert.assertEquals(2, result.getLeavesNumber());
    }

    @Test
    public void testGetLeftParamsWhenInnerLeftNode()
    {
        testObject = new NodeParameters(-10, 10, 1, node3, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(-12, 11), result.getDistance());
        Assert.assertSame(node7, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void testGetRightParamsWhenInnerLeftNode()
    {
        testObject = new NodeParameters(-10, 10, 1, node3, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(-9, 12), result.getDistance());
        Assert.assertSame(node6, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void testGetLeftParamsWhenInnerRightNode()
    {
        testObject = new NodeParameters(10, 10, 1, node2, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(9, 12), result.getDistance());
        Assert.assertSame(node5, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void testGetRightParamsWhenInnerRightNode()
    {
        testObject = new NodeParameters(10, 10, 1, node2, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(12, 11), result.getDistance());
        Assert.assertSame(node4, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void testGetLeftParamsWhenLeafNode()
    {
        testObject = new NodeParameters(10, 10, 2, node4, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(9, 11), result.getDistance());
        Assert.assertTrue(result.getNode().isNull());
        Assert.assertEquals(-1, result.getInvertedDepth());
        Assert.assertEquals(0, result.getLeavesNumber());
    }

    @Test
    public void testGetRightParamsWhenLeafNode()
    {
        testObject = new NodeParameters(10, 10, 2, node4, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(11, 11), result.getDistance());
        Assert.assertTrue(result.getNode().isNull());
        Assert.assertEquals(-1, result.getInvertedDepth());
        Assert.assertEquals(0, result.getLeavesNumber());
    }
}
