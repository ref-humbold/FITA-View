package fitaview.automaton;

import org.xml.sax.SAXException;

public class IllegalAlphabetWordException
    extends SAXException
{
    private static final long serialVersionUID = 2685639795692482834L;

    public IllegalAlphabetWordException(String s)
    {
        super(s);
    }
}
