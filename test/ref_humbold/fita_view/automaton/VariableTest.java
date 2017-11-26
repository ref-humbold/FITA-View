package ref_humbold.fita_view.automaton;

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
        testObject = new Variable("A", "B", "C");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testGetInitValue()
    {
        String result = testObject.getInitValue();

        Assert.assertNotNull(result);
        Assert.assertEquals("A", result);
    }

    @Test
    public void testIsValueWhenInSet()
    {
        boolean resultA = testObject.isValue("A");
        boolean resultB = testObject.isValue("B");
        boolean resultC = testObject.isValue("C");

        Assert.assertTrue(resultA);
        Assert.assertTrue(resultB);
        Assert.assertTrue(resultC);
    }

    @Test
    public void testIsValueWhenOutOfSet()
    {
        boolean resultD = testObject.isValue("D");

        Assert.assertFalse(resultD);
    }

    @Test
    public void testAddValueWhenNewValue()
    {
        try
        {
            testObject.addValue("D");
        }
        catch(IncorrectValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean resultD = testObject.isValue("D");

        Assert.assertTrue(resultD);
    }

    @Test
    public void testAddValueWhenExistingValue()
    {
        try
        {
            testObject.addValue("A");
        }
        catch(IncorrectValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        boolean resultA = testObject.isValue("A");

        Assert.assertTrue(resultA);
    }

    @Test(expected = IncorrectValueException.class)
    public void testAddValueWhenNull()
        throws IncorrectValueException
    {
        testObject.addValue(null);
    }

    @Test
    public void testToString()
    {
        String result = testObject.toString();

        Assert.assertEquals("Variable of [A, B, C]", result);
    }
}
