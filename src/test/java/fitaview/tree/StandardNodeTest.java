package fitaview.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;

public class StandardNodeTest
{
    private StandardNode testObject;
    private Variable variable1 = new Variable(1, "0", "1", "2", "3");
    private Variable variable2 = new Variable(2, "X", "Y", "Z");

    public StandardNodeTest()
            throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
            throws IllegalVariableValueException
    {
        testObject = new StandardNode("LABEL", 0);
        testObject.setStateValue(variable1, "3");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenNullLabel()
    {
        testObject = new StandardNode(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWhenEmptyLabel()
    {
        testObject = new StandardNode("", 0);
    }

    @Test
    public void testToString()
    {
        try
        {
            testObject.setLeft(new StandardNode("left", 3));
            testObject.setRight(new StandardNode("right", 2));
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        String result = testObject.toString();

        Assert.assertEquals("<$ 'LABEL', <$ 'left', #, # $>, <$ 'right', #, # $> $>", result);
    }

    @Test
    public void testSetLeftWhenRoot()
    {
        StandardNode node = new StandardNode("A", 1);

        try
        {
            testObject.setLeft(node);
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertSame(node, testObject.getLeft());
        Assert.assertSame(testObject, node.getParent());
    }

    @Test
    public void testSetLeftWhenChange()
    {
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);

        try
        {
            testObject.setLeft(node1);
            testObject.setLeft(node2);
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNull(node1.getParent());
        Assert.assertSame(node2, testObject.getLeft());
        Assert.assertSame(testObject, node2.getParent());
    }

    @Test(expected = NodeHasParentException.class)
    public void testSetLeftWhenHasParent()
            throws NodeHasParentException
    {
        StandardNode node = null;

        try
        {
            node = new StandardNode("A", 1, new StandardNode("B", 2), new StandardNode("C", 3));
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.setLeft(node.getLeft());
    }

    @Test
    public void testSetRightWhenRoot()
    {
        StandardNode node = new StandardNode("A", 1);

        try
        {
            testObject.setRight(node);
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertSame(node, testObject.getRight());
        Assert.assertSame(testObject, node.getParent());
    }

    @Test
    public void testSetRightWhenChange()
    {
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);

        try
        {
            testObject.setRight(node1);
            testObject.setRight(node2);
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNull(node1.getParent());
        Assert.assertSame(node2, testObject.getRight());
        Assert.assertSame(testObject, node2.getParent());
    }

    @Test(expected = NodeHasParentException.class)
    public void testSetRightWhenHasParent()
            throws NodeHasParentException
    {
        StandardNode node = null;

        try
        {
            node = new StandardNode("A", 1, new StandardNode("B", 2), new StandardNode("C", 3));
        }
        catch(NodeHasParentException e)
        {
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        testObject.setRight(node.getLeft());
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
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
        }

        Assert.assertNotNull(result);
        Assert.assertEquals(Collections.singletonMap(variable1, "3"), result);
    }

    @Test(expected = UndefinedStateValueException.class)
    public void testGetStateValueWhenNoValue()
            throws UndefinedStateValueException
    {
        testObject.getStateValue(variable2);
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
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
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
            Assert.fail(String.format("Unexpected exception %s", e.getClass().getSimpleName()));
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
