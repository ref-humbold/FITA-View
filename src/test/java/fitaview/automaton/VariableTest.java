package fitaview.automaton;

import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VariableTest
{
    private Variable testObject;

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new Variable(1, "A", "B", "C");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void constructor_WhenInitIsNull_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> new Variable(2, null))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void constructor_WhenInitIsEmpty_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> new Variable(2, ""))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void constructor_WhenValueIsNull_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> new Variable(2, "A", "B", "C", null))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void constructor_WhenValueIsEmpty_ThenIllegalVariableValueException()
    {
        Assertions.assertThatThrownBy(() -> new Variable(2, "A", "B", "C", ""))
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void constructor_WhenInitValueIsInValuesList_ThenNoDuplicates()
            throws Exception
    {
        // when
        testObject = new Variable(2, "A", "B", "C", "A");

        // then
        Assertions.assertThat(testObject).containsExactlyInAnyOrder("A", "B", "C");
    }

    @Test
    public void getInitValue_ThenInitialValue()
    {
        // when
        String result = testObject.getInitValue();

        // then
        Assertions.assertThat(result).isNotNull().isEqualTo("A");
    }

    @Test
    public void contains_WhenInnerValue_ThenTrue()
    {
        // when
        boolean resultA = testObject.contains("A");
        boolean resultB = testObject.contains("B");
        boolean resultC = testObject.contains("C");

        // then
        Assertions.assertThat(resultA).isTrue();
        Assertions.assertThat(resultB).isTrue();
        Assertions.assertThat(resultC).isTrue();
    }

    @Test
    public void contains_WhenOuterValue_ThenFalse()
    {
        // when
        boolean resultD = testObject.contains("D");

        // then
        Assertions.assertThat(resultD).isFalse();
    }

    @Test
    public void contains_WhenNull_ThenFalse()
    {
        // when
        boolean result = testObject.contains(null);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void contains_WhenEmpty_ThenFalse()
    {
        // when
        boolean result = testObject.contains("");

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void size_ThenNumberOfPossibleValues()
    {
        // when
        int result = testObject.size();

        // then
        Assertions.assertThat(result).isEqualTo(3);
    }

    @Test
    public void iterator()
    {
        // when
        ArrayList<String> result = new ArrayList<>();

        for(String s : testObject)
            result.add(s);

        // then
        Assertions.assertThat(result).containsExactlyInAnyOrder("A", "B", "C");
    }

    @Test
    public void toString_ThenStringRepresentation()
    {
        // when
        String result = testObject.toString();

        // then
        Assertions.assertThat(result).isEqualTo("Var_1::[A, B, C]");
    }
}
