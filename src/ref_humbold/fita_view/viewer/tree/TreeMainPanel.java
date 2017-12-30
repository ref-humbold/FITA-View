package ref_humbold.fita_view.viewer.tree;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.xml.sax.SAXException;

import ref_humbold.fita_view.automaton.FileFormatException;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.TreeReader;
import ref_humbold.fita_view.viewer.MessageBox;

public class TreeMainPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = 5944023926285119879L;
    private static final Color COLOR = Color.RED;

    private TreePointer treePointer = new TreePointer();
    private JFileChooser fileChooser = new JFileChooser();
    private JButton openFileButton = new JButton("Load tree from file");
    private JButton removeButton = new JButton("Remove tree");
    private TreeDrawingPanel drawingPanel = new TreeDrawingPanel(treePointer);

    public TreeMainPanel()
    {
        super();

        this.initializeComponents();
        this.setBackground(COLOR);

        this.add(openFileButton);
        this.add(drawingPanel);
        this.add(removeButton);
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
                    TreeNode tree = loadTree(file);

                    treePointer.set(tree);
                    MessageBox.showInfoBox("SUCCESS", "Successfully loaded file " + file.getName());
                }
                catch(Exception e)
                {
                    MessageBox.showExceptionBox(e);
                }
        }
        else if(actionEvent.getSource() == removeButton)
        {
            treePointer.delete();
        }
    }

    private File chooseFile()
    {
        int result = fileChooser.showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();

        return null;
    }

    private TreeNode loadTree(File file)
        throws FileFormatException, SAXException, IOException
    {
        TreeReader reader = new TreeReader(file);

        return reader.read();
    }

    private void initializeComponents()
    {
        openFileButton.addActionListener(this);
        removeButton.addActionListener(this);

        fileChooser.setFileFilter(new FileNameExtensionFilter("XML tree file", "tree.xml", "xml"));
        fileChooser.setMultiSelectionEnabled(false);
    }
}
