package fitaview.viewer.tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import fitaview.tree.NodeHasParentException;
import fitaview.tree.StandardNode;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;

public class NodeParametersTest
{
    private NodeParameters testObject;
    private final StandardNode node7 = new StandardNode("7", 7);
    private final StandardNode node6 = new StandardNode("6", 6);
    private final StandardNode node5 = new StandardNode("5", 5);
    private final StandardNode node4 = new StandardNode("4", 4);
    private final TreeNode node3 = new StandardNode("3", 3, node7, node6);
    private final TreeNode node2 = new StandardNode("2", 2, node5, node4);
    private final TreeNode node1 = new StandardNode("1", 1, node3, node2);

    public NodeParametersTest()
            throws NodeHasParentException
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
    public void getLeftParams_WhenRoot()
    {
        testObject = new NodeParameters(node1, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(-3, 1), result.getDistance());
        Assert.assertSame(node3, result.getNode());
        Assert.assertEquals(1, result.getInvertedDepth());
        Assert.assertEquals(2, result.getLeavesNumber());
    }

    @Test
    public void getRightParams_WhenRoot()
    {
        testObject = new NodeParameters(node1, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(3, 1), result.getDistance());
        Assert.assertSame(node2, result.getNode());
        Assert.assertEquals(1, result.getInvertedDepth());
        Assert.assertEquals(2, result.getLeavesNumber());
    }

    @Test
    public void getLeftParams_WhenInnerLeftNode()
    {
        testObject = new NodeParameters(-10, 10, 1, node3, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(-12, 11), result.getDistance());
        Assert.assertSame(node7, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void getRightParams_WhenInnerLeftNode()
    {
        testObject = new NodeParameters(-10, 10, 1, node3, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(-9, 12), result.getDistance());
        Assert.assertSame(node6, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void getLeftParams_WhenInnerRightNode()
    {
        testObject = new NodeParameters(10, 10, 1, node2, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(9, 12), result.getDistance());
        Assert.assertSame(node5, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void getRightParams_WhenInnerRightNode()
    {
        testObject = new NodeParameters(10, 10, 1, node2, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(12, 11), result.getDistance());
        Assert.assertSame(node4, result.getNode());
        Assert.assertEquals(0, result.getInvertedDepth());
        Assert.assertEquals(1, result.getLeavesNumber());
    }

    @Test
    public void getLeftParams_WhenLeafNode()
    {
        testObject = new NodeParameters(10, 10, 2, node4, 3);

        NodeParameters result = testObject.getLeftParams();

        Assert.assertEquals(Pair.make(9, 11), result.getDistance());
        Assert.assertTrue(result.getNode().isNull());
        Assert.assertEquals(-1, result.getInvertedDepth());
        Assert.assertEquals(0, result.getLeavesNumber());
    }

    @Test
    public void getRightParams_WhenLeafNode()
    {
        testObject = new NodeParameters(10, 10, 2, node4, 3);

        NodeParameters result = testObject.getRightParams();

        Assert.assertEquals(Pair.make(11, 11), result.getDistance());
        Assert.assertTrue(result.getNode().isNull());
        Assert.assertEquals(-1, result.getInvertedDepth());
        Assert.assertEquals(0, result.getLeavesNumber());
    }
}
