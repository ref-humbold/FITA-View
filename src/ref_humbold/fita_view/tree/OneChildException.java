package ref_humbold.fita_view.tree;

import org.xml.sax.SAXException;

public class OneChildException
    extends SAXException
{
    private static final long serialVersionUID = 3110952808738604691L;

    public OneChildException()
    {
        super();
    }

    public OneChildException(String s)
    {
        super(s);
    }

    public OneChildException(Exception e)
    {
        super(e);
    }

    public OneChildException(String s, Exception e)
    {
        super(s, e);
    }
}
