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

public class AutomatonReader
{
    private File file;
    private SAXParser parser;
    private boolean isTopDown;

    public AutomatonReader(File file)
        throws SAXException, FileFormatException
    {
        if(file.getName().endsWith(".bua.xml"))
        {
            this.isTopDown = false;
        }
        else if(file.getName().endsWith(".tda.xml"))
        {
            this.isTopDown = true;
        }
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
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            content = new StringBuilder();

            switch(qName)
            {
                case "alphabet":
                case "variables":
                case "accepting":
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
                        throw new NoVariableWithIDException(
                            "No variable with with ID " + varID + ".");
                    break;

                default:
                    throw new AutomatonParsingException("Unexpected tag: \'" + qName + "\'");
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
                case "accepting":
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
                        throw new IllegalVariableValueException(
                            "Illegal value of variable with ID " + varID + ". " + e.getMessage());
                    }
                    break;

                default:
                    throw new AutomatonParsingException("Unexpected tag: \'" + qName + "\'");
            }
        }

        @Override
        public void characters(char[] chars, int start, int length)
        {
            content.append(chars, start, length);
        }

        @Override
        public void error(SAXParseException e)
            throws SAXException
        {
            throw new AutomatonParsingException(e.getMessage(), e);
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
                    automaton.addTransition(variables.get(varID), nodeValue, label, leftResult,
                                            rightResult);
                    break;

                case "label":
                    label = content.toString();
                    content = null;

                    if(!label.equals(Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                        throw new IllegalAlphabetWordException(
                            "Given label \'" + label + "\' is not a part of automaton's alphabet.");
                    break;

                case "node-value":
                    nodeValue = content.toString();
                    content = null;

                    if(!nodeValue.equals(Wildcard.EVERY_VALUE) && !variables.get(varID)
                                                                            .contains(nodeValue))
                        throw new IllegalVariableValueException("Given node-value  \'" + nodeValue
                                                                    + "\' is not a value of variable with ID "
                                                                    + varID + ".");
                    break;

                case "left-result":
                    leftResult = content.toString();
                    content = null;

                    if(!leftResult.equals(Wildcard.SAME_VALUE) && !variables.get(varID)
                                                                            .contains(leftResult))
                        throw new IllegalVariableValueException("Given left-result \'" + leftResult
                                                                    + "\' is not a value of variable with ID "
                                                                    + varID + ".");
                    break;

                case "right-result":
                    rightResult = content.toString();
                    content = null;

                    if(!rightResult.equals(Wildcard.SAME_VALUE) && !variables.get(varID)
                                                                             .contains(rightResult))
                        throw new IllegalVariableValueException(
                            "Given right-result \'" + rightResult
                                + "\'is not a value of variable with ID " + varID + ".");
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
        private String label;
        private String leftValue;
        private String rightValue;
        private String nodeResult;
        private Map<Variable, String> accept;

        @Override
        public TreeAutomaton getAutomaton()
        {
            return automaton;
        }

        @Override
        public void error(SAXParseException e)
            throws SAXException
        {
            throw new AutomatonParsingException(e.getMessage(), e);
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
                case "label":
                case "left-value":
                case "right-value":
                case "node-result":
                    break;

                case "accept":
                    accept = new HashMap<>();
                    break;

                case "var-acc":
                    int id = Integer.parseInt(attributes.getValue("var-id"));
                    String value = attributes.getValue("value");

                    if(!variables.containsKey(varID))
                        throw new NoVariableWithIDException(
                            "No variable with with ID " + varID + ".");

                    Variable v = variables.get(id);

                    if(!value.equals(Wildcard.EVERY_VALUE) && !v.contains(value))
                        throw new IllegalVariableValueException("Given accepting value \'" + value
                                                                    + "\'is not a value of variable with ID "
                                                                    + id + ".");

                    if(accept.containsKey(v))
                        throw new DuplicatedAcceptingValueException(
                            "Accepting value for variable with ID " + id
                                + " has been already defined.");

                    accept.put(v, value);
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
                case "var-acc":
                    break;

                case "variables":
                    automaton = new BottomUpDFTA(alphabet, variables.values());
                    break;

                case "accept":
                    automaton.addAcceptingState(accept);
                    accept = null;
                    break;

                case "trans":
                    automaton.addTransition(variables.get(varID), leftValue, rightValue, label,
                                            nodeResult);
                    break;

                case "label":
                    label = content.toString();
                    content = null;

                    if(!label.equals(Wildcard.EVERY_VALUE) && !alphabet.contains(label))
                        throw new IllegalAlphabetWordException(
                            "Given label \'" + label + "\' is not a part of automaton's alphabet.");
                    break;

                case "left-value":
                    leftValue = content.toString();
                    content = null;

                    if(!leftValue.equals(Wildcard.EVERY_VALUE) && !leftValue.equals(
                        Wildcard.SAME_VALUE) && !variables.get(varID).contains(leftValue))
                        throw new IllegalVariableValueException("Given left-value \'" + leftValue
                                                                    + "\' is not a value of variable with ID "
                                                                    + varID + ".");
                    break;

                case "right-value":
                    rightValue = content.toString();
                    content = null;

                    if(!rightValue.equals(Wildcard.EVERY_VALUE) && !rightValue.equals(
                        Wildcard.SAME_VALUE) && !variables.get(varID).contains(rightValue))
                        throw new IllegalVariableValueException("Given right-value \'" + rightValue
                                                                    + "\' is not a value of variable with ID "
                                                                    + varID + ".");
                    break;

                case "node-result":
                    nodeResult = content.toString();
                    content = null;

                    if(!nodeResult.equals(Wildcard.LEFT_VALUE) && !nodeResult.equals(
                        Wildcard.RIGHT_VALUE) && !variables.get(varID).contains(nodeResult))
                        throw new IllegalVariableValueException("Given node-result \'" + nodeResult
                                                                    + "\' is not a value of variable with ID "
                                                                    + varID + ".");
                    break;

                default:
                    super.endElement(uri, localName, qName);
            }
        }
    }
}
