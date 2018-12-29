package refhumbold.fitaview.viewer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class XMLFileChooser
    extends JFileChooser
{
    private static final long serialVersionUID = -6720307160872607429L;
    private static XMLFileChooser instance = null;

    private XMLFileChooser()
    {
        super();
        setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        setMultiSelectionEnabled(false);
    }

    public static XMLFileChooser getInstance()
    {
        if(instance == null)
            instance = new XMLFileChooser();

        return instance;
    }
}
