package refhumbold.fitaview.automaton;

import org.xml.sax.SAXException;

public class NoAcceptanceForVariableException
    extends SAXException
{
    private static final long serialVersionUID = 6841844900810248994L;

    public NoAcceptanceForVariableException(String s)
    {
        super(s);
    }
}
