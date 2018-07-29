package refhumbold.fitaview.tree;

import org.xml.sax.SAXException;

public class TreeDepthException
    extends SAXException
{
    private static final long serialVersionUID = 4185505535495818929L;

    public TreeDepthException(String s)
    {
        super(s);
    }
}
