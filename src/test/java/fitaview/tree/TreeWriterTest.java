package fitaview.tree;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeWriterTest
{
    private TreeWriter testObject;

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
    public void testToStringWhenEmptyTree()
    {
        testObject = new TreeWriter(null);

        String result = testObject.toString();

        Assert.assertEquals("", result);
    }

    @Test
    public void testToStringWhenSimpleTree()
    {
        TreeNode tree = null;

        try
        {
            tree = new StandardNode("1", 0, new StandardNode("2", 0, new StandardNode("3", 0),
                                                             new StandardNode("4", 0)),
                                    new StandardNode("5", 0));
        }
        catch(NodeHasParentException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject = new TreeWriter(tree);

        String result = testObject.toString();
        String expected =
            "<node label=\"1\">\n  <node label=\"2\">\n    <node label=\"3\" />\n    <node label=\"4\" />\n"
                + "  </node>\n  <node label=\"5\" />\n</node>\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenRepeat()
    {
        TreeNode tree = null;

        try
        {
            RepeatNode repeat = new RepeatNode("5", 0);

            repeat.setLeft(new StandardNode("6", 0));
            repeat.setRight(
                new StandardNode("7", 0, new RecNode(repeat, 0), new StandardNode("9", 0)));

            tree = new StandardNode("1", 0, new StandardNode("2", 0, new StandardNode("3", 0),
                                                             new StandardNode("4", 0)), repeat);
        }
        catch(NodeHasParentException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        testObject = new TreeWriter(tree);

        String result = testObject.toString();
        String expected =
            "<node label=\"1\">\n  <node label=\"2\">\n    <node label=\"3\" />\n    <node label=\"4\" />\n"
                + "  </node>\n  <repeat label=\"5\">\n    <node label=\"6\" />\n    <node label=\"7\">\n"
                + "      <rec />\n      <node label=\"9\" />\n    </node>\n  </repeat>\n</node>\n";

        Assert.assertEquals(expected, result);
    }
}
