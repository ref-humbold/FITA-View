package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class AutomatonParsingException
    extends SAXException
{
    private static final long serialVersionUID = -2361435525177698748L;

    public AutomatonParsingException()
    {
        super();
    }

    public AutomatonParsingException(String s)
    {
        super(s);
    }

    public AutomatonParsingException(Exception e)
    {
        super(e);
    }

    public AutomatonParsingException(String s, Exception e)
    {
        super(s, e);
    }
}
