package ref_humbold.fita_view.automaton;

import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.tree.TreeNode;

public class NullAutomatonTest
{
    private NullAutomaton testObject;

    @Before
    public void setUp()
    {
        testObject = NullAutomaton.getInstance();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testGetTypeName()
    {
        String result = testObject.getTypeName();

        Assert.assertEquals("Automaton", result);
    }

    @Test
    public void testGetVariables()
    {
        List<Variable> result = testObject.getVariables();

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAlphabet()
    {
        Set<String> result = testObject.getAlphabet();

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testIsAccepted()
    {
        boolean result = testObject.isAccepted();

        Assert.assertFalse(result);
    }

    @Test
    public void testIsInAlphabet()
    {
        boolean result = testObject.isInAlphabet("name");

        Assert.assertFalse(result);
    }

    @Test
    public void testGenerateTree()
    {
        TreeNode result = testObject.generateTree();

        Assert.assertNull(result);
    }
}
