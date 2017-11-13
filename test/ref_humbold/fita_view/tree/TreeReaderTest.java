package ref_humbold.fita_view.tree;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TreeReaderTest
{
    private TreeReader testObject;

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testReadFiniteTree()
    {
        TreeVertex result = null;

        try
        {
            testObject = new TreeReader("test_files/TreeReader_testReadFiniteTree.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TreeVertex expected = new NodeVertex("1", 1, new NodeVertex("2", 2, new NodeVertex("3", 3),
                                                                    new NodeVertex("4", 4)),
                                             new NodeVertex("5", 5));

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadWhenSingleRepeat()
    {
        TreeVertex result = null;

        try
        {
            testObject = new TreeReader("test_files/TreeReader_testReadWhenSingleRepeat.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TreeVertex repeat = new RepeatVertex("5", 5);

        repeat.setLeft(new NodeVertex("6", 6));
        repeat.setRight(new NodeVertex("7", 7, new RecVertex(repeat, 6), new NodeVertex("9", 9)));

        TreeVertex expected = new NodeVertex("1", 1, new NodeVertex("2", 2, new NodeVertex("3", 3),
                                                                    new NodeVertex("4", 4)),
                                             repeat);

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadWhenNestedRepeats()
    {
        TreeVertex result = null;

        try
        {
            testObject = new TreeReader("test_files/TreeReader_testReadWhenNestedRepeats.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TreeVertex repeat5 = new RepeatVertex("5", 5);
        TreeVertex repeat7 = new RepeatVertex("7", 7);

        repeat7.setLeft(
            new NodeVertex("8", 8, new RecVertex(repeat7, 9), new RecVertex(repeat7, 10)));
        repeat7.setRight(new NodeVertex("11", 1));

        repeat5.setLeft(new NodeVertex("6", 6, repeat7, new RecVertex(repeat5, 12)));
        repeat5.setRight(
            new NodeVertex("13", 13, new RecVertex(repeat5, 14), new NodeVertex("15", 15)));

        TreeVertex expected = new NodeVertex("1", 1, new NodeVertex("2", 2, new NodeVertex("3", 3),
                                                                    new NodeVertex("4", 4)),
                                             repeat5);

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }
}
