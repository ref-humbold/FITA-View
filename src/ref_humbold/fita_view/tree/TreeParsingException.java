package ref_humbold.fita_view.tree;

import org.xml.sax.SAXException;

public class TreeParsingException
    extends SAXException
{
    private static final long serialVersionUID = -3864350332192463280L;

    public TreeParsingException()
    {
        super();
    }

    public TreeParsingException(String s)
    {
        super(s);
    }

    public TreeParsingException(Exception e)
    {
        super(e);
    }

    public TreeParsingException(String s, Exception e)
    {
        super(s, e);
    }
}
