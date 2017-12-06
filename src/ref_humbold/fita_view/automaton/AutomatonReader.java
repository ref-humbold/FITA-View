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

import ref_humbold.fita_view.FileFormatException;
import ref_humbold.fita_view.tree.TreeParsingException;

public class AutomatonReader
{
    private File file;
    private SAXParser parser;
    private boolean isTopDown;

    public AutomatonReader(File file)
        throws SAXException, FileFormatException
    {
        if(file.getName().endsWith(".bua.xml"))
            this.isTopDown = false;
        else if(file.getName().endsWith(".tda.xml"))
            this.isTopDown = true;
        else
            throw new FileFormatException(
                "File extension is not recognizable, should be one of \'.bua.xml\' or \'.tda.xml\'.");

        this.file = file;

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
     * @throws IOException if any IO error occurs
     * @throws SAXException if any parsing error occurs
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
        StringBuilder content = null;
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
            content = new StringBuilder();

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

                    if(!variables.containsKey(varID))
                        throw new AutomatonParsingException(
                            "No variable with with ID " + varID + ".");
                    break;

                default:
                    throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
            }
        }

        @Override
        public void characters(char[] chars, int start, int length)
        {
            content.append(chars, start, length);
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
                    break;

                case "word":
                    alphabet.add(content.toString());
                    content = null;
                    break;

                case "value":
                    varValues.add(content.toString());
                    content = null;
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
            content = new StringBuilder();

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
                    try
                    {
                        automaton.addTransition(variables.get(varID), nodeValue, label, leftResult,
                                                rightResult);
                    }
                    catch(DuplicatedTransitionException e)
                    {
                        throw new AutomatonParsingException("Duplicated transition entry.", e);
                    }
                    break;

                case "label":
                    label = content.toString();
                    content = null;

                    if(!label.equals(Transitions.EVERY_VALUE) && !alphabet.contains(label))
                        throw new AutomatonParsingException(
                            "Given label \'" + label + "\' is not a part of automaton's alphabet.");
                    break;

                case "node-value":
                    nodeValue = content.toString();
                    content = null;

                    if(!nodeValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                               .contains(nodeValue))
                        throw new AutomatonParsingException("Given node-value  \'" + nodeValue
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                    break;

                case "left-result":
                    leftResult = content.toString();
                    content = null;

                    if(!leftResult.equals(Transitions.SAME_VALUE) && !variables.get(varID)
                                                                               .contains(
                                                                                   leftResult))
                        throw new AutomatonParsingException("Given left-result \'" + leftResult
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                    break;

                case "right-result":
                    rightResult = content.toString();
                    content = null;

                    if(!rightResult.equals(Transitions.SAME_VALUE) && !variables.get(varID)
                                                                                .contains(
                                                                                    rightResult))
                        throw new AutomatonParsingException("Given right-result \'" + rightResult
                                                                + "\'is not a value of variable with ID "
                                                                + varID + ".");
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
            content = new StringBuilder();

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
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "variables":
                    automaton = new BottomUpDFTA(alphabet, variables.values());
                    break;

                case "trans":
                    try
                    {
                        automaton.addTransition(variables.get(varID), leftValue, leftLabel,
                                                rightValue, rightLabel, nodeResult);
                    }
                    catch(DuplicatedTransitionException e)
                    {
                        throw new AutomatonParsingException("Duplicated transition entry.", e);
                    }
                    break;

                case "left-label":
                    leftLabel = content.toString();
                    content = null;

                    if(!leftLabel.equals(Transitions.EVERY_VALUE) && !alphabet.contains(leftLabel))
                        throw new AutomatonParsingException("Given left-label \'" + leftLabel
                                                                + "\' is not a part of automaton's alphabet.");
                    break;

                case "right-label":
                    rightLabel = content.toString();
                    content = null;

                    if(!rightLabel.equals(Transitions.EVERY_VALUE) && !alphabet.contains(
                        rightLabel))
                        throw new AutomatonParsingException("Given right-label \'" + rightLabel
                                                                + "\' is not a part of automaton's alphabet.");
                    break;

                case "left-value":
                    leftValue = content.toString();
                    content = null;

                    if(!leftValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                               .contains(leftValue))
                        throw new AutomatonParsingException("Given left-value \'" + leftValue
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                    break;

                case "right-value":
                    rightValue = content.toString();
                    content = null;

                    if(!rightValue.equals(Transitions.EVERY_VALUE) && !variables.get(varID)
                                                                                .contains(
                                                                                    rightValue))
                        throw new AutomatonParsingException("Given right-value \'" + rightValue
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                    break;

                case "node-result":
                    nodeResult = content.toString();
                    content = null;

                    if(!nodeResult.equals(Transitions.LEFT_VALUE) && !nodeResult.equals(
                        Transitions.RIGHT_VALUE) && !variables.get(varID).contains(nodeResult))
                        throw new AutomatonParsingException("Given node-result \'" + nodeResult
                                                                + "\' is not a value of variable with ID "
                                                                + varID + ".");
                    break;

                default:
                    super.endElement(uri, localName, qName);
            }
        }
    }
}
