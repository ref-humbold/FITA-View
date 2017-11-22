package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
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

import ref_humbold.fita_view.Pair;

public class TreeReader
{
    private File file;
    private SAXParser parser;

    public TreeReader(String filename)
        throws SAXException
    {
        if(!filename.endsWith(".tree.xml"))
            throw new IllegalArgumentException(
                "File extension is not recognizable, should be \'.tree.xml\'.");

        this.file = new File(filename);
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

    public TreeVertex read()
        throws IOException, SAXException
    {
        TreeHandler handler = new TreeHandler();

        parser.parse(file, handler);

        return handler.getTree();
    }

    private enum TreeChild
    {
        LEFT, RIGHT, NONE
    }

    private class TreeHandler
        extends DefaultHandler
    {
        private Stack<TreeVertex> repeats = new Stack<>();
        private Stack<Pair<TreeVertex, TreeChild>> nodes = new Stack<>();
        private TreeVertex tree = null;
        private int idIndex = 1;

        public TreeVertex getTree()
        {
            return tree;
        }

        @Override
        public void error(SAXParseException e)
            throws SAXException
        {
            throw new TreeParsingException(e);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            switch(qName)
            {
                case "null":
                    break;

                case "node":
                case "repeat":
                    String label = attributes.getValue("label");

                    if(label == null)
                        throw new SAXException();

                    NodeVertex node = qName.equals("repeat") ? new RepeatVertex(label, idIndex)
                                                             : new NodeVertex(label, idIndex);

                    nodes.push(Pair.make(node, TreeChild.LEFT));

                    if(qName.equals("repeat"))
                        repeats.push(node);
                    break;

                case "rec":
                    if(repeats.empty())
                        throw new TreeParsingException(
                            "Vertex \'rec\' is out of scope of vertex \'repeat\'.");

                    nodes.push(Pair.make(new RecVertex(repeats.peek(), idIndex), null));
                    break;

                default:
                    throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
            }

            ++idIndex;
        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "null":
                    break;

                case "node":
                case "repeat":
                case "rec":
                    if(nodes.size() > 1)
                    {
                        Pair<TreeVertex, TreeChild> node = nodes.pop();
                        Pair<TreeVertex, TreeChild> parent = nodes.pop();

                        if(node.getSecond() == TreeChild.RIGHT)
                            throw new TreeParsingException(
                                "Node must have zero or two children, but it has one.");

                        switch(parent.getSecond())
                        {
                            case LEFT:
                                parent.getFirst().setLeft(node.getFirst());
                                nodes.push(Pair.make(parent.getFirst(), TreeChild.RIGHT));
                                break;

                            case RIGHT:
                                parent.getFirst().setRight(node.getFirst());
                                nodes.push(Pair.make(parent.getFirst(), TreeChild.NONE));
                                break;
                        }

                        if(qName.equals("repeat"))
                            repeats.pop();
                    }
                    break;

                default:
                    throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
            }
        }

        @Override
        public void endDocument()
            throws SAXException
        {
            tree = nodes.empty() ? null : nodes.get(0).getFirst();
        }
    }
}
