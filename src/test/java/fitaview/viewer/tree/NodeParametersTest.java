package fitaview.viewer.tree;

import org.assertj.core.api.Assertions;
import org.junit.After;
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
        // given
        testObject = new NodeParameters(node1, 3);
        // when
        Pair<Integer, Integer> result = testObject.getDistance();
        // then
        Assertions.assertThat(result).isEqualTo(Pair.make(0, 0));
    }

    @Test
    public void getLeftParams_WhenRoot()
    {
        // given
        testObject = new NodeParameters(node1, 3);
        // when
        NodeParameters result = testObject.getLeftParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(-3, 1));
        Assertions.assertThat(result.getNode()).isSameAs(node3);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(1);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(2);
    }

    @Test
    public void getRightParams_WhenRoot()
    {
        // given
        testObject = new NodeParameters(node1, 3);
        // when
        NodeParameters result = testObject.getRightParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(3, 1));
        Assertions.assertThat(result.getNode()).isSameAs(node2);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(1);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(2);
    }

    @Test
    public void getLeftParams_WhenInnerLeftNode()
    {
        // given
        testObject = new NodeParameters(-10, 10, 1, node3, 3);
        // when
        NodeParameters result = testObject.getLeftParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(-12, 11));
        Assertions.assertThat(result.getNode()).isSameAs(node7);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(0);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(1);
    }

    @Test
    public void getRightParams_WhenInnerLeftNode()
    {
        // given
        testObject = new NodeParameters(-10, 10, 1, node3, 3);
        // when
        NodeParameters result = testObject.getRightParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(-9, 12));
        Assertions.assertThat(result.getNode()).isSameAs(node6);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(0);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(1);
    }

    @Test
    public void getLeftParams_WhenInnerRightNode()
    {
        // given
        testObject = new NodeParameters(10, 10, 1, node2, 3);
        // when
        NodeParameters result = testObject.getLeftParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(9, 12));
        Assertions.assertThat(result.getNode()).isSameAs(node5);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(0);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(1);
    }

    @Test
    public void getRightParams_WhenInnerRightNode()
    {
        // given
        testObject = new NodeParameters(10, 10, 1, node2, 3);
        // when
        NodeParameters result = testObject.getRightParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(12, 11));
        Assertions.assertThat(result.getNode()).isSameAs(node4);
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(0);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(1);
    }

    @Test
    public void getLeftParams_WhenLeafNode()
    {
        // given
        testObject = new NodeParameters(10, 10, 2, node4, 3);
        // when
        NodeParameters result = testObject.getLeftParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(9, 11));
        Assertions.assertThat(result.getNode().isNull()).isTrue();
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(-1);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(0);
    }

    @Test
    public void getRightParams_WhenLeafNode()
    {
        // given
        testObject = new NodeParameters(10, 10, 2, node4, 3);
        // when
        NodeParameters result = testObject.getRightParams();
        // then
        Assertions.assertThat(result.getDistance()).isEqualTo(Pair.make(11, 11));
        Assertions.assertThat(result.getNode().isNull()).isTrue();
        Assertions.assertThat(result.getInvertedDepth()).isEqualTo(-1);
        Assertions.assertThat(result.getLeavesNumber()).isEqualTo(0);
    }
}
