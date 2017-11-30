package ref_humbold.fita_view.automaton;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VariableTest
{
    private Variable testObject;

    @Before
    public void setUp()
    {
        try
        {
            testObject = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testConstructorWhenInitIsNull()
        throws IllegalVariableValueException
    {
        testObject = new Variable(null);
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testConstructorWhenInitIsEmpty()
        throws IllegalVariableValueException
    {
        testObject = new Variable("");
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testConstructorWhenValueIsNull()
        throws IllegalVariableValueException
    {
        testObject = new Variable("A", "B", "C", null);
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testConstructorWhenValueIsEmpty()
        throws IllegalVariableValueException
    {
        testObject = new Variable("A", "B", "C", "");
    }

    @Test
    public void testGetInitValue()
    {
        String result = testObject.getInitValue();

        Assert.assertNotNull(result);
        Assert.assertEquals("A", result);
    }

    @Test
    public void testSize()
    {
        int result = testObject.size();

        Assert.assertEquals(3, result);
    }

    @Test
    public void testIsEmpty()
    {
        boolean result = testObject.isEmpty();

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsWhenInSet()
    {
        boolean resultA = testObject.contains("A");
        boolean resultB = testObject.contains("B");
        boolean resultC = testObject.contains("C");

        Assert.assertTrue(resultA);
        Assert.assertTrue(resultB);
        Assert.assertTrue(resultC);
    }

    @Test
    public void testContainsWhenOutOfSet()
    {
        boolean resultD = testObject.contains("D");

        Assert.assertFalse(resultD);
    }

    @Test
    public void testContainsWhenNull()
    {
        boolean result = testObject.contains(null);

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsWhenEmpty()
    {
        boolean result = testObject.contains("");

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsAllWhenInSet()
    {
        Collection<String> collection = Arrays.asList("C", "A");

        boolean result = testObject.containsAll(collection);

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsAllWhenOutOfSet()
    {
        Collection<String> collection = Arrays.asList("B", "D");

        boolean result = testObject.containsAll(collection);

        Assert.assertFalse(result);
    }

    @Test
    public void testContainsAllWhenNullAndEmpty()
    {
        Collection<String> collection = Arrays.asList("C", null, "");

        boolean result = testObject.containsAll(collection);

        Assert.assertFalse(result);
    }

    @Test
    public void testToString()
    {
        String result = testObject.toString();

        Assert.assertEquals("Variable::[A, B, C]", result);
    }
}
