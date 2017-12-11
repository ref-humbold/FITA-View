package ref_humbold.fita_view.automaton.transition;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.IllegalVariableValueException;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.automaton.Wildcard;

public class BottomUpTransitionsTest
{
    private BottomUpTransitions testObject;
    private Variable v = new Variable("A", "B", "C", "D");

    public BottomUpTransitionsTest()
        throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
    {
        testObject = new BottomUpTransitions();
        testObject.add(v, Triple.make("A", "B", "0"), "C");
        testObject.add(v, Triple.make(Wildcard.EVERY_VALUE, "C", "1"), "B");
        testObject.add(v, Triple.make(Wildcard.SAME_VALUE, Wildcard.EVERY_VALUE, "2"), "A");
        testObject.add(v, Triple.make("D", "C", "3"), Wildcard.RIGHT_VALUE);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testContainsKeyWhenInnerDirectKey()
    {
        boolean result = testObject.containsKey(v, Triple.make("A", "B", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsKeyWhenInnerWildcardKey()
    {
        boolean result = testObject.containsKey(v, Triple.make(Wildcard.EVERY_VALUE, "C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsKeyWhenOuterKey()
    {
        boolean result = testObject.containsKey(v, Triple.make("E", "E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsKeyWhenOuterWildcardKey()
    {
        boolean result = testObject.containsKey(v, Triple.make("E", Wildcard.EVERY_VALUE, "1"));

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsEntryWhenInnerKey()
    {
        boolean result = testObject.containsEntry(v, Triple.make("A", "B", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsEntryWhenInnerWildcardEveryKey()
    {
        boolean result = testObject.containsEntry(v, Triple.make("A", "C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsEntryWhenInnerWildcardSameKey()
    {
        boolean result = testObject.containsEntry(v, Triple.make("D", "D", "2"));

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsEntryWhenOuterKey()
    {
        boolean result = testObject.containsEntry(v, Triple.make("E", "B", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void testAddWhenDirectKey()
    {
        testObject.add(v, Triple.make("D", "B", "4"), "A");

        boolean result = testObject.containsKey(v, Triple.make("D", "B", "4"));

        Assert.assertTrue(result);
    }

    @Test
    public void testAddWhenWildcardKey()
    {
        testObject.add(v, Triple.make("D", Wildcard.EVERY_VALUE, "4"), "B");

        boolean result = testObject.containsKey(v, Triple.make("D", Wildcard.EVERY_VALUE, "4"));
        boolean result0 = testObject.containsEntry(v, Triple.make("D", "A", "4"));
        boolean result1 = testObject.containsEntry(v, Triple.make("D", "B", "4"));
        boolean result2 = testObject.containsEntry(v, Triple.make("D", "C", "4"));
        boolean result3 = testObject.containsEntry(v, Triple.make("D", "D", "4"));

        Assert.assertTrue(result);
        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test
    public void testGetWhenKeyDirectlyInside()
    {
        String result = null;

        try
        {
            result = testObject.get(v, Triple.make("A", "B", "0"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("C", result);
    }

    @Test
    public void testGetWhenKeyExpectsWildcardEveryInside()
    {
        String result = null;

        try
        {
            result = testObject.get(v, Triple.make("B", "C", "1"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("B", result);
    }

    @Test
    public void testGetWhenKeyExpectsWildcardSameInside()
    {
        String result = null;

        try
        {
            result = testObject.get(v, Triple.make("C", "C", "2"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("A", result);
    }

    @Test
    public void testGetWhenWildcardInValue()
    {
        String result = null;

        try
        {
            result = testObject.get(v, Triple.make("D", "C", "3"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Wildcard.RIGHT_VALUE, result);
    }
}
