package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class DuplicatedTransitionException
    extends SAXException
{
    private static final long serialVersionUID = -5872121228497322894L;

    public DuplicatedTransitionException(String s)
    {
        super(s);
    }

    public DuplicatedTransitionException(String s, Exception e)
    {
        super(s, e);
    }
}
