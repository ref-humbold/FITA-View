package refhumbold.fitaview.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import refhumbold.fitaview.automaton.IllegalVariableValueException;
import refhumbold.fitaview.automaton.Variable;

public class RecNodeTest
{
    private RecNode testObject;
    private Variable variable1 = new Variable(1, "0", "1", "2", "3");
    private Variable variable2 = new Variable(2, "X", "Y", "Z");

    public RecNodeTest()
        throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
        throws IllegalVariableValueException
    {
        testObject = new RecNode(new RepeatNode("LABEL", 0), 10);
        testObject.setStateValue(variable1, "3");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenNullRecursive()
    {
        testObject = new RecNode(null, 0);
    }

    @Test
    public void testToString()
    {
        String result = testObject.toString();

        Assert.assertEquals("<@ REC @>", result);
    }

    @Test
    public void testGetLabel()
    {
        String result = testObject.getLabel();

        Assert.assertNotNull(result);
        Assert.assertEquals("LABEL", result);
    }

    @Test
    public void testGetState()
    {
        Map<Variable, String> result = null;

        try
        {
            result = testObject.getState();
        }
        catch(UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Collections.singletonMap(variable1, "3"), result);
    }

    @Test(expected = UndefinedStateValueException.class)
    public void testGetStateValueWhenNoValue()
        throws UndefinedStateValueException
    {
        String result = testObject.getStateValue(variable2);
    }

    @Test
    public void testGetStateValueWhenIsValue()
    {
        String result = null;
        try
        {
            result = testObject.getStateValue(variable1);
        }
        catch(UndefinedStateValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertEquals("3", result);
    }

    @Test
    public void testGetStateValueOrNullWhenNoValue()
    {
        String result = testObject.getStateValueOrNull(variable2);

        Assert.assertNull(result);
    }

    @Test
    public void testGetStateValueOrNullWhenIsValue()
    {
        String result = testObject.getStateValueOrNull(variable1);

        Assert.assertNotNull(result);
        Assert.assertEquals("3", result);
    }

    @Test
    public void testSetStateValueWhenIsValue()
    {
        try
        {
            testObject.setStateValue(variable2, "Y");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        String result = testObject.getStateValueOrNull(variable2);

        Assert.assertNotNull(result);
        Assert.assertEquals("Y", result);
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testSetStateValueWhenIncorrectValue()
        throws IllegalVariableValueException
    {
        testObject.setStateValue(variable2, "N");
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testSetStateValueWhenEmptyValue()
        throws IllegalVariableValueException
    {
        testObject.setStateValue(variable2, "");
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testSetStateValueWhenNull()
        throws IllegalVariableValueException
    {
        testObject.setStateValue(variable2, null);
    }

    @Test
    public void testSetInitialState()
    {
        testObject.setInitialState(Arrays.asList(variable1, variable2));

        String result1 = testObject.getStateValueOrNull(variable1);
        String result2 = testObject.getStateValueOrNull(variable2);

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(variable1.getInitValue(), result1);
        Assert.assertEquals(variable2.getInitValue(), result2);
    }

    @Test
    public void testDeleteState()
    {
        testObject.deleteState();

        Map<Variable, String> result = testObject.getStateWithNulls();

        Assert.assertTrue(result.isEmpty());
    }
}
