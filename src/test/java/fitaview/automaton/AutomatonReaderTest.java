package fitaview.automaton;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

import fitaview.TestUtils;
import fitaview.automaton.transition.DuplicatedTransitionException;
import fitaview.automaton.transition.IllegalTransitionException;
import fitaview.utils.Pair;

public class AutomatonReaderTest
{
    private static final String DIRECTORY = "src/test/resources/AutomatonReaderTest/";
    private AutomatonReader testObject;

    @After
    public void tearDown()
    {
        testObject = null;
    }

    // region file naming

    @Test
    public void read_WhenIncorrectFileExtension_ThenFileFormatException()
    {
        // given
        File file =
                new File(DIRECTORY + "read_WhenIncorrectFileExtension_ThenFileFormatException.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(file))
                  .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void read_WhenExpectedBottomUpButNamedTopDown_ThenConfusingFileNameException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenExpectedBottomUpButNamedTopDown_ThenConfusingFileNameException.bua.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(file))
                  .isInstanceOf(TheFreddieMercuryConfusingFileNameException.class);
    }

    @Test
    public void read_WhenExpectedTopDownButNamedBottomUp_ThenConfusingFileNameException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenExpectedTopDownButNamedBottomUp_ThenConfusingFileNameException.tda.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(file))
                  .isInstanceOf(TheFreddieMercuryConfusingFileNameException.class);
    }

    // endregion
    // region TopDownDfta

    @Test
    public void read_WhenTopDownDfta_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenTopDownDfta_ThenAutomatonObject.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then

        TopDownDfta expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new TopDownDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));
            automaton.addTransition(v, "A", "0", "B", "C");
            automaton.addTransition(v, "A", "1", "A", "A");
            automaton.addTransition(v, "B", "0", "C", "A");
            automaton.addTransition(v, "B", "1", "B", "B");
            automaton.addTransition(v, "C", "0", "A", "B");
            automaton.addTransition(v, "C", "1", "C", "C");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenTopDownDftaWithWildcards_ThenAutomatonObject()
    {
        // given
        File file = new File(
                DIRECTORY + "read_WhenTopDownDftaWithWildcards_ThenAutomatonObject.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        TopDownDfta expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new TopDownDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("A", false)));
            automaton.addTransition(v, "A", "0", "B", "C");
            automaton.addTransition(v, "A", "1", "B", Wildcard.SAME_VALUE);
            automaton.addTransition(v, "B", "0", "C", "A");
            automaton.addTransition(v, "C", "0", "A", "B");
            automaton.addTransition(v, Wildcard.EVERY_VALUE, "1", Wildcard.SAME_VALUE,
                                    Wildcard.SAME_VALUE);
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenTopDownDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchLabel_ThenIllegalAlphabetWordException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithNoSuchLabel_ThenIllegalAlphabetWordException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalAlphabetWordException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchVariableValue_ThenIllegalVariableValueException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithNoSuchVariableValue_ThenIllegalVariableValueException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchVariableId_ThenNoVariableWithIdException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithNoSuchVariableId_ThenNoVariableWithIdException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoVariableWithIdException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithIncorrectVariableValue_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithIncorrectVariableValue_ThenAutomatonParsingException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithEmptyVariableValue_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithEmptyVariableValue_ThenAutomatonParsingException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoAcceptanceForVariableException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedAcceptanceValueException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithAcceptingUnspecified_ThenIncorrectAcceptanceConditionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithAcceptingUnspecified_ThenIncorrectAcceptanceConditionException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IncorrectAcceptanceConditionException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithMultipleTransitions_ThenDuplicatedTransitionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenTopDownDftaWithMultipleTransitions_ThenDuplicatedTransitionException.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedTransitionException.class);
    }

    // endregion
    // region TopDownNfta

    @Test
    public void read_WhenTopDownNfta_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenTopDownNfta_ThenAutomatonObject.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        TopDownNfta expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new TopDownNfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));
            automaton.addTransition(v, "A", "0", "B", "C");
            automaton.addTransition(v, "A", "1", "A", "A");
            automaton.addTransition(v, "A", "1", "A", "B");
            automaton.addTransition(v, "B", "0", "C", "A");
            automaton.addTransition(v, "B", "0", "B", "C");
            automaton.addTransition(v, "B", "1", "B", "B");
            automaton.addTransition(v, "C", "0", "A", "B");
            automaton.addTransition(v, "C", "1", "C", "C");
            automaton.addTransition(v, "C", "1", "B", "B");
            automaton.addTransition(v, "C", "1", "A", "A");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownNfta.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region TopDownDita

    @Test
    public void read_WhenTopDownDita_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenTopDownDita_ThenAutomatonObject.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        TopDownDita expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new TopDownDita(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addBuchiAcceptanceConditions(
                    Collections.singletonMap(v, Pair.make("C", true)));
            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));
            automaton.addTransition(v, "A", "0", "B", "C");
            automaton.addTransition(v, "A", "1", "A", "A");
            automaton.addTransition(v, "B", "0", "C", "A");
            automaton.addTransition(v, "B", "1", "B", "B");
            automaton.addTransition(v, "C", "0", "A", "B");
            automaton.addTransition(v, "C", "1", "C", "C");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDita.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region TopDownNita

    @Test
    public void read_WhenTopDownNita_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenTopDownNita_ThenAutomatonObject.tda.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        TopDownNita expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new TopDownNita(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addBuchiAcceptanceConditions(
                    Collections.singletonMap(v, Pair.make("A", false)));
            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));
            automaton.addTransition(v, "A", "0", "B", "C");
            automaton.addTransition(v, "A", "1", "A", "A");
            automaton.addTransition(v, "A", "1", "A", "B");
            automaton.addTransition(v, "B", "0", "C", "A");
            automaton.addTransition(v, "B", "0", "B", "C");
            automaton.addTransition(v, "B", "1", "B", "B");
            automaton.addTransition(v, "C", "0", "A", "B");
            automaton.addTransition(v, "C", "1", "C", "C");
            automaton.addTransition(v, "C", "1", "B", "B");
            automaton.addTransition(v, "C", "1", "A", "A");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownNita.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region BottomUpDfta

    @Test
    public void read_WhenBottomUpDfta_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenBottomUpDfta_ThenAutomatonObject.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        BottomUpDfta expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new BottomUpDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
            automaton.addTransition(v, "A", "A", "0", "A");
            automaton.addTransition(v, "A", "A", "1", "B");
            automaton.addTransition(v, "A", "B", "0", "B");
            automaton.addTransition(v, "A", "B", "1", "C");
            automaton.addTransition(v, "A", "C", "0", "C");
            automaton.addTransition(v, "A", "C", "1", "A");
            automaton.addTransition(v, "B", "A", "0", "B");
            automaton.addTransition(v, "B", "A", "1", "C");
            automaton.addTransition(v, "B", "B", "0", "B");
            automaton.addTransition(v, "B", "B", "1", "C");
            automaton.addTransition(v, "B", "C", "0", "C");
            automaton.addTransition(v, "B", "C", "1", "A");
            automaton.addTransition(v, "C", "A", "0", "A");
            automaton.addTransition(v, "C", "A", "1", "B");
            automaton.addTransition(v, "C", "B", "0", "B");
            automaton.addTransition(v, "C", "B", "1", "C");
            automaton.addTransition(v, "C", "C", "0", "C");
            automaton.addTransition(v, "C", "C", "1", "A");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenBottomUpDftaWithWildcards_ThenAutomatonObject()
    {
        // given
        File file = new File(
                DIRECTORY + "read_WhenBottomUpDftaWithWildcards_ThenAutomatonObject.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        BottomUpDfta expected = TestUtils.failOnException(() -> {
            Variable v = new Variable(0, "A", "B", "C");
            BottomUpDfta automaton =
                    new BottomUpDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(
                    Collections.singletonMap(v, Pair.make(Wildcard.EVERY_VALUE, true)));
            automaton.addTransition(v, "A", Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "A");
            automaton.addTransition(v, "B", "A", Wildcard.EVERY_VALUE, Wildcard.LEFT_VALUE);
            automaton.addTransition(v, "B", "B", "0", "B");
            automaton.addTransition(v, "B", "C", Wildcard.EVERY_VALUE, "C");
            automaton.addTransition(v, "C", Wildcard.EVERY_VALUE, "0", Wildcard.RIGHT_VALUE);
            automaton.addTransition(v, "C", "A", "1", "B");
            automaton.addTransition(v, "C", "B", "1", "C");
            automaton.addTransition(v, Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE, "1", "C");
            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenBottomUpDftaWithDoubledSameWildcard_ThenIllegalTransitionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithDoubledSameWildcard_ThenIllegalTransitionException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithSameWildcardWithoutEveryWildcard_ThenIllegalTransitionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithSameWildcardWithoutEveryWildcard_ThenIllegalTransitionException.bua.xml");
        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchLabel_ThenIllegalAlphabetWordException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithNoSuchLabel_ThenIllegalAlphabetWordException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalAlphabetWordException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithIncorrectVariableValue_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithIncorrectVariableValue_ThenAutomatonParsingException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchVariableValue_ThenIllegalVariableValueException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithNoSuchVariableValue_ThenIllegalVariableValueException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchVariableId_ThenNoVariableWithIdException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithNoSuchVariableId_ThenNoVariableWithIdException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoVariableWithIdException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithEmptyVariableValue_ThenAutomatonParsingException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithEmptyVariableValue_ThenAutomatonParsingException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoAcceptanceForVariableException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedAcceptanceValueException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithAcceptanceIncludingAndExcluding_ThenIncorrectAcceptanceConditionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithAcceptanceIncludingAndExcluding_ThenIncorrectAcceptanceConditionException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IncorrectAcceptanceConditionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithMultipleTransitions_ThenDuplicatedTransitionException()
    {
        // given
        File file = new File(DIRECTORY
                                     + "read_WhenBottomUpDftaWithMultipleTransitions_ThenDuplicatedTransitionException.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedTransitionException.class);
    }

    // endregion
    // region BottomUpNfta

    @Test
    public void read_WhenBottomUpNfta_ThenAutomatonObject()
    {
        // given
        File file = new File(DIRECTORY + "read_WhenBottomUpNfta_ThenAutomatonObject.bua.xml");

        testObject = TestUtils.failOnException(() -> new AutomatonReader(file));

        // when
        TreeAutomaton result = TestUtils.failOnException(() -> testObject.read());

        // then
        BottomUpNfta expected = TestUtils.failOnException(() -> {
            var v = new Variable(0, "A", "B", "C");
            var automaton = new BottomUpNfta(Collections.singletonList(v), Arrays.asList("0", "1"));

            automaton.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
            automaton.addTransition(v, "A", "A", "0", "A");
            automaton.addTransition(v, "A", "A", "1", "B");
            automaton.addTransition(v, "A", "A", "1", "C");
            automaton.addTransition(v, "A", "B", "0", "B");
            automaton.addTransition(v, "A", "B", "1", "C");
            automaton.addTransition(v, "A", "C", "0", "C");
            automaton.addTransition(v, "A", "C", "1", "A");
            automaton.addTransition(v, "B", "A", "0", "B");
            automaton.addTransition(v, "B", "A", "1", "C");
            automaton.addTransition(v, "B", "A", "1", "B");
            automaton.addTransition(v, "B", "B", "0", "B");
            automaton.addTransition(v, "B", "B", "1", "C");
            automaton.addTransition(v, "B", "C", "0", "C");
            automaton.addTransition(v, "B", "C", "1", "A");
            automaton.addTransition(v, "C", "A", "0", "A");
            automaton.addTransition(v, "C", "A", "0", "C");
            automaton.addTransition(v, "C", "A", "1", "B");
            automaton.addTransition(v, "C", "B", "0", "B");
            automaton.addTransition(v, "C", "B", "1", "C");
            automaton.addTransition(v, "C", "C", "0", "C");
            automaton.addTransition(v, "C", "C", "1", "A");

            return automaton;
        });

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpNfta.class)
                  .isEqualTo(expected);
    }

    // endregion
}
