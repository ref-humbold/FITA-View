package fitaview.viewer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class XmlFileChooser
        extends JFileChooser
{
    private static final long serialVersionUID = -6720307160872607429L;
    private static XmlFileChooser instance = null;

    private XmlFileChooser()
    {
        super();
        setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        setMultiSelectionEnabled(false);
    }

    public static XmlFileChooser getInstance()
    {
        if(instance == null)
            instance = new XmlFileChooser();

        return instance;
    }
}
