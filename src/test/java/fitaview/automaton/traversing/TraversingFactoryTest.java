package fitaview.automaton.traversing;

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
    public void testGetTraversingWhenTopDownDFS()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.DFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFS);
    }

    @Test
    public void testGetTraversingWhenTopDownBFS()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.BFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownBFS);
    }

    @Test
    public void testGetTraversingWhenTopDownLevel()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.LEVEL);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownLevel);
    }

    @Test(expected = IncorrectTraversingException.class)
    public void testGetTraversingWhenBottomUpDFS()
        throws IncorrectTraversingException
    {
        TraversingFactory.getBottomUpTraversing(TraversingMode.DFS);
    }

    @Test
    public void testGetTraversingWhenBottomUpBFS()
    {
        BottomUpTraversing result = null;

        try
        {
            result = TraversingFactory.getBottomUpTraversing(TraversingMode.BFS);
        }
        catch(IncorrectTraversingException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
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
            result = TraversingFactory.getBottomUpTraversing(TraversingMode.LEVEL);
        }
        catch(IncorrectTraversingException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpLevel);
    }
}
