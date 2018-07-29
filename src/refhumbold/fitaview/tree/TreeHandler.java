package refhumbold.fitaview.tree;

import java.util.Objects;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import refhumbold.fitaview.Pair;

class TreeHandler
    extends DefaultHandler
{
    private Stack<Pair<StandardNode, TreeChild>> nodes = new Stack<>();
    private Stack<RepeatNode> repeats = new Stack<>();
    private TreeNode tree = null;
    private Locator locator;
    private int index = 1;
    private int actualDepth = 0;
    private int maxDepth = 0;

    public TreeNode getTree()
    {
        return tree;
    }

    public int getMaxDepth()
    {
        return this.maxDepth;
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
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

        ++actualDepth;
        maxDepth = Math.max(maxDepth, actualDepth);

        if(actualDepth > TreeNode.MAX_HEIGHT)
            throw new TreeDepthException(
                writePosition() + "Tree depth is greater than allowed " + Integer.toString(
                    TreeNode.MAX_HEIGHT) + ".");

        switch(qName)
        {
            case "rec":
                break;

            case "node":
                label = attributes.getValue("label");

                if(label == null)
                    throw new TreeParsingException(writePosition() + "Label is null");

                StandardNode standardNode = new StandardNode(label, index);

                nodes.push(Pair.make(standardNode, TreeChild.LEFT));
                index += index + 1;
                break;

            case "repeat":
                label = attributes.getValue("label");

                if(label == null)
                    throw new TreeParsingException(writePosition() + "Label is null");

                RepeatNode repeatNode = new RepeatNode(label, index);

                nodes.push(Pair.make(repeatNode, TreeChild.LEFT));
                repeats.push(repeatNode);
                index += index + 1;
                break;

            default:
                throw new TreeParsingException(
                    writePosition() + "Unexpected tag: \'" + qName + "\'");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException
    {
        switch(qName)
        {
            case "rec":
            case "node":
            case "repeat":
                if(nodes.size() > 1)
                {
                    TreeNode node;

                    if(Objects.equals(qName, "rec"))
                        node = new RecNode(repeats.peek(), index);
                    else
                    {
                        Pair<StandardNode, TreeChild> nodesPair = nodes.pop();

                        if(nodesPair.getSecond() == TreeChild.LEFT)
                            index /= 2;
                        else if(nodesPair.getSecond() == TreeChild.RIGHT)
                            throw new OneChildException(writePosition()
                                                            + "Node must have zero or two children, but it has one.");

                        node = nodesPair.getFirst();
                    }

                    Pair<StandardNode, TreeChild> parent = nodes.pop();

                    index /= 2;
                    --actualDepth;

                    try
                    {
                        switch(parent.getSecond())
                        {
                            case LEFT:
                                parent.getFirst().setLeft(node);
                                nodes.push(Pair.make(parent.getFirst(), TreeChild.RIGHT));
                                index *= 2;
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

                    if(Objects.equals(qName, "repeat"))
                        repeats.pop();
                }
                break;

            default:
                throw new TreeParsingException(
                    writePosition() + "Unexpected tag: \'" + qName + "\'");
        }
    }

    @Override
    public void endDocument()
    {
        tree = nodes.empty() ? null : nodes.get(0).getFirst();
    }

    private String writePosition()
    {
        if(locator == null)
            return "";

        return "LINE " + locator.getLineNumber() + ", COLUMN " + locator.getColumnNumber() + ": ";
    }

    private enum TreeChild
    {
        LEFT, RIGHT, NONE
    }
}
