package ref_humbold.fita_view.automaton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            Schema schema = schemaFactory.newSchema(
                new File("src/ref_humbold/fita_view/tree/TopDownAutomaton.xsd"));
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            parserFactory.setSchema(schema);
            this.parser = parserFactory.newSAXParser();
        }
        catch(ParserConfigurationException | SAXException e)
        {
            throw new SAXException("Cannot start parser.", e);
        }
    }

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
        protected Set<String> alphabet = new HashSet<>();
        protected String tagName = null;
        private Map<Integer, Variable> variables = new HashMap<>();
        private Integer varID;

        protected List<Variable> getVariables()
        {
            return new ArrayList<>(variables.values());
        }

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
                    break;

                case "word":
                    tagName = "word";
                    break;

                case "variables":
                    break;

                case "var":
                    String init = attributes.getValue("init");

                    varID = Integer.parseInt(attributes.getValue("id"));
                    variables.put(varID, new Variable(init));
                    break;

                case "value":
                    tagName = "value";
                    break;

                case "transitions":
                    break;

                case "trans":
                    varID = Integer.parseInt(attributes.getValue("varID"));
                    break;

                case "label":
                    tagName = "label";
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
                    variables.get(varID).addValue(new String(chars, start, length));
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
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

        @Override
        public TreeAutomaton getAutomaton()
        {
            return automaton;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            switch(qName)
            {
                case "automaton":
                    isDeterministic = Boolean.parseBoolean(attributes.getValue("determinism"));
                    break;

                case "label":
                    tagName = "label";
                    break;

                case "nodeValue":
                    tagName = "nodeValue";
                    break;

                case "leftResult":
                    tagName = "leftResult";
                    break;

                case "rightResult":
                    tagName = "rightResult";
                    break;

                default:
                    super.startElement(uri, localName, qName, attributes);
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
                case "label":
                    break;

                case "nodeValue":
                    break;

                case "leftResult":
                    break;

                case "rightResult":
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
                    automaton = isDeterministic ? new TopDownDFTA(alphabet, getVariables())
                                                : new TopDownNFTA(alphabet, getVariables());
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
            switch(qName)
            {
                case "automaton":
                    break;

                case "label":
                    tagName = "label";
                    break;

                case "leftValue":
                    tagName = "leftValue";
                    break;

                case "rightValue":
                    tagName = "rightValue";
                    break;

                case "nodeResult":
                    tagName = "nodeResult";
                    break;

                default:
                    super.startElement(uri, localName, qName, attributes);
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
                case "label":
                    break;

                case "leftValue":
                    break;

                case "rightValue":
                    break;

                case "nodeResult":
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
                    automaton = new BottomUpDFTA(alphabet, getVariables());
                    break;

                default:
                    super.endElement(uri, localName, qName);
            }
        }
    }
}
