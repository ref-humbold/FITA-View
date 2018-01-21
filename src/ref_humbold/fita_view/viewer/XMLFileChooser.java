package ref_humbold.fita_view.viewer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XMLFileChooser
    extends JFileChooser
{
    private static final long serialVersionUID = -6720307160872607429L;
    private static XMLFileChooser instance = null;

    private XMLFileChooser()
    {
        super();

        this.setFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        this.setMultiSelectionEnabled(false);
    }

    public static XMLFileChooser getInstance()
    {
        if(instance == null)
            instance = new XMLFileChooser();

        return instance;
    }
}
