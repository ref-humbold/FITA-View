package ref_humbold.fita_view.tree;

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
    }

    @Test
    public void testToStringWhenNull()
    {
        testObject = new TreeWriter(null);

        String result = testObject.toString();

        Assert.assertEquals("", result);
    }

    @Test
    public void testToStringWhenSimpleTree()
    {
        TreeVertex tree =
            new NodeVertex("1", new NodeVertex("2", new NodeVertex("3"), new NodeVertex("4")),
                           new NodeVertex("5"));

        testObject = new TreeWriter(tree);

        String result = testObject.toString();
        String expected =
            "<node label=\"1\">\n  <node label=\"2\">\n    <node label=\"3\" />\n    <node label=\"4\" />\n"
                + "  </node>\n  <node label=\"5\" />\n</node>\n";

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWhenSingleRepeat()
    {
        TreeVertex repeat = new RepeatVertex("5");

        repeat.setLeft(new NodeVertex("6"));
        repeat.setRight(new NodeVertex("7", new RecVertex(repeat), new NodeVertex("9")));

        TreeVertex tree =
            new NodeVertex("1", new NodeVertex("2", new NodeVertex("3"), new NodeVertex("4")),
                           repeat);

        testObject = new TreeWriter(tree);

        String result = testObject.toString();
        String expected =
            "<node label=\"1\">\n  <node label=\"2\">\n    <node label=\"3\" />\n    <node label=\"4\" />\n"
                + "  </node>\n  <repeat label=\"5\">\n    <node label=\"6\" />\n    <node label=\"7\">\n"
                + "      <rec />\n      <node label=\"9\" />\n    </node>\n  </repeat>\n</node>\n";

        Assert.assertEquals(expected, result);
    }
}
