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

import ref_humbold.fita_view.automaton.FileFormatException;
import ref_humbold.fita_view.automaton.AutomatonReader;
import ref_humbold.fita_view.automaton.TreeAutomaton;

public class AutomatonPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = -7678389910832412322L;

    private TreeAutomaton automaton = null;
    private JFileChooser fileChooser = new JFileChooser();
    private JButton openFileButton = new JButton("Load automaton from file");

    public AutomatonPanel()
    {
        super();

        this.initComponents();
        this.setBackground(new Color(0x0000FF));
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
                    automaton = loadAutomaton(file);
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

    private TreeAutomaton loadAutomaton(File file)
        throws IOException, SAXException, FileFormatException
    {
        AutomatonReader reader = new AutomatonReader(file);

        return reader.read();
    }

    private void initComponents()
    {
        openFileButton.addActionListener(this);
        add(openFileButton);

        fileChooser.addChoosableFileFilter(
            new FileNameExtensionFilter("XML bottom-up automaton file", "bua.xml", "xml"));
        fileChooser.setFileFilter(
            new FileNameExtensionFilter("XML top-down automaton file", "tda.xml", "xml"));
        fileChooser.setMultiSelectionEnabled(false);
    }
}
