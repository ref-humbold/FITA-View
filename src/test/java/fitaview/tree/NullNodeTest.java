package fitaview.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;

public class NullNodeTest
{
    private NullNode testObject;
    private final Variable variable1 = new Variable(1, "0", "1", "2", "3");
    private final Variable variable2 = new Variable(2, "X", "Y", "Z");

    public NullNodeTest()
            throws IllegalVariableValueException
    {
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new NullNode();
        testObject.setStateValue(variable1, "3");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testGetType()
    {
        // when
        NodeType result = testObject.getType();
        // then
        Assertions.assertThat(result).isEqualTo(NodeType.NULL);
    }

    @Test
    public void testGetLeft()
    {
        // when
        TreeNode result = testObject.getLeft();
        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testGetRight()
    {
        // when
        TreeNode result = testObject.getRight();
        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testGetLabel()
    {
        // when
        String result = testObject.getLabel();
        // then
        Assertions.assertThat(result).isEqualTo("");
    }

    @Test
    public void testGetState()
    {
        // when
        Map<Variable, String> result = null;

        try
        {
            result = testObject.getState();
        }
        catch(UndefinedStateValueException e)
        {
            Assertions.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(Collections.singletonMap(variable1, "3"));
    }

    @Test
    public void testGetStateValueWhenNoValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.getStateValue(variable2))
                  .isInstanceOf(UndefinedStateValueException.class);
    }

    @Test
    public void testGetStateValueWhenIsValue()
    {
        // when
        String result = null;

        try
        {
            result = testObject.getStateValue(variable1);
        }
        catch(UndefinedStateValueException e)
        {
            Assertions.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("3");
    }

    @Test
    public void testGetStateValueOrNullWhenNoValue()
    {
        // when
        String result = testObject.getStateValueOrNull(variable2);
        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void testGetStateValueOrNullWhenIsValue()
    {
        // when
        String result = testObject.getStateValueOrNull(variable1);
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("3");
    }

    @Test
    public void testSetStateValueWhenIsValue()
    {
        // given
        try
        {
            testObject.setStateValue(variable2, "Y");
        }
        catch(IllegalVariableValueException e)
        {
            Assertions.fail("Unexpected exception %s".formatted(e.getClass().getSimpleName()));
        }
        // when
        String result = testObject.getStateValueOrNull(variable2);
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("Y");
    }

    @Test
    public void testSetStateValueWhenIncorrectValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, "N"))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void testSetStateValueWhenEmptyValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, ""))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void testSetStateValueWhenNull()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, null))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void testSetInitialState()
    {
        // given
        testObject.setInitialState(Arrays.asList(variable1, variable2));
        // when
        String result1 = testObject.getStateValueOrNull(variable1);
        String result2 = testObject.getStateValueOrNull(variable2);
        // then
        Assertions.assertThat(result1).isNotNull();
        Assertions.assertThat(result2).isNotNull();
        Assertions.assertThat(result1).isEqualTo(variable1.getInitValue());
        Assertions.assertThat(result2).isEqualTo(variable2.getInitValue());
    }

    @Test
    public void testDeleteState()
    {
        // when
        testObject.deleteState();
        // then
        Assertions.assertThat(testObject.getStateWithNulls()).isEmpty();
    }
}
