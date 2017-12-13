package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpBFSTest
{
    private BottomUpBFS testObject;
    private TreeVertex finiteTreeNode13 = new NodeVertex("13", null, null);
    private TreeVertex finiteTreeNode12 = new NodeVertex("12", null, null);
    private TreeVertex finiteTreeNode11 = new NodeVertex("11", null, null);
    private TreeVertex finiteTreeNode10 = new NodeVertex("10", null, null);
    private TreeVertex finiteTreeNode7 = new NodeVertex("7", null, null);
    private TreeVertex finiteTreeNode6 = new NodeVertex("6", finiteTreeNode12, finiteTreeNode13);
    private TreeVertex finiteTreeNode5 = new NodeVertex("5", finiteTreeNode10, finiteTreeNode11);
    private TreeVertex finiteTreeNode4 = new NodeVertex("4", null, null);
    private TreeVertex finiteTreeNode3 = new NodeVertex("3", finiteTreeNode6, finiteTreeNode7);
    private TreeVertex finiteTreeNode2 = new NodeVertex("2", finiteTreeNode4, finiteTreeNode5);
    private TreeVertex finiteTreeNode1 = new NodeVertex("1", finiteTreeNode2, finiteTreeNode3);
    private List<Pair<TreeVertex, Integer>> verticesDepths;

    @Before
    public void setUp()
    {
        testObject = new BottomUpBFS();
        verticesDepths = Arrays.asList(Pair.make(finiteTreeNode1, 1), Pair.make(finiteTreeNode2, 3),
                                       Pair.make(finiteTreeNode3, 2), Pair.make(finiteTreeNode4, 7),
                                       Pair.make(finiteTreeNode5, 6), Pair.make(finiteTreeNode6, 5),
                                       Pair.make(finiteTreeNode7, 4),
                                       Pair.make(finiteTreeNode10, 11),
                                       Pair.make(finiteTreeNode11, 10),
                                       Pair.make(finiteTreeNode12, 9),
                                       Pair.make(finiteTreeNode13, 8));
    }

    @After
    public void tearDown()
    {
        testObject = null;
        verticesDepths = null;
    }

    @Test
    public void testNext()
    {
        ArrayList<TreeVertex> result = new ArrayList<>();

        testObject.initialize(
            Arrays.asList(verticesDepths.get(3), verticesDepths.get(6), verticesDepths.get(7),
                          verticesDepths.get(8), verticesDepths.get(9), verticesDepths.get(10)));

        while(testObject.hasNext())
        {
            ArrayList<TreeVertex> vertices = new ArrayList<>();

            for(TreeVertex v : testObject.next())
                vertices.add(v);

            Assert.assertEquals(1, vertices.size());

            result.add(vertices.get(0));
        }

        TreeVertex[] expected =
            new TreeVertex[]{finiteTreeNode10, finiteTreeNode11, finiteTreeNode12, finiteTreeNode13,
                             finiteTreeNode4, finiteTreeNode5, finiteTreeNode6, finiteTreeNode7,
                             finiteTreeNode2, finiteTreeNode3, finiteTreeNode1};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testInitializeSingle()
    {
        testObject.initialize(verticesDepths.get(7));

        Queue<Pair<TreeVertex, Integer>> queue = testObject.vertexQueue;

        Assert.assertEquals(1, queue.size());
        Assert.assertTrue(queue.contains(verticesDepths.get(7)));
        Assert.assertEquals(3, testObject.maxDepth);
    }

    @Test
    public void testInitializeSingleWhenDoubleInvoke()
    {
        testObject.initialize(verticesDepths.get(7));
        testObject.initialize(verticesDepths.get(6));

        Queue<Pair<TreeVertex, Integer>> queue = testObject.vertexQueue;

        Assert.assertEquals(1, queue.size());
        Assert.assertFalse(queue.contains(verticesDepths.get(7)));
        Assert.assertTrue(queue.contains(verticesDepths.get(6)));
        Assert.assertEquals(2, testObject.maxDepth);
    }

    @Test
    public void testInitializeCollection()
    {
        testObject.initialize(Arrays.asList(verticesDepths.get(7), verticesDepths.get(8)));

        Queue<Pair<TreeVertex, Integer>> queue = testObject.vertexQueue;

        Assert.assertEquals(2, queue.size());
        Assert.assertTrue(queue.contains(verticesDepths.get(7)));
        Assert.assertTrue(queue.contains(verticesDepths.get(8)));
        Assert.assertEquals(3, testObject.maxDepth);
    }

    @Test
    public void testInitializeCollectionWhenDoubleInvoke()
    {
        testObject.initialize(Arrays.asList(verticesDepths.get(7), verticesDepths.get(8)));
        testObject.initialize(Arrays.asList(verticesDepths.get(6), verticesDepths.get(3)));

        Queue<Pair<TreeVertex, Integer>> queue = testObject.vertexQueue;

        Assert.assertEquals(2, queue.size());
        Assert.assertFalse(queue.contains(verticesDepths.get(7)));
        Assert.assertFalse(queue.contains(verticesDepths.get(8)));
        Assert.assertTrue(queue.contains(verticesDepths.get(6)));
        Assert.assertTrue(queue.contains(verticesDepths.get(3)));
        Assert.assertEquals(2, testObject.maxDepth);
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
        testObject.initialize(Collections.singletonList(verticesDepths.get(9)));

        boolean result = testObject.hasNext();

        Assert.assertTrue(result);
    }
}
