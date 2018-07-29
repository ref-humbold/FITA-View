package refhumbold.fitaview.automaton.transition;

import org.xml.sax.SAXException;

public class DuplicatedTransitionException
    extends SAXException
{
    private static final long serialVersionUID = -5872121228497322894L;

    public DuplicatedTransitionException(String s)
    {
        super(s);
    }
}
