package refhumbold.fitaview.tree;

import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import refhumbold.fitaview.Pair;

public class TreeReaderTest
{
    public static final String DIRECTORY = "test_files/TreeReaderTest/";
    private TreeReader testObject;

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
    public void testReadFiniteTree()
    {
        Pair<TreeNode, Integer> result = null;
        TreeNode expected = null;

        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadFiniteTree.tree.xml"));
            result = testObject.read();
            expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)),
                                        new StandardNode("5", 2));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result.getFirst());
        Assert.assertEquals(new Integer(3), result.getSecond());
    }

    @Test
    public void testReadWhenSingleRepeat()
    {
        Pair<TreeNode, Integer> result = null;

        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadWhenSingleRepeat.tree.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TreeNode expected = null;

        try
        {
            RepeatNode repeat = new RepeatNode("5", 2);

            repeat.setLeft(new StandardNode("6", 5));
            repeat.setRight(
                new StandardNode("7", 4, new RecNode(repeat, 9), new StandardNode("9", 8)));
            expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)), repeat);
        }
        catch(NodeHasParentException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result.getFirst());
        Assert.assertEquals(new Integer(4), result.getSecond());
    }

    @Test
    public void testReadWhenNestedRepeats()
    {
        Pair<TreeNode, Integer> result = null;

        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadWhenNestedRepeats.tree.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TreeNode expected = null;

        try
        {
            RepeatNode repeat5 = new RepeatNode("5", 2);
            RepeatNode repeat7 = new RepeatNode("7", 11);

            repeat7.setLeft(
                new StandardNode("8", 23, new RecNode(repeat7, 47), new RecNode(repeat7, 46)));
            repeat7.setRight(new StandardNode("11", 22));

            repeat5.setLeft(new StandardNode("6", 5, repeat7, new RecNode(repeat5, 10)));
            repeat5.setRight(
                new StandardNode("13", 4, new RecNode(repeat5, 9), new StandardNode("15", 8)));

            expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)),
                                        repeat5);
        }
        catch(NodeHasParentException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result.getFirst());
        Assert.assertEquals(new Integer(6), result.getSecond());
    }

    @Test(expected = TreeParsingException.class)
    public void testReadWhenRecOutOfScope()
        throws SAXException
    {
        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadWhenRecOutOfScope.tree.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = OneChildException.class)
    public void testReadWhenOneChild()
        throws SAXException
    {
        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadWhenOneChild.tree.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = TreeDepthException.class)
    public void testReadWhenTreeDepthIsViolated()
        throws SAXException
    {
        try
        {
            testObject = new TreeReader(
                new File(DIRECTORY + "testReadWhenTreeDepthIsViolated.tree.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = TreeParsingException.class)
    public void testReadWhenThreeChildren()
        throws SAXException
    {
        try
        {
            testObject = new TreeReader(new File(DIRECTORY + "testReadWhenThreeChildren.tree.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }
}
