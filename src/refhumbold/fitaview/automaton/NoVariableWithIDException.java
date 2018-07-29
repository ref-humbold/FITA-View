package refhumbold.fitaview.automaton;

import org.xml.sax.SAXException;

public class NoVariableWithIDException
    extends SAXException
{
    private static final long serialVersionUID = -2119786768131576888L;

    public NoVariableWithIDException(String s)
    {
        super(s);
    }
}
