package refhumbold.fitaview.automaton;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.automaton.transition.DuplicatedTransitionException;
import refhumbold.fitaview.automaton.transition.IllegalTransitionException;

public class AutomatonReaderTest
{
    private static final String DIRECTORY = "test_files/AutomatonReaderTest/";
    private AutomatonReader testObject;

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    // region files

    @Test(expected = FileFormatException.class)
    public void testReadWhenIncorrectFileExtension()
        throws SAXException
    {
        testObject = new AutomatonReader(
            new File(DIRECTORY + "testReadWhenIncorrectFileExtension.xml"));
    }

    @Test(expected = TheFreddyMercuryConfusingFileNameException.class)
    public void testReadWhenExpectedBottomUpButNamedTopDown()
        throws TheFreddyMercuryConfusingFileNameException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownAutomaton.bua.xml"));
        }
        catch(SAXException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = TheFreddyMercuryConfusingFileNameException.class)
    public void testReadWhenExpectedTopDownButNamedBottomUp()
        throws TheFreddyMercuryConfusingFileNameException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpAutomaton.tda.xml"));
        }
        catch(SAXException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    // endregion
    // region TopDownDFTA

    @Test
    public void testReadTopDownDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadTopDownDFTA.tda.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownDFTA expected = new TopDownDFTA(Collections.singletonList(v),
                                               Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));

        try
        {
            expected.addTransition(v, "A", "0", "B", "C");
            expected.addTransition(v, "A", "1", "A", "A");
            expected.addTransition(v, "B", "0", "C", "A");
            expected.addTransition(v, "B", "1", "B", "B");
            expected.addTransition(v, "C", "0", "A", "B");
            expected.addTransition(v, "C", "1", "C", "C");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadTopDownDFTAWhenWildcards()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenWildcards.tda.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownDFTA expected = new TopDownDFTA(Collections.singletonList(v),
                                               Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("A", false)));

        try
        {
            expected.addTransition(v, "A", "0", "B", "C");
            expected.addTransition(v, "A", "1", "B", Wildcard.SAME_VALUE);
            expected.addTransition(v, "B", "0", "C", "A");
            expected.addTransition(v, "C", "0", "A", "B");
            expected.addTransition(v, Wildcard.EVERY_VALUE, "1", Wildcard.SAME_VALUE,
                                   Wildcard.SAME_VALUE);
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadTopDownDFTAWhenIncorrectAlphabetWord()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenIncorrectAlphabetWord.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalAlphabetWordException.class)
    public void testReadTopDownDFTAWhenNoSuchLabel()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenNoSuchLabel.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testReadTopDownDFTAWhenNoSuchVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenNoSuchVariableValue.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = NoVariableWithIDException.class)
    public void testReadTopDownDFTAWhenNoSuchVariableId()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenNoSuchVariableId.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadTopDownDFTAWhenIncorrectVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenIncorrectVariableValue.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadTopDownDFTAWhenVariableValueIsEmpty()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenVariableValueIsEmpty.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = NoAcceptanceForVariableException.class)
    public void testReadTopDownDFTAWhenNoAcceptingValueForVariable()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenNoAcceptingValueForVariable.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = DuplicatedAcceptanceValueException.class)
    public void testReadTopDownDFTAWhenDuplicatedAcceptingValueForVariable()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                DIRECTORY + "testReadTopDownDFTAWhenDuplicatedAcceptingValueForVariable.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IncorrectAcceptanceConditionException.class)
    public void testReadTopDownDFTAWhenAcceptingUnspecified()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenAcceptingUnspecified.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = DuplicatedTransitionException.class)
    public void testReadTopDownDFTAWhenMultipleTransitions()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadTopDownDFTAWhenMultipleTransitions.tda.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    // endregion
    // region TopDownNFTA

    @Test
    public void testReadTopDownNFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadTopDownNFTA.tda.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownNFTA expected = new TopDownNFTA(Collections.singletonList(v),
                                               Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));

        try
        {
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
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownNFTA);
        Assert.assertEquals(expected, result);
    }

    // endregion
    // region TopDownDITA

    @Test
    public void testReadTopDownDITA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadTopDownDITA.tda.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownDITA expected = new TopDownDITA(Collections.singletonList(v),
                                               Arrays.asList("0", "1"));

        expected.addBuchiAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));
        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", false)));

        try
        {
            expected.addTransition(v, "A", "0", "B", "C");
            expected.addTransition(v, "A", "1", "A", "A");
            expected.addTransition(v, "B", "0", "C", "A");
            expected.addTransition(v, "B", "1", "B", "B");
            expected.addTransition(v, "C", "0", "A", "B");
            expected.addTransition(v, "C", "1", "C", "C");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDITA);
        Assert.assertEquals(expected, result);
    }

    // endregion
    // region TopDownNITA

    @Test
    public void testReadTopDownNITA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadTopDownNITA.tda.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownNITA expected = new TopDownNITA(Collections.singletonList(v),
                                               Arrays.asList("0", "1"));

        expected.addBuchiAcceptanceConditions(Collections.singletonMap(v, Pair.make("A", false)));
        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("B", true)));

        try
        {
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
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownNITA);
        Assert.assertEquals(expected, result);
    }

    // endregion
    // region BottomUpDFTA

    @Test
    public void testReadBottomUpDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadBottomUpDFTA.bua.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        BottomUpDFTA expected = new BottomUpDFTA(Collections.singletonList(v),
                                                 Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));

        try
        {
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
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadBottomUpDFTAWhenWildcards()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenWildcards.bua.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        BottomUpDFTA expected = new BottomUpDFTA(Collections.singletonList(v),
                                                 Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(
            Collections.singletonMap(v, Pair.make(Wildcard.EVERY_VALUE, true)));

        try
        {
            expected.addTransition(v, "A", Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "A");
            expected.addTransition(v, "B", "A", Wildcard.EVERY_VALUE, Wildcard.LEFT_VALUE);
            expected.addTransition(v, "B", "B", "0", "B");
            expected.addTransition(v, "B", "C", Wildcard.EVERY_VALUE, "C");
            expected.addTransition(v, "C", Wildcard.EVERY_VALUE, "0", Wildcard.RIGHT_VALUE);
            expected.addTransition(v, "C", "A", "1", "B");
            expected.addTransition(v, "C", "B", "1", "C");
            expected.addTransition(v, Wildcard.EVERY_VALUE, Wildcard.SAME_VALUE, "1", "C");
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalTransitionException.class)
    public void testReadBottomUpDFTAWhenDoubleSameWildcard()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenDoubleSameWildcard.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalTransitionException.class)
    public void testReadBottomUpDFTAWhenSameWildcardWithoutEveryWildcard()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                DIRECTORY + "testReadBottomUpDFTAWhenSameWildcardWithoutEveryWildcard.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadBottomUpDFTAWhenIncorrectAlphabetWord()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenIncorrectAlphabetWord.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalAlphabetWordException.class)
    public void testReadBottomUpDFTAWhenNoSuchLabel()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenNoSuchLabel.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadBottomUpDFTAWhenIncorrectVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenIncorrectVariableValue.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IllegalVariableValueException.class)
    public void testReadBottomUpDFTAWhenNoSuchVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenNoSuchVariableValue.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = NoVariableWithIDException.class)
    public void testReadBottomUpDFTAWhenNoSuchVariableId()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenNoSuchVariableId.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadBottomUpDFTAWhenVariableValueIsEmpty()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenVariableValueIsEmpty.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = NoAcceptanceForVariableException.class)
    public void testReadBottomUpDFTAWhenNoAcceptingValueForVariable()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                DIRECTORY + "testReadBottomUpDFTAWhenNoAcceptingValueForVariable.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = DuplicatedAcceptanceValueException.class)
    public void testReadBottomUpDFTAWhenDuplicatedAcceptingValueForVariable()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                DIRECTORY + "testReadBottomUpDFTAWhenDuplicatedAcceptingValueForVariable.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = IncorrectAcceptanceConditionException.class)
    public void testReadBottomUpDFTAWhenAcceptingIncludesAndExcludes()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                DIRECTORY + "testReadBottomUpDFTAWhenAcceptingIncludesAndExcludes.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Test(expected = DuplicatedTransitionException.class)
    public void testReadBottomUpDFTAWhenMultipleTransitions()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                new File(DIRECTORY + "testReadBottomUpDFTAWhenMultipleTransitions.bua.xml"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        try
        {
            testObject.read();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    // endregion
    // region BottomUpNFTA

    @Test
    public void testReadBottomUpNFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(new File(DIRECTORY + "testReadBottomUpNFTA.bua.xml"));
            result = testObject.read();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Variable v = null;

        try
        {
            v = new Variable(0, "A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        BottomUpNFTA expected = new BottomUpNFTA(Collections.singletonList(v),
                                                 Arrays.asList("0", "1"));

        expected.addAcceptanceConditions(Collections.singletonMap(v, Pair.make("C", true)));

        try
        {
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
        }
        catch(DuplicatedTransitionException | IllegalTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpNFTA);
        Assert.assertEquals(expected, result);
    }

    // endregion
}
