package fitaview.utils;

import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

public abstract class XmlHandler<T>
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
        return locator == null
               ? ""
               : String.format("LINE %d, COLUMN %d", locator.getLineNumber(),
                               locator.getColumnNumber());
    }
}
