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
    private Stack<Pair<StandardNode, TreeChild>> nodes = new Stack<>();
    private Stack<RepeatNode> repeats = new Stack<>();
    private TreeNode tree = null;
    private int index = 1;

    public TreeNode getTree()
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
        String label;

        switch(qName)
        {
            case "null":
            case "rec":
                break;

            case "node":
                label = attributes.getValue("label");

                if(label == null)
                    throw new SAXException();

                StandardNode standardNode = new StandardNode(label, index);

                nodes.push(Pair.make(standardNode, TreeChild.LEFT));
                index += index + 1;
                break;

            case "repeat":
                label = attributes.getValue("label");

                if(label == null)
                    throw new SAXException();

                RepeatNode repeatNode = new RepeatNode(label, index);

                nodes.push(Pair.make(repeatNode, TreeChild.LEFT));
                repeats.push(repeatNode);
                index += index + 1;
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

            case "rec":
            case "node":
            case "repeat":
                if(nodes.size() > 1)
                {
                    TreeNode node;

                    if(qName.equals("rec"))
                        node = new RecNode(repeats.peek(), index);
                    else
                    {
                        Pair<StandardNode, TreeChild> nodesPair = nodes.pop();

                        if(nodesPair.getSecond() == TreeChild.RIGHT)
                            throw new OneChildException(
                                "Node must have zero or two children, but it has one.");

                        node = nodesPair.getFirst();
                    }

                    Pair<StandardNode, TreeChild> parent = nodes.pop();

                    index /= 2;

                    try
                    {
                        switch(parent.getSecond())
                        {
                            case LEFT:
                                parent.getFirst().setLeft(node);
                                nodes.push(Pair.make(parent.getFirst(), TreeChild.RIGHT));
                                --index;
                                break;

                            case RIGHT:
                                parent.getFirst().setRight(node);
                                nodes.push(Pair.make(parent.getFirst(), TreeChild.NONE));
                                break;

                            case NONE:
                                break;
                        }
                    }
                    catch(NodeHasParentException e)
                    {
                        throw new TreeParsingException("Child node has parent.", e);
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