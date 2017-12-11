package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class IllegalTransitionException
    extends SAXException
{
    private static final long serialVersionUID = -2124517184564768207L;

    public IllegalTransitionException(String s)
    {
        super(s);
    }

    public IllegalTransitionException(String s, Exception e)
    {
        super(s, e);
    }
}
