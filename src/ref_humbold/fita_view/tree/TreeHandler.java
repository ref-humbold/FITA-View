package ref_humbold.fita_view.tree;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import ref_humbold.fita_view.Pair;

class TreeHandler
    extends DefaultHandler
{
    private Stack<TreeVertex> repeats = new Stack<>();
    private Stack<Pair<TreeVertex, TreeChild>> nodes = new Stack<>();
    private TreeVertex tree = null;
    private int index = 1;

    public TreeVertex getTree()
    {
        return tree;
    }

    @Override
    public void error(SAXParseException e)
        throws SAXException
    {
        throw new TreeParsingException(e.getMessage(), e);
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

                NodeVertex node = qName.equals("repeat") ? new RepeatVertex(label, index)
                                                         : new NodeVertex(label, index);

                nodes.push(Pair.make(node, TreeChild.LEFT));
                index += index + 1;

                if(qName.equals("repeat"))
                    repeats.push(node);
                break;

            case "rec":
                nodes.push(Pair.make(new RecVertex(repeats.peek(), index), null));
                break;

            default:
                throw new TreeParsingException("Unexpected tag: \'" + qName + "\'");
        }
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
                        throw new OneChildException(
                            "Node must have zero or two children, but it has one.");

                    index /= 2;

                    switch(parent.getSecond())
                    {
                        case LEFT:
                            parent.getFirst().setLeft(node.getFirst());
                            nodes.push(Pair.make(parent.getFirst(), TreeChild.RIGHT));
                            --index;
                            break;

                        case RIGHT:
                            parent.getFirst().setRight(node.getFirst());
                            nodes.push(Pair.make(parent.getFirst(), TreeChild.NONE));
                            break;

                        case NONE:
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
    {
        tree = nodes.empty() ? null : nodes.get(0).getFirst();
    }

    private enum TreeChild
    {
        LEFT, RIGHT, NONE
    }
}
