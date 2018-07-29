package refhumbold.fitaview.automaton;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

public class AutomatonReader
{
    private File file;
    private SAXParser parser;
    private AutomatonType type;

    public AutomatonReader(File file)
        throws SAXException, FileFormatException
    {
        if(file.getName().endsWith(".bua.xml"))
            this.type = AutomatonType.BOTTOM_UP;
        else if(file.getName().endsWith(".tda.xml"))
            this.type = AutomatonType.TOP_DOWN;
        else
            throw new FileFormatException(
                "File extension is not recognizable, should be one of \'.bua.xml\' or \'.tda.xml\'.");

        this.file = file;

        try
        {
            Schema schema = getSchema(this.type);
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            parserFactory.setSchema(schema);
            this.parser = parserFactory.newSAXParser();
        }
        catch(ParserConfigurationException | SAXException e)
        {
            throw new SAXException("Cannot start parser: " + e.getMessage(), e);
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
        AutomatonHandler handler = getHandler(type);

        parser.parse(file, handler);

        return handler.getAutomaton();
    }

    private Schema getSchema(AutomatonType type)
        throws SAXException
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        switch(type)
        {
            case BOTTOM_UP:
                return schemaFactory.newSchema(getClass().getResource("BottomUpAutomaton.xsd"));

            case TOP_DOWN:
                return schemaFactory.newSchema(getClass().getResource("TopDownAutomaton.xsd"));
        }

        throw new SAXException("Incorrect type.");
    }

    private AutomatonHandler getHandler(AutomatonType type)
        throws SAXException
    {
        switch(type)
        {
            case BOTTOM_UP:
                return new BottomUpAutomatonHandler();

            case TOP_DOWN:
                return new TopDownAutomatonHandler();
        }

        throw new SAXException("Incorrect type.");
    }

    private enum AutomatonType
    {
        BOTTOM_UP, TOP_DOWN
    }
}
