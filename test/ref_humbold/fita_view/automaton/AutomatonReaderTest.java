package ref_humbold.fita_view.automaton;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import ref_humbold.fita_view.automaton.transition.DuplicatedTransitionException;
import ref_humbold.fita_view.automaton.transition.IllegalTransitionException;

public class AutomatonReaderTest
{
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

    // TopDownDFTA

    @Test
    public void testReadTopDownDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                new File("test/ref_humbold/fita_view/automaton/testReadTopDownDFTA.tda.xml"));
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
            v = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownDFTA expected =
            new TopDownDFTA(Arrays.asList("0", "1"), Collections.singletonList(v));

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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenWildcards.tda.xml"));
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
            v = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownDFTA expected =
            new TopDownDFTA(Arrays.asList("0", "1"), Collections.singletonList(v));

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

    @Test(expected = DuplicatedTransitionException.class)
    public void testReadTopDownDFTAWhenMultipleTransitions()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenMultipleTransitions.tda.xml"));
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
    public void testReadTopDownDFTAWhenIncorrectAlphabetWord()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenIncorrectAlphabetWord.tda.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenIncorrectVariableValue.tda.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchLabel.tda.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchVariableValue.tda.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchVariableId.tda.xml"));
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

    // TopDownNFTA

    @Test
    public void testReadTopDownNFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                new File("test/ref_humbold/fita_view/automaton/testReadTopDownNFTA.tda.xml"));
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
            v = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        TopDownNFTA expected =
            new TopDownNFTA(Arrays.asList("0", "1"), Collections.singletonList(v));

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

    // BottomUpDFTA

    @Test
    public void testReadBottomUpDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                new File("test/ref_humbold/fita_view/automaton/testReadBottomUpDFTA.bua.xml"));
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
            v = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        BottomUpDFTA expected =
            new BottomUpDFTA(Arrays.asList("0", "1"), Collections.singletonList(v));

        expected.addAcceptingState(Collections.singletonMap(v, "C"));

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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenWildcards.bua.xml"));
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
            v = new Variable("A", "B", "C");
        }
        catch(IllegalVariableValueException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        BottomUpDFTA expected =
            new BottomUpDFTA(Arrays.asList("0", "1"), Collections.singletonList(v));

        expected.addAcceptingState(Collections.singletonMap(v, Wildcard.EVERY_VALUE));

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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenDoubleSameWildcard.bua.xml"));
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
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenSameWildcardWithoutEveryWildcard.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenMultipleTransitions.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenIncorrectAlphabetWord.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenIncorrectVariableValue.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchLabel.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchVariableValue.bua.xml"));
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
            testObject = new AutomatonReader(new File(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchVariableId.bua.xml"));
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

    @Ignore
    @Test(expected = FileFormatException.class)
    public void testReadWhenExpectedBottomUpButNamedTopDown()
        throws FileFormatException
    {
        try
        {
            testObject = new AutomatonReader(
                new File("test/ref_humbold/fita_view/automaton/testReadTopDownAutomaton.bua.xml"));
        }
        catch(SAXException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }

    @Ignore
    @Test(expected = FileFormatException.class)
    public void testReadWhenExpectedTopDownButNamedBottomUp()
        throws FileFormatException
    {
        try
        {
            testObject = new AutomatonReader(
                new File("test/ref_humbold/fita_view/automaton/testReadBottomUpAutomaton.tda.xml"));
        }
        catch(SAXException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }
    }
}
