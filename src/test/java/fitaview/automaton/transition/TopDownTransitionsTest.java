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

public class TopDownTransitionsTest
{
    private TopDownTransitions<Pair<String, String>> testObject;
    private final Variable variable = new Variable(0, "A", "B", "C", "D");

    public TopDownTransitionsTest()
            throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new TopDownTransitions<>(this::pairFunction, this::pairFunction);
        testObject.add(variable, Pair.make("A", "0"), Pair.make("B", "C"));
        testObject.add(variable, Pair.make(Wildcard.EVERY_VALUE, "1"), Pair.make("D", "D"));
        testObject.add(variable, Pair.make("D", "2"),
                       Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE));
        testObject.add(variable, Pair.make("C", "3"), Pair.make("B", "A"));
        testObject.add(variable, Pair.make(Wildcard.EVERY_VALUE, "3"), Pair.make("C", "C"));
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void containsKey_WhenInnerDirectKey()
    {
        boolean result = testObject.containsKey(variable, Pair.make("A", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsKey_WhenInnerWildcardKey()
    {
        boolean result = testObject.containsKey(variable, Pair.make(Wildcard.EVERY_VALUE, "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsKey_WhenOuterKey()
    {
        boolean result = testObject.containsKey(variable, Pair.make("E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void containsKey_WhenOuterWildcardKey()
    {
        boolean result = testObject.containsKey(variable, Pair.make("E", Wildcard.EVERY_VALUE));

        Assert.assertFalse(result);
    }

    @Test
    public void containsEntry_WhenInnerKey()
    {
        boolean result = testObject.containsEntry(variable, Pair.make("A", "0"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsEntry_WhenInnerWildcardKey()
    {
        boolean result = testObject.containsEntry(variable, Pair.make("C", "1"));

        Assert.assertTrue(result);
    }

    @Test
    public void containsEntry_WhenOuterKey()
    {
        boolean result = testObject.containsEntry(variable, Pair.make("E", "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void containsEntry_WhenKeyHasNull()
    {
        boolean result = testObject.containsEntry(variable, Pair.make(null, "0"));

        Assert.assertFalse(result);
    }

    @Test
    public void add_WhenDirectKey()
    {
        try
        {
            testObject.add(variable, Pair.make("B", "10"), Pair.make("A", "A"));
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        boolean result = testObject.containsKey(variable, Pair.make("B", "10"));

        Assert.assertTrue(result);
    }

    @Test
    public void add_WhenWildcardKey()
    {
        try
        {
            testObject.add(variable, Pair.make(Wildcard.EVERY_VALUE, "11"), Pair.make("B", "B"));
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        boolean result = testObject.containsKey(variable, Pair.make(Wildcard.EVERY_VALUE, "11"));
        boolean result0 = testObject.containsEntry(variable, Pair.make("A", "11"));
        boolean result1 = testObject.containsEntry(variable, Pair.make("B", "11"));
        boolean result2 = testObject.containsEntry(variable, Pair.make("C", "11"));
        boolean result3 = testObject.containsEntry(variable, Pair.make("D", "11"));

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
            testObject.add(variable, Pair.make("A", "0"), Pair.make("B", "C"));
        }
        catch(IllegalTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
    }

    @Test
    public void testGetAll()
    {
        List<Pair<String, String>> result = null;

        try
        {
            result = testObject.getAll(variable, Pair.make("C", "3"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertArrayEquals(new Object[]{Pair.make("B", "A"), Pair.make("C", "C")},
                                 result.toArray());
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getAll_WhenKeyHasNull()
            throws NoSuchTransitionException
    {
        testObject.getAll(variable, Pair.make(null, "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getAll_WhenKeyHasNoEntry()
            throws NoSuchTransitionException
    {
        testObject.getAll(variable, Pair.make("C", "0"));
    }

    @Test
    public void getMatched_WhenKeyDirectlyInside()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.getMatched(variable, Pair.make("A", "0"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make("B", "C"), result);
    }

    @Test
    public void getMatched_WhenKeyExpectsWildcardInside()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.getMatched(variable, Pair.make("B", "1"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make("D", "D"), result);
    }

    @Test
    public void getMatched_WhenWildcardInValue()
    {
        Pair<String, String> result = null;

        try
        {
            result = testObject.getMatched(variable, Pair.make("D", "2"));
        }
        catch(NoSuchTransitionException e)
        {
            Assert.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE), result);
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getMatched_WhenKeyHasNull()
            throws NoSuchTransitionException
    {
        testObject.getMatched(variable, Pair.make(null, "0"));
    }

    @Test(expected = NoSuchTransitionException.class)
    public void getMatched_WhenKeyHasNoEntry()
            throws NoSuchTransitionException
    {
        testObject.getMatched(variable, Pair.make("C", "0"));
    }

    @Test
    public void testConvertToStringMap()
    {
        Map<Pair<Variable, String>, String> result = testObject.convertToStringMap();
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(Pair.make(variable, pairFunction(Pair.make("A", "0"))),
                     pairFunction(Pair.make("B", "C")));
        expected.put(Pair.make(variable, pairFunction(Pair.make(Wildcard.EVERY_VALUE, "1"))),
                     pairFunction(Pair.make("D", "D")));
        expected.put(Pair.make(variable, pairFunction(Pair.make("D", "2"))),
                     pairFunction(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variable, pairFunction(Pair.make("C", "3"))),
                     pairFunction(Pair.make("B", "A")));
        expected.put(Pair.make(variable, pairFunction(Pair.make(Wildcard.EVERY_VALUE, "3"))),
                     pairFunction(Pair.make("C", "C")));

        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.size());
        Assert.assertEquals(expected, result);
    }

    private String pairFunction(Pair<String, String> pair)
    {
        return "[ %s # %s ]".formatted(pair.getFirst(), pair.getSecond());
    }
}
