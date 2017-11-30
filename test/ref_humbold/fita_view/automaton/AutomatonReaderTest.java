package ref_humbold.fita_view.automaton;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

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

    @Test
    public void testReadTopDownDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTA.tda.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
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
        catch(DuplicatedTransitionException e)
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
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenWildcards.tda.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
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
            expected.addTransition(v, "A", "1", "B", "(=)");
            expected.addTransition(v, "B", "0", "C", "A");
            expected.addTransition(v, "C", "0", "A", "B");
            expected.addTransition(v, "(*)", "1", "(=)", "(=)");
        }
        catch(DuplicatedTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadTopDownNFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownNFTA.tda.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
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

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TopDownNFTA);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReadBottomUpDFTA()
    {
        TreeAutomaton result = null;

        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTA.bua.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
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

        try
        {
            expected.addTransition(v, "A", "0", "A", "0", "A");
            expected.addTransition(v, "A", "0", "A", "1", "B");
            expected.addTransition(v, "A", "0", "B", "0", "B");
            expected.addTransition(v, "A", "0", "B", "1", "C");
            expected.addTransition(v, "A", "0", "C", "0", "C");
            expected.addTransition(v, "A", "0", "C", "1", "A");
            expected.addTransition(v, "A", "1", "A", "0", "B");
            expected.addTransition(v, "A", "1", "A", "1", "C");
            expected.addTransition(v, "A", "1", "B", "0", "B");
            expected.addTransition(v, "A", "1", "B", "1", "A");
            expected.addTransition(v, "A", "1", "C", "0", "B");
            expected.addTransition(v, "A", "1", "C", "1", "C");
            expected.addTransition(v, "B", "0", "A", "0", "A");
            expected.addTransition(v, "B", "0", "A", "1", "B");
            expected.addTransition(v, "B", "0", "B", "0", "B");
            expected.addTransition(v, "B", "0", "B", "1", "C");
            expected.addTransition(v, "B", "0", "C", "0", "C");
            expected.addTransition(v, "B", "0", "C", "1", "A");
            expected.addTransition(v, "B", "1", "A", "0", "A");
            expected.addTransition(v, "B", "1", "A", "1", "C");
            expected.addTransition(v, "B", "1", "B", "0", "B");
            expected.addTransition(v, "B", "1", "B", "1", "C");
            expected.addTransition(v, "B", "1", "C", "0", "A");
            expected.addTransition(v, "B", "1", "C", "1", "B");
            expected.addTransition(v, "C", "0", "A", "0", "C");
            expected.addTransition(v, "C", "0", "A", "1", "B");
            expected.addTransition(v, "C", "0", "B", "0", "B");
            expected.addTransition(v, "C", "0", "B", "1", "C");
            expected.addTransition(v, "C", "0", "C", "0", "C");
            expected.addTransition(v, "C", "0", "C", "1", "A");
            expected.addTransition(v, "C", "1", "A", "0", "A");
            expected.addTransition(v, "C", "1", "A", "1", "B");
            expected.addTransition(v, "C", "1", "B", "0", "B");
            expected.addTransition(v, "C", "1", "B", "1", "C");
            expected.addTransition(v, "C", "1", "C", "0", "C");
            expected.addTransition(v, "C", "1", "C", "1", "A");
        }
        catch(DuplicatedTransitionException e)
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
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenWildcards.bua.xml");
            result = testObject.read();
        }
        catch(SAXException | IOException e)
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

        try
        {
            expected.addTransition(v, "A", "(*)", "(*)", "(*)", "(<)");
            expected.addTransition(v, "A", "1", "A", "1", "B");
            expected.addTransition(v, "B", "0", "A", "0", "A");
            expected.addTransition(v, "B", "0", "A", "1", "B");
            expected.addTransition(v, "B", "0", "B", "0", "B");
            expected.addTransition(v, "B", "0", "B", "1", "C");
            expected.addTransition(v, "B", "0", "C", "0", "C");
            expected.addTransition(v, "B", "0", "C", "1", "A");
            expected.addTransition(v, "B", "1", "A", "0", "A");
            expected.addTransition(v, "B", "1", "A", "1", "C");
            expected.addTransition(v, "B", "1", "B", "0", "B");
            expected.addTransition(v, "B", "1", "B", "1", "C");
            expected.addTransition(v, "B", "1", "C", "0", "A");
            expected.addTransition(v, "B", "1", "C", "1", "B");
            expected.addTransition(v, "C", "0", "(*)", "(*)", "(>)");
            expected.addTransition(v, "C", "1", "A", "0", "A");
            expected.addTransition(v, "C", "1", "A", "1", "B");
            expected.addTransition(v, "C", "1", "B", "0", "B");
            expected.addTransition(v, "C", "1", "B", "1", "C");
            expected.addTransition(v, "C", "1", "C", "0", "C");
            expected.addTransition(v, "C", "1", "C", "1", "A");
        }
        catch(DuplicatedTransitionException e)
        {
            e.printStackTrace();
            Assert.fail("Unexpected exception " + e.getClass().getSimpleName());
        }

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BottomUpDFTA);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = AutomatonParsingException.class)
    public void testReadTopDownDFTAWhenMultipleTransitions()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenMultipleTransitions.tda.xml");
        }
        catch(SAXException e)
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
    public void testReadBottomUpDFTAWhenMultipleTransitions()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenMultipleTransitions.bua.xml");
        }
        catch(SAXException e)
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
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenIncorrectAlphabetWord.tda.xml");
        }
        catch(SAXException e)
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
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenIncorrectAlphabetWord.bua.xml");
        }
        catch(SAXException e)
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
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenIncorrectVariableValue.tda.xml");
        }
        catch(SAXException e)
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
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenIncorrectVariableValue.bua.xml");
        }
        catch(SAXException e)
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
    public void testReadTopDownDFTAWhenNoSuchLabel()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchLabel.tda.xml");
        }
        catch(SAXException e)
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
    public void testReadBottomUpDFTAWhenNoSuchLabel()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchLabel.bua.xml");
        }
        catch(SAXException e)
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
    public void testReadTopDownDFTAWhenNoSuchVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchVariableValue.tda.xml");
        }
        catch(SAXException e)
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
    public void testReadBottomUpDFTAWhenNoSuchVariableValue()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchVariableValue.bua.xml");
        }
        catch(SAXException e)
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
    public void testReadTopDownDFTAWhenNoSuchVariableId()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadTopDownDFTAWhenNoSuchVariableId.tda.xml");
        }
        catch(SAXException e)
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
    public void testReadBottomUpDFTAWhenNoSuchVariableId()
        throws SAXException
    {
        try
        {
            testObject = new AutomatonReader(
                "test/ref_humbold/fita_view/automaton/testReadBottomUpDFTAWhenNoSuchVariableId.bua.xml");
        }
        catch(SAXException e)
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
}
