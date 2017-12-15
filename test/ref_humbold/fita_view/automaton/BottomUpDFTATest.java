package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.tree.NodeVertex;
import ref_humbold.fita_view.tree.TreeVertex;

public class BottomUpDFTATest
{
    private BottomUpDFTA testObject;
    private List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private List<Variable> variables = new ArrayList<>();
    private TreeVertex tree = new NodeVertex("0", 1, null, null);

    @Before
    public void setUp()
    {
        testObject = new BottomUpDFTA(alphabet, variables);
        testObject.setTree(tree);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Ignore(value = "Test not implemented yet.")
    @Test
    public void testRun()
    {
        try
        {
            testObject.run();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Ignore(value = "Test not implemented yet.")
    @Test
    public void testMakeStepForward()
    {
        try
        {
            testObject.makeStepForward();
        }
        catch(IllegalVariableValueException | NoSuchTransitionException | NoTraversingException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Ignore(value = "Test not implemented yet.")
    @Test
    public void testIsAcceptedWhenAutomatonHasRun()
    {
    }

    @Test
    public void testIsAcceptedWhenAutomatonHasNotRun()
    {
        boolean result = testObject.isAccepted();

        Assert.assertFalse(result);
    }

    @Test
    public void testIsInAlphabetWhenValueInAlphabet()
    {
        boolean result = testObject.isInAlphabet("0");

        Assert.assertTrue(result);
    }

    @Test
    public void testIsInAlphabetWhenValueOutOfAlphabet()
    {
        boolean result = testObject.isInAlphabet("5");

        Assert.assertFalse(result);
    }
}
