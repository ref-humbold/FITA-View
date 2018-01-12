package ref_humbold.fita_view.automaton;

import org.xml.sax.SAXException;

public class IncorrectAcceptingRuleException
    extends SAXException
{
    private static final long serialVersionUID = 5392205010591913796L;

    public IncorrectAcceptingRuleException(String s)
    {
        super(s);
    }
}
