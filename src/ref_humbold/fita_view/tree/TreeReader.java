package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ref_humbold.fita_view.Pair;

public class TreeReader
{
    private File file;
    private SAXParser parser;
    private TreeVertex tree;

    public TreeReader(String filename)
        throws SAXException
    {
        if(!filename.endsWith(".xml"))
            throw new IllegalArgumentException("File is not an XML.");

        this.file = new File(filename);
        try
        {
            this.parser = SAXParserFactory.newInstance().newSAXParser();
        }
        catch(ParserConfigurationException | SAXException e)
        {
            throw new SAXException("Cannot start parser.", e);
        }
    }

    public TreeVertex read()
        throws IOException, SAXException
    {
        parser.parse(file, new TreeHandler());

        return tree;
    }

    private class TreeHandler
        extends DefaultHandler
    {
        private Stack<TreeVertex> repeats = new Stack<>();
        private Stack<Pair<TreeVertex, Boolean>> nodes = new Stack<>();
        private int idIndex = 1;

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            switch(qName)
            {
                case "node":
                case "repeat":
                case "rec":
                    if(nodes.size() > 1)
                    {
                        TreeVertex node = nodes.pop().getFirst();
                        Pair<TreeVertex, Boolean> parent = nodes.pop();

                        if(parent.getSecond())
                            parent.getFirst().setLeft(node);
                        else
                            parent.getFirst().setRight(node);

                        nodes.push(Pair.make(parent.getFirst(), false));

                        if(qName.equals("repeat"))
                            repeats.pop();
                    }
                    break;

                default:
                    throw new SAXException();
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            switch(qName)
            {
                case "node":
                case "repeat":
                    String label = attributes.getValue("label");

                    if(label == null)
                        throw new SAXException();

                    NodeVertex node = new NodeVertex(label, idIndex);

                    nodes.push(Pair.make(node, true));

                    if(qName.equals("repeat"))
                        repeats.push(node);
                    break;

                case "rec":
                    nodes.push(Pair.make(new RecVertex(repeats.peek(), idIndex), null));
                    break;

                default:
                    throw new SAXException();
            }

            ++idIndex;
        }

        @Override
        public void endDocument()
            throws SAXException
        {
            tree = nodes.get(0).getFirst();
        }
    }
}
