package fitaview.automaton.transition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;
import fitaview.automaton.Wildcard;
import fitaview.utils.Pair;
import fitaview.utils.Triple;

public class BottomUpTransitionsTest
{
    private BottomUpTransitions<String> testObject;
    private final Variable variable = new Variable(0, "A", "B", "C", "D");

    public BottomUpTransitionsTest()
            throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new BottomUpTransitions<>(this::keyFunction, this::valueFunction);
        testObject.add(variable, Triple.make("A", "B", "0"), "C");
        testObject.add(variable, Triple.make(Wildcard.EVERY_VALUE, "C", "1"), "B");
        testObject.add(variable, Triple.make(Wildcard.SAME_VALUE, Wildcard.EVERY_VALUE, "2"), "A");
        testObject.add(variable, Triple.make("D", "C", "3"), Wildcard.RIGHT_VALUE);
        testObject.add(variable, Triple.make("C", "A", "4"), "C");
        testObject.add(variable, Triple.make(Wildcard.EVERY_VALUE, "A", "4"), "D");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void containsKey_WhenInnerDirectKey()
    {
        boolean result = testObject.containsKey(variable, Triple.make("A", "B", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsKey_WhenInnerWildcardKey()
    {
        boolean result =
                testObject.containsKey(variable, Triple.make(Wildcard.EVERY_VALUE, "C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsKey_WhenOuterKey()
    {
        boolean result = testObject.containsKey(variable, Triple.make("E", "E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void containsKey_WhenOuterWildcardKey()
    {
        boolean result =
                testObject.containsKey(variable, Triple.make("E", Wildcard.EVERY_VALUE, "1"));

        Assert.assertFalse(result);
    }

    @Test
    public void containsEntry_WhenInnerKey()
    {
        boolean result = testObject.containsEntry(variable, Triple.make("A", "B", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsEntry_WhenInnerWildcardEveryKey()
    {
        boolean result = testObject.containsEntry(variable, Triple.make("A", "C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsEntry_WhenInnerWildcardSameKey()
    {
        boolean result = testObject.containsEntry(variable, Triple.make("D", "D", "2"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsEntry_WhenOuterKey()
    {
        boolean result = testObject.containsEntry(variable, Triple.make("E", "B", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void containsEntry_WhenKeyHasNull()
    {
        boolean result = testObject.containsEntry(variable, Triple.make(null, "B", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void add_WhenDirectKey()
    {
        try
        {
            testObject.add(variable, Triple.make("D", "B", "10"), "A");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        boolean result = testObject.containsKey(variable, Triple.make("D", "B", "10"));

        Assert.assertTrue(result);
    }

    @Test
    public void add_WhenWildcardKey()
    {
        try
        {
            testObject.add(variable, Triple.make("D", Wildcard.EVERY_VALUE, "11"), "B");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        boolean result =
                testObject.containsKey(variable, Triple.make("D", Wildcard.EVERY_VALUE, "11"));
        boolean result0 = testObject.containsEntry(variable, Triple.make("D", "A", "11"));
        boolean result1 = testObject.containsEntry(variable, Triple.make("D", "B", "11"));
        boolean result2 = testObject.containsEntry(variable, Triple.make("D", "C", "11"));
        boolean result3 = testObject.containsEntry(variable, Triple.make("D", "D", "11"));

        Assert.assertTrue(result);
        Assert.assertTrue(result0);
        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test(expected = DuplicatedTransitionException.class)
    public void add_WhenDuplicated()
            throws DuplicatedTransitionException
    {
        try
        {
            testObject.add(variable, Triple.make("A", "B", "0"), "C");
        }
        catch(IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
    }

    @Test(expected = IllegalTransitionException.class)
    public void add_WhenIncorrectSameWildcard()
            throws IllegalTransitionException
    {
        try
        {
            testObject.add(variable, Triple.make("A", Wildcard.SAME_VALUE, "12"), "C");
        }
        catch(DuplicatedTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testGetAll()
    {
        List<String> result = null;

        try
        {
            result = testObject.getAll(variable, Triple.make("C", "A", "4"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertArrayEquals(new Object[]{"C", "D"}, result.toArray());
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getAll_WhenKeyHasNull()
            throws NoSuchTransitionException
    {
        testObject.getAll(variable, Triple.make(null, "B", "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getAll_WhenKeyHasNoEntry()
            throws NoSuchTransitionException
    {
        testObject.getAll(variable, Triple.make("C", "C", "0"));
    }

    @Test
    public void getMatched_WhenKeyDirectlyInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(variable, Triple.make("A", "B", "0"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("C", result);
    }

    @Test
    public void getMatched_WhenKeyExpectsWildcardEveryInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(variable, Triple.make("B", "C", "1"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("B", result);
    }

    @Test
    public void getMatched_WhenKeyExpectsWildcardSameInside()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(variable, Triple.make("C", "C", "2"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("A", result);
    }

    @Test
    public void getMatched_WhenWildcardInValue()
    {
        String result = null;

        try
        {
            result = testObject.getMatched(variable, Triple.make("D", "C", "3"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Wildcard.RIGHT_VALUE, result);
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getMatched_WhenKeyHasNull()
            throws NoSuchTransitionException
    {
        testObject.getMatched(variable, Triple.make(null, "B", "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getMatched_WhenKeyHasNoEntry()
            throws NoSuchTransitionException
    {
        testObject.getMatched(variable, Triple.make("C", "C", "0"));
    }

    @Test
    public void testConvertToStringMap()
    {
        Map<Pair<Variable, String>, String> result = testObject.convertToStringMap();
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(Pair.make(variable, keyFunction(Triple.make("A", "B", "0"))),
                     valueFunction("C"));
        expected.put(Pair.make(variable, keyFunction(Triple.make(Wildcard.EVERY_VALUE, "C", "1"))),
                     valueFunction("B"));
        expected.put(Pair.make(variable, keyFunction(
                Triple.make(Wildcard.SAME_VALUE, Wildcard.EVERY_VALUE, "2"))), valueFunction("A"));
        expected.put(Pair.make(variable, keyFunction(Triple.make("D", "C", "3"))),
                     valueFunction(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(variable, keyFunction(Triple.make("C", "A", "4"))),
                     valueFunction("C"));
        expected.put(Pair.make(variable, keyFunction(Triple.make(Wildcard.EVERY_VALUE, "A", "4"))),
                     valueFunction("D"));

        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.size());
        Assert.assertEquals(expected, result);
    }

    private String keyFunction(Triple<String, String, String> key)
    {
        return "[ %s # %s # %s ]".formatted(key.getFirst(), key.getSecond(), key.getThird());
    }

    private String valueFunction(String value)
    {
        return "[ %s ]".formatted(value);
    }
}
