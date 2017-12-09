package ref_humbold.fita_view.viewer;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xml.sax.SAXException;

import ref_humbold.fita_view.FileFormatException;
import ref_humbold.fita_view.tree.TreeReader;
import ref_humbold.fita_view.tree.TreeVertex;

public class TreePanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = 5944023926285119879L;

    private TreeVertex tree = null;
    private JFileChooser fileChooser = new JFileChooser();
    private JButton openFileButton = new JButton("Load tree from file");

    public TreePanel()
    {
        super();

        this.initComponents();
        this.setBackground(new Color(0xFF0000));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == openFileButton)
        {
            File file = chooseFile();

            if(file != null)
                try
                {
                    tree = loadTree(file);
                    JOptionPane.showMessageDialog(null, "Successfully loaded " + file.getName(),
                                                  "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                }
                catch(FileFormatException | IOException | SAXException e)
                {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(),
                                                  e.getClass().getSimpleName(),
                                                  JOptionPane.ERROR_MESSAGE);
                }
        }
    }

    private File chooseFile()
    {
        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();

        return null;
    }

    private TreeVertex loadTree(File file)
        throws FileFormatException, SAXException, IOException
    {
        TreeReader reader = new TreeReader(file);

        return reader.read();
    }

    private void initComponents()
    {
        this.openFileButton.addActionListener(this);
        this.add(openFileButton);

        fileChooser.setFileFilter(new FileNameExtensionFilter("XML tree file", "tree.xml", "xml"));
        fileChooser.setMultiSelectionEnabled(false);
    }
}
