package fitaview.automaton;

import java.util.*;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitaview.utils.Pair;

public class BottomUpNftaTest
{
    private BottomUpNfta testObject;
    private final List<Variable> variables =
            Arrays.asList(new Variable(1, "A", "B"), new Variable(2, "X", "Y"));
    private final List<String> alphabet = Arrays.asList("0", "1");

    public BottomUpNftaTest()
            throws Exception
    {
    }

    @Before
    public void setUp()
            throws Exception
    {
        Map<Variable, Pair<String, Boolean>> accept = new HashMap<>();

        accept.put(variables.get(0), Pair.make("B", true));
        accept.put(variables.get(1), Pair.make("Y", true));

        testObject = new BottomUpNfta(variables, alphabet);
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE,
                                 Wildcard.EVERY_VALUE, "A");
        testObject.addTransition(variables.get(0), "A", "A", "1", "B");
        testObject.addTransition(variables.get(0), "A", "B", "0", "A");
        testObject.addTransition(variables.get(0), "A", "B", "1", "B");
        testObject.addTransition(variables.get(0), "B", "A", "0", "B");
        testObject.addTransition(variables.get(0), "B", "A", "1", "A");
        testObject.addTransition(variables.get(0), "B", "B", "0", "B");
        testObject.addTransition(variables.get(0), "B", "B", "1", "A");
        testObject.addTransition(variables.get(1), "X", Wildcard.EVERY_VALUE, "0",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(1), "X", "X", "1", "Y");
        testObject.addTransition(variables.get(1), "X", "Y", "1", "X");
        testObject.addTransition(variables.get(1), "X", "Y", "0", "X");
        testObject.addTransition(variables.get(1), "Y", "X", "0", "X");
        testObject.addTransition(variables.get(1), "Y", "X", "1", "Y");
        testObject.addTransition(variables.get(1), "Y", "Y", "0", "Y");
        testObject.addTransition(variables.get(1), "Y", "Y", "1", "X");
        testObject.addTransition(variables.get(1), "Y", "Y", "1", "Y");
        testObject.addAcceptanceConditions(accept);
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void getTypeName_ThenFullName()
    {
        // when
        String result = testObject.getTypeName();

        // then
        Assertions.assertThat(result)
                  .isEqualTo("Bottom-up non-deterministic finite tree automaton");
    }

    @Test
    public void getNextStates_WhenZero_ThenAllStatesForWord()
    {
        // given
        Map<Variable, String> leftState = new HashMap<>();
        Map<Variable, String> rightState = new HashMap<>();

        leftState.put(variables.get(0), "B");
        leftState.put(variables.get(1), "X");
        rightState.put(variables.get(0), "B");
        rightState.put(variables.get(1), "Y");

        // when
        Set<Map<Variable, String>> result = testObject.getNextStates(leftState, rightState, "0");

        // then
        Map<Variable, String> state1 = new HashMap<>();
        Map<Variable, String> state2 = new HashMap<>();
        Map<Variable, String> state3 = new HashMap<>();
        Map<Variable, String> state4 = new HashMap<>();
        Set<Map<Variable, String>> expected = new HashSet<>();

        state1.put(variables.get(0), "A");
        state1.put(variables.get(1), "Y");
        state2.put(variables.get(0), "B");
        state2.put(variables.get(1), "Y");
        state3.put(variables.get(0), "A");
        state3.put(variables.get(1), "X");
        state4.put(variables.get(0), "B");
        state4.put(variables.get(1), "X");
        expected.add(state1);
        expected.add(state2);
        expected.add(state3);
        expected.add(state4);

        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void getNextStates_WhenOne_ThenAllStatesForWord()
    {
        // given
        Map<Variable, String> leftState = new HashMap<>();
        Map<Variable, String> rightState = new HashMap<>();

        leftState.put(variables.get(0), "A");
        leftState.put(variables.get(1), "Y");
        rightState.put(variables.get(0), "A");
        rightState.put(variables.get(1), "X");

        // when
        Set<Map<Variable, String>> result = testObject.getNextStates(leftState, rightState, "1");

        // then
        Map<Variable, String> state1 = new HashMap<>();
        Map<Variable, String> state2 = new HashMap<>();
        Set<Map<Variable, String>> expected = new HashSet<>();

        state1.put(variables.get(0), "A");
        state1.put(variables.get(1), "Y");
        state2.put(variables.get(0), "B");
        state2.put(variables.get(1), "Y");
        expected.add(state1);
        expected.add(state2);

        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void checkEmptiness_ThenFalse()
            throws Exception
    {
        // when
        boolean result = testObject.checkEmptiness();

        // then
        Assertions.assertThat(result).isFalse();
    }
}
