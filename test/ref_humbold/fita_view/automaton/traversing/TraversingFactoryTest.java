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
        TopDownTraversing result = TraversingFactory.getInstance()
                                                    .getTopDownTraversing(TraversingMode.DFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFS);
    }

    @Test
    public void testGetTraversingWhenTopDownBFS()
    {
        TopDownTraversing result = TraversingFactory.getInstance()
                                                    .getTopDownTraversing(TraversingMode.BFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownBFS);
    }

    @Test
    public void testGetTraversingWhenTopDownLevel()
    {
        TopDownTraversing result = TraversingFactory.getInstance()
                                                    .getTopDownTraversing(TraversingMode.LEVEL);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownLevel);
    }

    @Test(expected = IncorrectTraversingException.class)
    public void testGetTraversingWhenBottomUpDFS()
        throws IncorrectTraversingException
    {
        TraversingFactory.getInstance().getBottomUpTraversing(TraversingMode.DFS);
    }

    @Test
    public void testGetTraversingWhenBottomUpBFS()
    {
        BottomUpTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance().getBottomUpTraversing(TraversingMode.BFS);
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
        BottomUpTraversing result = null;

        try
        {
            result = TraversingFactory.getInstance().getBottomUpTraversing(TraversingMode.LEVEL);
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
