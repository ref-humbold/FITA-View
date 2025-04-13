package fitaview.tree;

import org.xml.sax.SAXException;

public class InvalidChildrenException
        extends SAXException
{
    private static final long serialVersionUID = 3110952808738604691L;

    public InvalidChildrenException(String s)
    {
        super(s);
    }
}
