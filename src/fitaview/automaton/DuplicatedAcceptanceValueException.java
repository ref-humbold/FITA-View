package fitaview.automaton;

import org.xml.sax.SAXException;

public class DuplicatedAcceptanceValueException
    extends SAXException
{
    private static final long serialVersionUID = 1496093248408744789L;

    public DuplicatedAcceptanceValueException(String s)
    {
        super(s);
    }
}
