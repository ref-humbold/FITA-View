package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class NoVariableWithIDException
    extends SAXException
{
    private static final long serialVersionUID = -2119786768131576888L;

    public NoVariableWithIDException()
    {
        super();
    }

    public NoVariableWithIDException(String s)
    {
        super(s);
    }

    public NoVariableWithIDException(Exception e)
    {
        super(e);
    }

    public NoVariableWithIDException(String s, Exception e)
    {
        super(s, e);
    }
}
