package fitaview.automaton.traversing;

import org.junit.Assert;
import org.junit.Test;

public class TraversingFactoryTest
{
    @Test
    public void getTraversing_WhenTopDownDFS()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.DFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDfs);
    }

    @Test
    public void getTraversing_WhenTopDownBFS()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.BFS);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownBfs);
    }

    @Test
    public void getTraversing_WhenTopDownLevel()
    {
        TopDownTraversing result = TraversingFactory.getTopDownTraversing(TraversingMode.LEVEL);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownLevel);
    }

    @Test(expected = IncorrectTraversingException.class)
    public void getTraversing_WhenBottomUpDFS()
            throws IncorrectTraversingException
    {
        TraversingFactory.getBottomUpTraversing(TraversingMode.DFS);
    }

    @Test
    public void getTraversing_WhenBottomUpBFS()
    {
        BottomUpTraversing result = null;

        try
        {
            result = TraversingFactory.getBottomUpTraversing(TraversingMode.BFS);
        }
        catch(IncorrectTraversingException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpBfs);
    }

    @Test
    public void getTraversing_WhenBottomUpLevel()
    {
        BottomUpTraversing result = null;

        try
        {
            result = TraversingFactory.getBottomUpTraversing(TraversingMode.LEVEL);
        }
        catch(IncorrectTraversingException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpLevel);
    }
}
