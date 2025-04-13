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
    public void getType_ThenNullType()
    {
        // when
        NodeType result = testObject.getType();

        // then
        Assertions.assertThat(result).isEqualTo(NodeType.NULL);
    }

    @Test
    public void getLeft_ThenNull()
    {
        // when
        TreeNode result = testObject.getLeft();

        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void getRight_ThenNull()
    {
        // when
        TreeNode result = testObject.getRight();

        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void getLabel_ThenEmpty()
    {
        // when
        String result = testObject.getLabel();

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void getState_ThenVariableValues()
            throws Exception
    {
        // when
        Map<Variable, String> result = testObject.getState();

        // then
        Assertions.assertThat(result)
                  .isNotNull()
                  .containsExactlyEntriesOf(Collections.singletonMap(variable1, "3"));
    }

    @Test
    public void getStateValue_WhenNoValue_ThenUndefinedStateValueException()
    {
        Assertions.assertThatThrownBy(() -> testObject.getStateValue(variable2))
                  .isInstanceOf(UndefinedStateValueException.class);
    }

    @Test
    public void getStateValue_WhenIsValue_ThenVariableValue()
            throws Exception
    {
        // when
        String result = testObject.getStateValue(variable1);

        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("3");
    }

    @Test
    public void getStateValueOrNull_WhenNoValue_ThenNull()
    {
        // when
        String result = testObject.getStateValueOrNull(variable2);

        // then
        Assertions.assertThat(result).isNull();
    }

    @Test
    public void getStateValueOrNull_WhenIsValue_ThenVariableValue()
    {
        // when
        String result = testObject.getStateValueOrNull(variable1);

        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("3");
    }

    @Test
    public void setStateValue_WhenIsValue_ThenNewVariableValue()
            throws Exception
    {
        // when
        testObject.setStateValue(variable2, "Y");

        // then
        Assertions.assertThat(testObject.getStateValueOrNull(variable2)).isNotNull().isEqualTo("Y");
    }

    @Test
    public void setStateValue_WhenIncorrectValue_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, "N"))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void setStateValue_WhenEmptyValue_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, ""))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void setStateValue_WhenNull_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> testObject.setStateValue(variable2, null))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void setInitialState_ThenInitialVariableValues()
    {
        // when
        testObject.setInitialState(Arrays.asList(variable1, variable2));

        // then
        Assertions.assertThat(testObject.getStateValueOrNull(variable1))
                  .isNotNull()
                  .isEqualTo(variable1.getInitValue());
        Assertions.assertThat(testObject.getStateValueOrNull(variable2))
                  .isNotNull()
                  .isEqualTo(variable2.getInitValue());
    }

    @Test
    public void deleteState_ThenEmpty()
    {
        // when
        testObject.deleteState();

        // then
        Assertions.assertThat(testObject.getStateWithNulls()).isEmpty();
    }
}
