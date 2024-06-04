package fitaview.tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitaview.TestUtils;
import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.Variable;

public class RecNodeTest
{
    private RecNode testObject;
    private final Variable variable1 = new Variable(1, "0", "1", "2", "3");
    private final Variable variable2 = new Variable(2, "X", "Y", "Z");

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

    @Test
    public void constructor_WhenNullRecursive()
    {
        Assertions.assertThatThrownBy(() -> new RecNode(null, 0))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testToString()
    {
        // when
        String result = testObject.toString();
        // then
        Assertions.assertThat(result).isEqualTo("<@ REC @>");
    }

    @Test
    public void testGetLabel()
    {
        // when
        String result = testObject.getLabel();
        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("LABEL");
    }

    @Test
    public void testGetState()
    {
        // when
        Map<Variable, String> result = TestUtils.failOnException(() -> testObject.getState());
        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .isEqualTo(Collections.singletonMap(variable1, "3"));
    }

    @Test
    public void getStateValue_WhenNoValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.getStateValue(variable2))
                  .isInstanceOf(UndefinedStateValueException.class);
    }

    @Test
    public void getStateValue_WhenIsValue()
    {
        // when
        String result = TestUtils.failOnException(() -> testObject.getStateValue(variable1));
        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("3");
    }

    @Test
    public void getStateValueOrNull_WhenNoValue()
    {
        // when
        String result = testObject.getStateValueOrNull(variable2);
        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void getStateValueOrNull_WhenIsValue()
    {
        // when
        String result = testObject.getStateValueOrNull(variable1);
        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("3");
    }

    @Test
    public void setStateValue_WhenIsValue()
    {
        // given
        TestUtils.failOnException(() -> testObject.setStateValue(variable2, "Y"));
        // when
        String result = testObject.getStateValueOrNull(variable2);
        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("Y");
    }

    @Test
    public void setStateValue_WhenIncorrectValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, "N"))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void setStateValue_WhenEmptyValue()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, ""))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void setStateValue_WhenNull()
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
        Assertions.assertThat(result1).isNotNull().isEqualTo(variable1.getInitValue());
        Assertions.assertThat(result2).isNotNull().isEqualTo(variable2.getInitValue());
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
