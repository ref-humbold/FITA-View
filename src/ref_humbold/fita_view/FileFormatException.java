package ref_humbold.fita_view;

public class FileFormatException
    extends Exception
{
    private static final long serialVersionUID = -7877774515451121490L;

    public FileFormatException()
    {
        super();
    }

    public FileFormatException(String s)
    {
        super(s);
    }

    public FileFormatException(Throwable t)
    {
        super(t);
    }

    public FileFormatException(String s, Throwable t)
    {
        super(s, t);
    }
}
