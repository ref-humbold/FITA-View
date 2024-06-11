package fitaview.automaton;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

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
        var path =
                Path.of(DIRECTORY, "read_WhenIncorrectFileExtension_ThenFileFormatException.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(path.toFile()))
                  .isInstanceOf(FileFormatException.class);
    }

    @Test
    public void read_WhenExpectedBottomUpButNamedTopDown_ThenConfusingFileNameException()
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenExpectedBottomUpButNamedTopDown_ThenConfusingFileNameException.bua.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(path.toFile()))
                  .isInstanceOf(TheFreddieMercuryConfusingFileNameException.class);
    }

    @Test
    public void read_WhenExpectedTopDownButNamedBottomUp_ThenConfusingFileNameException()
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenExpectedTopDownButNamedBottomUp_ThenConfusingFileNameException.tda.xml");

        // then
        Assertions.assertThatThrownBy(() -> new AutomatonReader(path.toFile()))
                  .isInstanceOf(TheFreddieMercuryConfusingFileNameException.class);
    }

    // endregion
    // region TopDownDfta

    @Test
    public void read_WhenTopDownDfta_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenTopDownDfta_ThenAutomatonObject.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new TopDownDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));
        expected.addTransition(v, "A", "0", "B", "C");
        expected.addTransition(v, "A", "1", "A", "A");
        expected.addTransition(v, "B", "0", "C", "A");
        expected.addTransition(v, "B", "1", "B", "B");
        expected.addTransition(v, "C", "0", "A", "B");
        expected.addTransition(v, "C", "1", "C", "C");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenTopDownDftaWithWildcards_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path =
                Path.of(DIRECTORY, "read_WhenTopDownDftaWithWildcards_ThenAutomatonObject.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new TopDownDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("A", false)));
        expected.addTransition(v, "A", "0", "B", "C");
        expected.addTransition(v, "A", "1", "B", Wildcard.SAME_VALUE);
        expected.addTransition(v, "B", "0", "C", "A");
        expected.addTransition(v, "C", "0", "A", "B");
        expected.addTransition(v, Wildcard.EVERY_VALUE, "1", Wildcard.SAME_VALUE,
                               Wildcard.SAME_VALUE);

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenTopDownDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchLabel_ThenIllegalAlphabetWordException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithNoSuchLabel_ThenIllegalAlphabetWordException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalAlphabetWordException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchVariableValue_ThenIllegalVariableValueException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithNoSuchVariableValue_ThenIllegalVariableValueException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoSuchVariableId_ThenNoVariableWithIdException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithNoSuchVariableId_ThenNoVariableWithIdException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoVariableWithIdException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithIncorrectVariableValue_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithIncorrectVariableValue_ThenAutomatonParsingException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithEmptyVariableValue_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithEmptyVariableValue_ThenAutomatonParsingException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoAcceptanceForVariableException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedAcceptanceValueException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithAcceptingUnspecified_ThenIncorrectAcceptanceConditionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithAcceptingUnspecified_ThenIncorrectAcceptanceConditionException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IncorrectAcceptanceConditionException.class);
    }

    @Test
    public void read_WhenTopDownDftaWithMultipleTransitions_ThenDuplicatedTransitionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenTopDownDftaWithMultipleTransitions_ThenDuplicatedTransitionException.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedTransitionException.class);
    }

    // endregion
    // region TopDownNfta

    @Test
    public void read_WhenTopDownNfta_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenTopDownNfta_ThenAutomatonObject.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new TopDownNfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));
        expected.addTransition(v, "A", "0", "B", "C");
        expected.addTransition(v, "A", "1", "A", "A");
        expected.addTransition(v, "A", "1", "A", "B");
        expected.addTransition(v, "B", "0", "C", "A");
        expected.addTransition(v, "B", "0", "B", "C");
        expected.addTransition(v, "B", "1", "B", "B");
        expected.addTransition(v, "C", "0", "A", "B");
        expected.addTransition(v, "C", "1", "C", "C");
        expected.addTransition(v, "C", "1", "B", "B");
        expected.addTransition(v, "C", "1", "A", "A");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownNfta.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region TopDownDita

    @Test
    public void read_WhenTopDownDita_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenTopDownDita_ThenAutomatonObject.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new TopDownDita(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addBuchiAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));
        expected.addTransition(v, "A", "0", "B", "C");
        expected.addTransition(v, "A", "1", "A", "A");
        expected.addTransition(v, "B", "0", "C", "A");
        expected.addTransition(v, "B", "1", "B", "B");
        expected.addTransition(v, "C", "0", "A", "B");
        expected.addTransition(v, "C", "1", "C", "C");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownDita.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region TopDownNita

    @Test
    public void read_WhenTopDownNita_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenTopDownNita_ThenAutomatonObject.tda.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new TopDownNita(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addBuchiAcceptanceConditions(Collections.singletonMap(v, Pair.make("A", false)));
        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));
        expected.addTransition(v, "A", "0", "B", "C");
        expected.addTransition(v, "A", "1", "A", "A");
        expected.addTransition(v, "A", "1", "A", "B");
        expected.addTransition(v, "B", "0", "C", "A");
        expected.addTransition(v, "B", "0", "B", "C");
        expected.addTransition(v, "B", "1", "B", "B");
        expected.addTransition(v, "C", "0", "A", "B");
        expected.addTransition(v, "C", "1", "C", "C");
        expected.addTransition(v, "C", "1", "B", "B");
        expected.addTransition(v, "C", "1", "A", "A");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(TopDownNita.class)
                  .isEqualTo(expected);
    }

    // endregion
    // region BottomUpDfta

    @Test
    public void read_WhenBottomUpDfta_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenBottomUpDfta_ThenAutomatonObject.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new BottomUpDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
        expected.addTransition(v, "A", "A", "0", "A");
        expected.addTransition(v, "A", "A", "1", "B");
        expected.addTransition(v, "A", "B", "0", "B");
        expected.addTransition(v, "A", "B", "1", "C");
        expected.addTransition(v, "A", "C", "0", "C");
        expected.addTransition(v, "A", "C", "1", "A");
        expected.addTransition(v, "B", "A", "0", "B");
        expected.addTransition(v, "B", "A", "1", "C");
        expected.addTransition(v, "B", "B", "0", "B");
        expected.addTransition(v, "B", "B", "1", "C");
        expected.addTransition(v, "B", "C", "0", "C");
        expected.addTransition(v, "B", "C", "1", "A");
        expected.addTransition(v, "C", "A", "0", "A");
        expected.addTransition(v, "C", "A", "1", "B");
        expected.addTransition(v, "C", "B", "0", "B");
        expected.addTransition(v, "C", "B", "1", "C");
        expected.addTransition(v, "C", "C", "0", "C");
        expected.addTransition(v, "C", "C", "1", "A");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenBottomUpDftaWithWildcards_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithWildcards_ThenAutomatonObject.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new BottomUpDfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(
                Collections.singletonMap(v, Pair.make(Wildcard.EVERY_VALUE, true)));
        expected.addTransition(v, "A", Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "A");
        expected.addTransition(v, "B", "A", Wildcard.EVERY_VALUE, Wildcard.LEFT_VALUE);
        expected.addTransition(v, "B", "B", "0", "B");
        expected.addTransition(v, "B", "C", Wildcard.EVERY_VALUE, "C");
        expected.addTransition(v, "C", Wildcard.EVERY_VALUE, "0", Wildcard.RIGHT_VALUE);
        expected.addTransition(v, "C", "A", "1", "B");
        expected.addTransition(v, "C", "B", "1", "C");
        expected.addTransition(v, Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE, "1", "C");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpDfta.class)
                  .isEqualTo(expected);
    }

    @Test
    public void read_WhenBottomUpDftaWithDoubledSameWildcard_ThenIllegalTransitionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithDoubledSameWildcard_ThenIllegalTransitionException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithSameWildcardWithoutEveryWildcard_ThenIllegalTransitionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithSameWildcardWithoutEveryWildcard_ThenIllegalTransitionException.bua.xml");
        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalTransitionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithIncorrectAlphabetWord_ThenAutomatonParsingException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchLabel_ThenIllegalAlphabetWordException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithNoSuchLabel_ThenIllegalAlphabetWordException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalAlphabetWordException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithIncorrectVariableValue_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithIncorrectVariableValue_ThenAutomatonParsingException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchVariableValue_ThenIllegalVariableValueException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithNoSuchVariableValue_ThenIllegalVariableValueException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IllegalVariableValueException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoSuchVariableId_ThenNoVariableWithIdException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithNoSuchVariableId_ThenNoVariableWithIdException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoVariableWithIdException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithEmptyVariableValue_ThenAutomatonParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithEmptyVariableValue_ThenAutomatonParsingException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(AutomatonParsingException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithNoAcceptingValueForVariable_ThenNoAcceptanceForVariableException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(NoAcceptanceForVariableException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithDuplicatedAcceptingValueForVariable_ThenDuplicatedAcceptanceValueException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedAcceptanceValueException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithAcceptanceIncludingAndExcluding_ThenIncorrectAcceptanceConditionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithAcceptanceIncludingAndExcluding_ThenIncorrectAcceptanceConditionException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(IncorrectAcceptanceConditionException.class);
    }

    @Test
    public void read_WhenBottomUpDftaWithMultipleTransitions_ThenDuplicatedTransitionException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY,
                           "read_WhenBottomUpDftaWithMultipleTransitions_ThenDuplicatedTransitionException.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(DuplicatedTransitionException.class);
    }

    // endregion
    // region BottomUpNfta

    @Test
    public void read_WhenBottomUpNfta_ThenAutomatonObject()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenBottomUpNfta_ThenAutomatonObject.bua.xml");

        testObject = new AutomatonReader(path.toFile());

        // when
        TreeAutomaton result = testObject.read();

        // then
        var v = new Variable(0, "A", "B", "C");
        var expected = new BottomUpNfta(Collections.singletonList(v), Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
        expected.addTransition(v, "A", "A", "0", "A");
        expected.addTransition(v, "A", "A", "1", "B");
        expected.addTransition(v, "A", "A", "1", "C");
        expected.addTransition(v, "A", "B", "0", "B");
        expected.addTransition(v, "A", "B", "1", "C");
        expected.addTransition(v, "A", "C", "0", "C");
        expected.addTransition(v, "A", "C", "1", "A");
        expected.addTransition(v, "B", "A", "0", "B");
        expected.addTransition(v, "B", "A", "1", "C");
        expected.addTransition(v, "B", "A", "1", "B");
        expected.addTransition(v, "B", "B", "0", "B");
        expected.addTransition(v, "B", "B", "1", "C");
        expected.addTransition(v, "B", "C", "0", "C");
        expected.addTransition(v, "B", "C", "1", "A");
        expected.addTransition(v, "C", "A", "0", "A");
        expected.addTransition(v, "C", "A", "0", "C");
        expected.addTransition(v, "C", "A", "1", "B");
        expected.addTransition(v, "C", "B", "0", "B");
        expected.addTransition(v, "C", "B", "1", "C");
        expected.addTransition(v, "C", "C", "0", "C");
        expected.addTransition(v, "C", "C", "1", "A");

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(BottomUpNfta.class)
                  .isEqualTo(expected);
    }

    // endregion
}
