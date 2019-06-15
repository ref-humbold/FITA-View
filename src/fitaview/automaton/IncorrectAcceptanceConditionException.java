package fitaview.automaton;

import org.xml.sax.SAXException;

public class IncorrectAcceptanceConditionException
    extends SAXException
{
    private static final long serialVersionUID = 5392205010591913796L;

    public IncorrectAcceptanceConditionException(String s)
    {
        super(s);
    }
}
