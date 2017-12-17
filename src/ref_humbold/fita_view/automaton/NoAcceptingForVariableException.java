package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class NoAcceptingForVariableException
    extends SAXException
{
    private static final long serialVersionUID = 6841844900810248994L;

    public NoAcceptingForVariableException(String s)
    {
        super(s);
    }
}
