package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class DuplicatedAcceptingValueException
    extends SAXException
{
    private static final long serialVersionUID = 1496093248408744789L;

    public DuplicatedAcceptingValueException(String s)
    {
        super(s);
    }

    public DuplicatedAcceptingValueException(String s, Exception e)
    {
        super(s, e);
    }
}
