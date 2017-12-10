package ref_humbold.fita_view.automaton.traversing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TraversingFactoryTest
{
    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testGetInstance()
    {
        TraversingFactory result = TraversingFactory.getInstance();

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetTraversingWhenTopDownDFS()
    {
        TreeTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance()
                                      .getTraversing(TraversingMode.DFS,
                                                     TraversingDirection.TOP_DOWN);
        }
        catch(IncorrectTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFS);
    }

    @Test
    public void testGetTraversingWhenTopDownBFS()
    {
        TreeTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance()
                                      .getTraversing(TraversingMode.BFS,
                                                     TraversingDirection.TOP_DOWN);
        }
        catch(IncorrectTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownBFS);
    }

    @Test
    public void testGetTraversingWhenTopDownLevel()
    {
        TreeTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance()
                                      .getTraversing(TraversingMode.LEVEL,
                                                     TraversingDirection.TOP_DOWN);
        }
        catch(IncorrectTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownLevel);
    }

    @Test(expected = IncorrectTraversingException.class)
    public void testGetTraversingWhenBottomUpDFS()
        throws IncorrectTraversingException
    {
        TraversingFactory.getInstance()
                         .getTraversing(TraversingMode.DFS, TraversingDirection.BOTTOM_UP);
    }

    @Test
    public void testGetTraversingWhenBottomUpBFS()
    {
        TreeTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance()
                                      .getTraversing(TraversingMode.BFS,
                                                     TraversingDirection.BOTTOM_UP);
        }
        catch(IncorrectTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpBFS);
    }

    @Test
    public void testGetTraversingWhenBottomUpLevel()
    {
        TreeTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance()
                                      .getTraversing(TraversingMode.LEVEL,
                                                     TraversingDirection.BOTTOM_UP);
        }
        catch(IncorrectTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpLevel);
    }
}
