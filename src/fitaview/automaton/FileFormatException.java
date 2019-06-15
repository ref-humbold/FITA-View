package fitaview.automaton;

import org.xml.sax.SAXException;

public class FileFormatException
    extends SAXException
{
    private static final long serialVersionUID = -7877774515451121490L;

    public FileFormatException(String s)
    {
        super(s);
    }
}
