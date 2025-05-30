package fitaview.tree;

import java.util.Objects;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fitaview.utils.Pair;
import fitaview.utils.XmlHandler;

class TreeHandler
        extends XmlHandler<TreeNode>
{
    private static final int MAX_HEIGHT = 15;
    private final Stack<Pair<StandardNode, TreeChild>> nodes = new Stack<>();
    private final Stack<RepeatNode> repeats = new Stack<>();
    private int index = 1;
    private int actualDepth = 0;
    private int maxDepth = 0;

    @Override
    public TreeNode getResult()
    {
        return result;
    }

    public int getMaxDepth()
    {
        return maxDepth;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
    {
        String label;

        ++actualDepth;
        maxDepth = Math.max(maxDepth, actualDepth);

        if(actualDepth > MAX_HEIGHT)
            throw new TreeDepthException(
                    String.format("%s: Tree depth is greater than allowed %d", writePosition(),
                                  MAX_HEIGHT));

        switch(qName)
        {
            case "rec":
                break;

            case "node":
                label = attributes.getValue("label");

                if(label == null)
                    throw new TreeParsingException(
                            String.format("%s: Label is null", writePosition()));

                StandardNode standardNode = new StandardNode(label, index);

                nodes.push(Pair.make(standardNode, TreeChild.LEFT));
                index += index + 1;
                break;

            case "repeat":
                label = attributes.getValue("label");

                if(label == null)
                    throw new TreeParsingException(
                            String.format("%s: Label is null", writePosition()));

                RepeatNode repeatNode = new RepeatNode(label, index);

                nodes.push(Pair.make(repeatNode, TreeChild.LEFT));
                repeats.push(repeatNode);
                index += index + 1;
                break;

            default:
                throw new TreeParsingException(
                        String.format("%s: Unexpected tag: '%s'", writePosition(), qName));
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
                            throw new InvalidChildrenException(String.format(
                                    "%s: Node must have zero or two children, but it has one",
                                    writePosition()));

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
                        throw new TreeParsingException("Child node has parent", e);
                    }

                    if(Objects.equals(qName, "repeat"))
                        repeats.pop();
                }
                break;

            default:
                throw new TreeParsingException(
                        String.format("%s: Unexpected tag: '%s'", writePosition(), qName));
        }
    }

    @Override
    public void endDocument()
    {
        result = nodes.empty() ? null : nodes.get(0).getFirst();
    }

    @Override
    public void error(SAXParseException e)
            throws SAXException
    {
        throw new TreeParsingException(e.getMessage(), e);
    }

    private enum TreeChild
    {
        LEFT, RIGHT, NONE
    }
}
