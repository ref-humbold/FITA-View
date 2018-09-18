package refhumbold.fitaview;

import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XMLHandler<T>
    extends DefaultHandler
{
    protected T result = null;
    private Locator locator;

    public T getResult()
    {
        return result;
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    protected String writePosition()
    {
        if(locator == null)
            return "";

        return String.format("LINE %d, COLUMN %d", locator.getLineNumber(),
                             locator.getColumnNumber());
    }
}
