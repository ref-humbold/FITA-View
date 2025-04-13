package fitaview.automaton;

import org.xml.sax.SAXException;

public class NoVariableWithIdException
        extends SAXException
{
    private static final long serialVersionUID = -2119786768131576888L;

    public NoVariableWithIdException(String s)
    {
        super(s);
    }
}
