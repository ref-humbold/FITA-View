package ref_humbold.fita_view.automaton.traversing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;

public class TopDownDFSTest
{
    private TopDownDFS testObject;
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
        ArrayList<TreeVertex> result = new ArrayList<>();

        testObject.initialize(finiteTreeNode1);

        while(testObject.hasNext())
        {
            ArrayList<TreeVertex> vertices = new ArrayList<>();

            for(TreeVertex v : testObject.next())
                vertices.add(v);

            Assert.assertEquals(1, vertices.size());

            result.add(vertices.get(0));
        }

        TreeVertex[] expected =
            new TreeVertex[]{finiteTreeNode1, finiteTreeNode2, finiteTreeNode4, finiteTreeNode5,
                             finiteTreeNode10, finiteTreeNode11, finiteTreeNode3, finiteTreeNode6,
                             finiteTreeNode12, finiteTreeNode13, finiteTreeNode7};

        Assert.assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void testInitialize()
    {
        testObject.initialize(finiteTreeNode1);

        Deque<TreeVertex> deque = testObject.vertexDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertTrue(deque.contains(finiteTreeNode1));
    }

    @Test
    public void testInitializeWhenDoubleInvoke()
    {
        testObject.initialize(finiteTreeNode1);
        testObject.initialize(finiteTreeNode2);

        Deque<TreeVertex> deque = testObject.vertexDeque;

        Assert.assertEquals(1, deque.size());
        Assert.assertFalse(deque.contains(finiteTreeNode1));
        Assert.assertTrue(deque.contains(finiteTreeNode2));
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
        testObject.initialize(Collections.singletonList(finiteTreeNode11));

        boolean result = testObject.hasNext();

        Assert.assertTrue(result);
    }
}
