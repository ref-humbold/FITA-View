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
    public void testRead()
    {
        TreeVertex result = null;

        try
        {
            testObject = new TreeReader("test_files/TreeReader_testRead.xml");
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

        TreeVertex repeat = new NodeVertex("5", 5);
        TreeVertex rec = new RecVertex(repeat, -1);

        repeat.setLeft(new NodeVertex("6", 6));
        repeat.setRight(new NodeVertex("7", 7, rec, new NodeVertex("8", 8)));

        TreeVertex expected = new NodeVertex("1", 1, new NodeVertex("2", 2, new NodeVertex("3", 3),
                                                                    new NodeVertex("4", 4)),
                                             repeat);

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
    }
}
