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

public class StandardNodeTest
{
    private StandardNode testObject;
    private final Variable variable1 = new Variable(1, "0", "1", "2", "3");
    private final Variable variable2 = new Variable(2, "X", "Y", "Z");

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

    @Test
    public void constructor_WhenNullLabel_ThenIllegalArgumentException()
    {
        Assertions.assertThatThrownBy(() -> new StandardNode(null, 0))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructor_WhenEmptyLabel_ThenIllegalArgumentException()
    {
        Assertions.assertThatThrownBy(() -> new StandardNode("", 0))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void toString_ThenStringRepresentation()
            throws Exception
    {
        // given
        testObject.setLeft(new StandardNode("left", 3));
        testObject.setRight(new StandardNode("right", 2));

        // when
        String result = testObject.toString();

        // then
        Assertions.assertThat(result)
                  .isEqualTo("<$ 'LABEL', <$ 'left', #, # $>, <$ 'right', #, # $> $>");
    }

    @Test
    public void getType_ThenNodeType()
    {
        // when
        NodeType result = testObject.getType();

        // then
        Assertions.assertThat(result).isEqualTo(NodeType.NODE);
    }

    @Test
    public void setLeft_WhenRoot_ThenParent()
            throws Exception
    {
        // given
        StandardNode node = new StandardNode("A", 1);

        // when
        testObject.setLeft(node);

        // then
        Assertions.assertThat(testObject.getLeft()).isSameAs(node);
        Assertions.assertThat(node.getParent()).isSameAs(testObject);
    }

    @Test
    public void setLeft_WhenChange_ThenParentOfNewNode()
            throws Exception
    {
        // given
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);

        testObject.setLeft(node1);

        // when
        testObject.setLeft(node2);

        // then
        Assertions.assertThat(node1.getParent()).isNull();
        Assertions.assertThat(testObject.getLeft()).isSameAs(node2);
        Assertions.assertThat(node2.getParent()).isSameAs(testObject);
    }

    @Test
    public void setLeft_WhenHasParent_ThenNodeHasParentException()
            throws Exception
    {
        // given
        TreeNode leftNode = new StandardNode("A", 1, new StandardNode("B", 2),
                                             new StandardNode("C", 3)).getLeft();

        // then
        Assertions.assertThatThrownBy(() -> testObject.setLeft(leftNode))
                  .isInstanceOf(NodeHasParentException.class);
    }

    @Test
    public void setRight_WhenRoot_ThenParent()
            throws Exception
    {
        // given
        StandardNode node = new StandardNode("A", 1);

        // when
        testObject.setRight(node);

        // then
        Assertions.assertThat(testObject.getRight()).isSameAs(node);
        Assertions.assertThat(node.getParent()).isSameAs(testObject);
    }

    @Test
    public void setRight_WhenChange_ThenParentOfNewNode()
            throws Exception
    {
        // given
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);

        testObject.setRight(node1);

        // when
        testObject.setRight(node2);

        // then
        Assertions.assertThat(node1.getParent()).isNull();
        Assertions.assertThat(testObject.getRight()).isSameAs(node2);
        Assertions.assertThat(node2.getParent()).isSameAs(testObject);
    }

    @Test
    public void setRight_WhenHasParent_ThenNodeHasParentException()
            throws Exception
    {
        // given
        TreeNode leftNode = new StandardNode("A", 1, new StandardNode("B", 2),
                                             new StandardNode("C", 3)).getLeft();

        // then
        Assertions.assertThatThrownBy(() -> testObject.setRight(leftNode))
                  .isInstanceOf(NodeHasParentException.class);
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
        String result1 = testObject.getStateValueOrNull(variable1);
        String result2 = testObject.getStateValueOrNull(variable2);

        Assertions.assertThat(result1).isNotNull().isEqualTo(variable1.getInitValue());
        Assertions.assertThat(result2).isNotNull().isEqualTo(variable2.getInitValue());
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
