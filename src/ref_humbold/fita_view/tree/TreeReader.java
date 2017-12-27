package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

import ref_humbold.fita_view.automaton.FileFormatException;

public class TreeReader
{
    private File file;
    private SAXParser parser;

    public TreeReader(File file)
        throws SAXException, FileFormatException
    {
        if(!file.getName().endsWith(".tree.xml"))
            throw new FileFormatException(
                "File extension is not recognizable, should be \'.tree.xml\'.");

        this.file = file;

        try
        {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory schemaFactory = SchemaFactory.newInstance(language);
            Schema schema =
                schemaFactory.newSchema(new File("src/ref_humbold/fita_view/tree/Tree.xsd"));
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
     * Reading tree from XML file.
     * @return tree object
     * @throws IOException if any IO error occurs
     * @throws SAXException if any parsing error occurs
     */
    public TreeNode read()
        throws IOException, SAXException
    {
        TreeHandler handler = new TreeHandler();

        parser.parse(file, handler);

        return handler.getTree();
    }
}
