package ref_humbold.fita_view.automaton.transition;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.automaton.Wildcard;

public class TopDownTransitionsTest
{
    private TopDownTransitions<Pair<String, String>> testObject;
    private Variable v = new Variable("A", "B", "C", "D");

    public TopDownTransitionsTest()
        throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
    {
        testObject = new TopDownTransitions<>();
        testObject.add(v, Pair.make("A", "0"), Pair.make("B", "C"));
        testObject.add(v, Pair.make(Wildcard.EVERY_VALUE, "1"), Pair.make("D", "D"));
        testObject.add(v, Pair.make("D", "2"), Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE));
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testContainsKeyWhenInnerDirectKey()
    {
        boolean result = testObject.containsKey(v, Pair.make("A", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsKeyWhenInnerWildcardKey()
    {
        boolean result = testObject.containsKey(v, Pair.make(Wildcard.EVERY_VALUE, "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsKeyWhenOuterKey()
    {
        boolean result = testObject.containsKey(v, Pair.make("E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsKeyWhenOuterWildcardKey()
    {
        boolean result = testObject.containsKey(v, Pair.make("E", Wildcard.EVERY_VALUE));

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsEntryWhenInnerKey()
    {
        boolean result = testObject.containsEntry(v, Pair.make("A", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsEntryWhenInnerWildcardKey()
    {
        boolean result = testObject.containsEntry(v, Pair.make("C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsEntryWhenOuterKey()
    {
        boolean result = testObject.containsEntry(v, Pair.make("E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void testAddWhenDirectKey()
    {
        testObject.add(v, Pair.make("B", "0"), Pair.make("A", "A"));

        boolean result = testObject.containsKey(v, Pair.make("B", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void testAddWhenWildcardKey()
    {
        testObject.add(v, Pair.make(Wildcard.EVERY_VALUE, "4"), Pair.make("B", "B"));

        boolean result = testObject.containsKey(v, Pair.make(Wildcard.EVERY_VALUE, "4"));
        boolean result0 = testObject.containsEntry(v, Pair.make("A", "4"));
        boolean result1 = testObject.containsEntry(v, Pair.make("B", "4"));
        boolean result2 = testObject.containsEntry(v, Pair.make("C", "4"));
        boolean result3 = testObject.containsEntry(v, Pair.make("D", "4"));

        Assert.assertTrue(result);
        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test
    public void testGetWhenKeyDirectlyInside()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.get(v, Pair.make("A", "0"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make("B", "C"), result);
    }

    @Test
    public void testGetWhenKeyExpectsWildcardInside()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.get(v, Pair.make("B", "1"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make("D", "D"), result);
    }

    @Test
    public void testGetWhenWildcardInValue()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.get(v, Pair.make("D", "2"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE), result);
    }
}
