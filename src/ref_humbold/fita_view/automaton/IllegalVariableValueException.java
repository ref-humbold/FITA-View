package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class IllegalVariableValueException
    extends SAXException
{
    private static final long serialVersionUID = -2128138364416571613L;

    public IllegalVariableValueException(String s)
    {
        super(s);
    }
}
