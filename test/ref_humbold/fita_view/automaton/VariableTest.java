package ref_humbold.fita_view.automaton;

import java.util.ArrayList;
import java.util.Iterator;
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
    public void testContainsWhenInnerValue()
    {
        boolean resultA = testObject.contains("A");
        boolean resultB = testObject.contains("B");
        boolean resultC = testObject.contains("C");

        Assert.assertTrue(resultA);
        Assert.assertTrue(resultB);
        Assert.assertTrue(resultC);
    }

    @Test
    public void testContainsWhenOuterValue()
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
    public void testIterator()
    {
        Iterator<String> iterator = testObject.iterator();

        ArrayList<String> result = new ArrayList<>();

        while(iterator.hasNext())
            result.add(iterator.next());
        
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains("A"));
        Assert.assertTrue(result.contains("B"));
        Assert.assertTrue(result.contains("C"));
    }

    @Test
    public void testToString()
    {
        String result = testObject.toString();

        Assert.assertEquals("Variable::[A, B, C]", result);
    }
}
