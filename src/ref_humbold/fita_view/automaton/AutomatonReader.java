package ref_humbold.fita_view.automaton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ref_humbold.fita_view.tree.TreeParsingException;

public class AutomatonReader
{
    private File file;
    private SAXParser parser;
    private boolean isTopDown;

    public AutomatonReader(String filename)
        throws SAXException
    {
        if(filename.endsWith(".bua.xml"))
            this.isTopDown = false;
        else if(filename.endsWith(".tda.xml"))
            this.isTopDown = true;
        else
            throw new IllegalArgumentException(
                "File extension is not recognizable, should be one of \'.bua.xml\' or \'.tda.xml\'.");

        this.file = new File(filename);

        try
        {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory schemaFactory = SchemaFactory.newInstance(language);
            Schema schema = isTopDown ? schemaFactory.newSchema(
                new File("src/ref_humbold/fita_view/automaton/TopDownAutomaton.xsd"))
                                      : schemaFactory.newSchema(new File(
                                          "src/ref_humbold/fita_view/automaton/BottomUpAutomaton.xsd"));
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            parserFactory.setSchema(schema);
            this.parser = parserFactory.newSAXParser();
        }
        catch(ParserConfigurationException | SAXException e)
        {
            throw new SAXException("Cannot start parser.", e);
        }
    }

    /**
     * Reading automaton from XML file.
     * @return automaton object
     * @throws IOException if any IO errors occur
     * @throws SAXException if any parsing errors occur
     */
    public TreeAutomaton read()
        throws IOException, SAXException
    {
        AutomatonHandler handler =
            isTopDown ? new TopDownAutomatonHandler() : new BottomUpAutomatonHandler();

        parser.parse(file, handler);

        return handler.getAutomaton();
    }

    private abstract class AutomatonHandler
        extends DefaultHandler
    {
        Collection<String> alphabet = new ArrayList<>();
        List<String> varValues = new ArrayList<>();
        Map<Integer, Variable> variables = new HashMap<>();
        String tagName = null;
        Integer varID;

        public abstract TreeAutomaton getAutomaton();

        @Override
        public void error(SAXParseException e)
            throws SAXException
        {
            throw new AutomatonParsingException(e);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            switch(qName)
            {
                case "alphabet":
                case "variables":
                case "transitions":
                case "word":
                case "value":
                    break;

                case "var":
                    varValues = new ArrayList<>();
                    varValues.add(attributes.getValue("init"));
                    varID = Integer.parseInt(attributes.getValue("id"));
                    break;

                case "trans":
                    varID = Integer.parseInt(attributes.getValue("var-id"));
                    break;

                default:
                    throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
            }
        }

        @Override
        public void characters(char[] chars, int start, int length)
            throws SAXException
        {
            if(tagName == null)
                return;

            switch(tagName)
            {
                case "word":
                    alphabet.add(new String(chars, start, length));
                    break;

                case "value":
                    try
                    {
                        varValues.add(new String(chars, start, length));
                    }
                    catch(IllegalVariableValueException e)
                    {
                        throw new AutomatonParsingException(e);
                    }
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "automaton":
                case "alphabet":
                case "transitions":
                case "word":
                case "value":
                    break;

                case "var":
                    try
                    {
                        variables.put(varID, new Variable(varValues.get(0), varValues));
                    }
                    catch(IllegalVariableValueException e)
                    {
                        throw new AutomatonParsingException(
                            "Illegal value of variable with ID " + varID + ".", e);
                    }
                    break;

                default:
                    throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
            }
        }
    }

    private class TopDownAutomatonHandler
        extends AutomatonHandler
    {
        private TopDownTreeAutomaton automaton;
        private boolean isDeterministic;
        private String nodeValue;
        private String label;
        private String leftResult;
        private String rightResult;

        @Override
        public TreeAutomaton getAutomaton()
        {
            return automaton;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            tagName = qName;

            switch(qName)
            {
                case "automaton":
                    isDeterministic = Boolean.parseBoolean(attributes.getValue("determinism"));
                    break;

                case "label":
                case "node-value":
                case "left-result":
                case "right-result":
                    break;

                default:
                    super.startElement(uri, localName, qName, attributes);
            }
        }

        @Override
        public void characters(char[] chars, int start, int length)
            throws SAXException
        {
            switch(tagName)
            {
                case "label":
                    label = new String(chars, start, length);

                    if(!label.equals(Transitions.EVERY_VALUE) && !alphabet.contains(label))
                        throw new AutomatonParsingException(
                            "Given label is not a part of automaton's alphabet.");
                    break;

                case "node-value":
                    nodeValue = new String(chars, start, length);

                    if(!nodeValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                               .contains(nodeValue))
                        throw new AutomatonParsingException(
                            "Given left-result is not a value of variable with ID " + varID + ".");
                    break;

                case "left-result":
                    leftResult = new String(chars, start, length);

                    if(!leftResult.equals(Transitions.SAME_VALUE) && !variables.get(varID)
                                                                               .contains(
                                                                                   leftResult))
                        throw new AutomatonParsingException(
                            "Given left-result is not a value of variable with ID " + varID + ".");
                    break;

                case "right-result":
                    rightResult = new String(chars, start, length);

                    if(!rightResult.equals(Transitions.SAME_VALUE) && !variables.get(varID)
                                                                                .contains(
                                                                                    rightResult))
                        throw new AutomatonParsingException(
                            "Given right-result is not a value of variable with ID " + varID + ".");
                    break;

                default:
                    super.characters(chars, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "variables":
                    automaton = isDeterministic ? new TopDownDFTA(alphabet, variables.values())
                                                : new TopDownNFTA(alphabet, variables.values());
                    break;

                case "trans":
                    automaton.addTransition(variables.get(varID), nodeValue, label, leftResult,
                                            rightResult);
                    break;

                case "label":
                case "node-value":
                case "left-result":
                case "right-result":
                    break;

                default:
                    super.endElement(uri, localName, qName);
            }
        }
    }

    private class BottomUpAutomatonHandler
        extends AutomatonHandler
    {
        private BottomUpDFTA automaton;
        private String leftLabel;
        private String rightLabel;
        private String leftValue;
        private String rightValue;
        private String nodeResult;

        @Override
        public TreeAutomaton getAutomaton()
        {
            return automaton;
        }

        @Override
        public void error(SAXParseException e)
            throws SAXException
        {
            throw new AutomatonParsingException(e);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            tagName = qName;

            switch(qName)
            {
                case "automaton":
                case "left-label":
                case "right-label":
                case "left-value":
                case "right-value":
                case "node-result":
                    break;

                default:
                    super.startElement(uri, localName, qName, attributes);
            }
        }

        @Override
        public void characters(char[] chars, int start, int length)
            throws SAXException
        {
            switch(tagName)
            {
                case "left-label":
                    leftLabel = new String(chars, start, length);

                    if(!leftLabel.equals(Transitions.EVERY_VALUE) && !alphabet.contains(leftLabel))
                        throw new AutomatonParsingException(
                            "Given left-label is not a part of automaton's alphabet.");
                    break;

                case "right-label":
                    rightLabel = new String(chars, start, length);

                    if(!rightLabel.equals(Transitions.EVERY_VALUE) && !alphabet.contains(
                        rightLabel))
                        throw new AutomatonParsingException(
                            "Given right-label is not a part of automaton's alphabet.");
                    break;

                case "left-value":
                    leftValue = new String(chars, start, length);

                    if(!leftValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                               .contains(leftValue))
                        throw new AutomatonParsingException(
                            "Given left-value is not a value of variable with ID " + varID + ".");
                    break;

                case "right-value":
                    rightValue = new String(chars, start, length);

                    if(!rightValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                                .contains(
                                                                                    rightValue))
                        throw new AutomatonParsingException(
                            "Given right-value is not a value of variable with ID " + varID + ".");
                    break;

                case "node-result":
                    nodeResult = new String(chars, start, length);

                    if(!nodeResult.equals(Transitions.LEFT_VALUE) && !nodeResult.equals(
                        Transitions.RIGHT_VALUE) && !variables.get(varID).contains(nodeResult))
                        throw new AutomatonParsingException(
                            "Given node-result is not a value of variable with ID " + varID + ".");
                    break;

                default:
                    super.characters(chars, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "variables":
                    automaton = new BottomUpDFTA(alphabet, variables.values());
                    break;

                case "trans":
                    automaton.addTransition(variables.get(varID), leftValue, leftLabel, rightValue,
                                            rightLabel, nodeResult);
                    break;

                case "left-label":
                case "right-label":
                case "left-value":
                case "right-value":
                case "node-result":
                    break;

                default:
                    super.endElement(uri, localName, qName);
            }
        }
    }
}
