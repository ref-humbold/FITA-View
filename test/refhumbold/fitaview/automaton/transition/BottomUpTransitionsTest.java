package refhumbold.fitaview.automaton.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.IllegalVariableValueException;
import refhumbold.fitaview.automaton.Variable;
import refhumbold.fitaview.automaton.Wildcard;

public class BottomUpTransitionsTest
{
    private BottomUpTransitions<String> testObject;
    private Variable v = new Variable(0, "A", "B", "C", "D");

    public BottomUpTransitionsTest()
        throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
        throws Exception
    {
        testObject = new BottomUpTransitions<>(this::keyFunction, this::valueFunction);
        testObject.add(v, Triple.make("A", "B", "0"), "C");
        testObject.add(v, Triple.make(Wildcard.EVERY_VALUE, "C", "1"), "B");
        testObject.add(v, Triple.make(Wildcard.SAME_VALUE, Wildcard.EVERY_VALUE, "2"), "A");
        testObject.add(v, Triple.make("D", "C", "3"), Wildcard.RIGHT_VALUE);
        testObject.add(v, Triple.make("C", "A", "4"), "C");
        testObject.add(v, Triple.make(Wildcard.EVERY_VALUE, "A", "4"), "D");
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
    public void testContainsEntryWhenKeyHasNull()
    {
        boolean result = testObject.containsEntry(v, Triple.make(null, "B", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void testAddWhenDirectKey()
    {
        try
        {
            testObject.add(v, Triple.make("D", "B", "10"), "A");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = testObject.containsKey(v, Triple.make("D", "B", "10"));

        Assert.assertTrue(result);
    }

    @Test
    public void testAddWhenWildcardKey()
    {
        try
        {
            testObject.add(v, Triple.make("D", Wildcard.EVERY_VALUE, "11"), "B");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean result = testObject.containsKey(v, Triple.make("D", Wildcard.EVERY_VALUE, "11"));
        boolean result0 = testObject.containsEntry(v, Triple.make("D", "A", "11"));
        boolean result1 = testObject.containsEntry(v, Triple.make("D", "B", "11"));
        boolean result2 = testObject.containsEntry(v, Triple.make("D", "C", "11"));
        boolean result3 = testObject.containsEntry(v, Triple.make("D", "D", "11"));

        Assert.assertTrue(result);
        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test(expected = DuplicatedTransitionException.class)
    public void testAddWhenDuplicated()
        throws DuplicatedTransitionException
    {
        try
        {
            testObject.add(v, Triple.make("A", "B", "0"), "C");
        }
        catch(IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalTransitionException.class)
    public void testAddWhenIncorrectSameWildcard()
        throws IllegalTransitionException
    {
        try
        {
            testObject.add(v, Triple.make("A", Wildcard.SAME_VALUE, "12"), "C");
        }
        catch(DuplicatedTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testGetAll()
    {
        List<String> result = null;

        try
        {
            result = testObject.getAll(v, Triple.make("C", "A", "4"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertArrayEquals(new Object[]{"C", "D"}, result.toArray());
    }

    @Test(expected = NoSuchTransitionException.class)
    public void testGetAllWhenKeyHasNull()
        throws NoSuchTransitionException
    {
        testObject.getAll(v, Triple.make(null, "B", "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void testGetAllWhenKeyHasNoEntry()
        throws NoSuchTransitionException
    {
        testObject.getAll(v, Triple.make("C", "C", "0"));
    }

    @Test
    public void testGetMatchedWhenKeyDirectlyInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(v, Triple.make("A", "B", "0"));
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
    public void testGetMatchedWhenKeyExpectsWildcardEveryInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(v, Triple.make("B", "C", "1"));
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
    public void testGetMatchedWhenKeyExpectsWildcardSameInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(v, Triple.make("C", "C", "2"));
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
    public void testGetMatchedWhenWildcardInValue()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(v, Triple.make("D", "C", "3"));
        }
        catch(NoSuchTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Wildcard.RIGHT_VALUE, result);
    }

    @Test(expected = NoSuchTransitionException.class)
    public void testGetMatchedWhenKeyHasNull()
        throws NoSuchTransitionException
    {
        testObject.getMatched(v, Triple.make(null, "B", "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void testGetMatchedWhenKeyHasNoEntry()
        throws NoSuchTransitionException
    {
        testObject.getMatched(v, Triple.make("C", "C", "0"));
    }

    @Test
    public void testConvertToStringMap()
    {
        Map<Pair<Variable, String>, String> result = testObject.convertToStringMap();
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(Pair.make(v, keyFunction(Triple.make("A", "B", "0"))), valueFunction("C"));
        expected.put(Pair.make(v, keyFunction(Triple.make(Wildcard.EVERY_VALUE, "C", "1"))),
                     valueFunction("B"));
        expected.put(
            Pair.make(v, keyFunction(Triple.make(Wildcard.SAME_VALUE, Wildcard.EVERY_VALUE, "2"))),
            valueFunction("A"));
        expected.put(Pair.make(v, keyFunction(Triple.make("D", "C", "3"))),
                     valueFunction(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(v, keyFunction(Triple.make("C", "A", "4"))), valueFunction("C"));
        expected.put(Pair.make(v, keyFunction(Triple.make(Wildcard.EVERY_VALUE, "A", "4"))),
                     valueFunction("D"));

        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.size());
        Assert.assertEquals(expected, result);
    }

    private String keyFunction(Triple<String, String, String> key)
    {
        return "[ " + key.getFirst() + " # " + key.getSecond() + " # " + key.getThird() + " ]";
    }

    private String valueFunction(String value)
    {
        return "[ " + value + " ]";
    }
}
