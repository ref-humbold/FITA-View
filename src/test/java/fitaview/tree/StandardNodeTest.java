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
    public void constructor_WhenNullLabel()
    {
        Assertions.assertThatThrownBy(() -> new StandardNode(null, 0))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void constructor_WhenEmptyLabel()
    {
        Assertions.assertThatThrownBy(() -> new StandardNode("", 0))
                  .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testToString()
    {
        // given
        TestUtils.failOnException(() -> {
            testObject.setLeft(new StandardNode("left", 3));
            testObject.setRight(new StandardNode("right", 2));
        });
        // when
        String result = testObject.toString();
        // then
        Assertions.assertThat(result)
                  .isEqualTo("<$ 'LABEL', <$ 'left', #, # $>, <$ 'right', #, # $> $>");
    }

    @Test
    public void setLeft_WhenRoot()
    {
        // given
        StandardNode node = new StandardNode("A", 1);
        // when
        TestUtils.failOnException(() -> testObject.setLeft(node));
        // then
        Assertions.assertThat(testObject.getLeft()).isSameAs(node);
        Assertions.assertThat(node.getParent()).isSameAs(testObject);
    }

    @Test
    public void setLeft_WhenChange()
    {
        // given
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);
        // when
        TestUtils.failOnException(() -> {
            testObject.setLeft(node1);
            testObject.setLeft(node2);
        });
        // then
        Assertions.assertThat(node1.getParent()).isNull();
        Assertions.assertThat(testObject.getLeft()).isSameAs(node2);
        Assertions.assertThat(node2.getParent()).isSameAs(testObject);
    }

    @Test
    public void setLeft_WhenHasParent()
    {
        // given
        TreeNode leftNode = TestUtils.failOnException(
                () -> new StandardNode("A", 1, new StandardNode("B", 2),
                                       new StandardNode("C", 3)).getLeft());
        // then
        Assertions.assertThatThrownBy(() -> testObject.setLeft(leftNode))
                  .isInstanceOf(NodeHasParentException.class);
    }

    @Test
    public void setRight_WhenRoot()
    {
        // given
        StandardNode node = new StandardNode("A", 1);
        // when
        TestUtils.failOnException(() -> testObject.setRight(node));
        // then
        Assertions.assertThat(testObject.getRight()).isSameAs(node);
        Assertions.assertThat(node.getParent()).isSameAs(testObject);
    }

    @Test
    public void setRight_WhenChange()
    {
        // given
        StandardNode node1 = new StandardNode("A", 1);
        StandardNode node2 = new StandardNode("B", 2);
        // when
        TestUtils.failOnException(() -> {
            testObject.setRight(node1);
            testObject.setRight(node2);
        });
        // then
        Assertions.assertThat(node1.getParent()).isNull();
        Assertions.assertThat(testObject.getRight()).isSameAs(node2);
        Assertions.assertThat(node2.getParent()).isSameAs(testObject);
    }

    @Test
    public void setRight_WhenHasParent()
    {
        // given
        TreeNode leftNode = TestUtils.failOnException(
                () -> new StandardNode("A", 1, new StandardNode("B", 2),
                                       new StandardNode("C", 3)).getLeft());
        // then
        Assertions.assertThatThrownBy(() -> testObject.setRight(leftNode))
                  .isInstanceOf(NodeHasParentException.class);
    }

    @Test
    public void testGetState()
    {
        // when
        Map<Variable, String> result = TestUtils.failOnException(() -> testObject.getState());
        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo(Collections.singletonMap(variable1, "3"));
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("3");
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
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("3");
    }

    @Test
    public void setStateValue_WhenIsValue()
    {
        // when
        TestUtils.failOnException(() -> testObject.setStateValue(variable2, "Y"));
        // then
        String result = testObject.getStateValueOrNull(variable2);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEqualTo("Y");
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
        // when
        testObject.setInitialState(Arrays.asList(variable1, variable2));
        // then
        String result1 = testObject.getStateValueOrNull(variable1);
        String result2 = testObject.getStateValueOrNull(variable2);

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
