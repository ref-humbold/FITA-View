package ref_humbold.fita_view.automaton;

public class FileFormatException
    extends Exception
{
    private static final long serialVersionUID = -7877774515451121490L;

    public FileFormatException(String s)
    {
        super(s);
    }

    public FileFormatException(String s, Throwable t)
    {
        super(s, t);
    }
}
