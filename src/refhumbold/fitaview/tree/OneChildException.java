package refhumbold.fitaview.tree;

import org.xml.sax.SAXException;

public class OneChildException
    extends SAXException
{
    private static final long serialVersionUID = 3110952808738604691L;

    public OneChildException(String s)
    {
        super(s);
    }
}
