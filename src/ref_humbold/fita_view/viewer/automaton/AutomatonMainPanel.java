package ref_humbold.fita_view.viewer.automaton;

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

import ref_humbold.fita_view.automaton.AutomatonNullableProxy;
import ref_humbold.fita_view.automaton.AutomatonReader;
import ref_humbold.fita_view.automaton.FileFormatException;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.viewer.MessageBox;

public class AutomatonMainPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = -7678389910832412322L;
    private static final Color COLOR = Color.BLUE;

    private JFileChooser fileChooser = new JFileChooser();
    private JButton openFileButton = new JButton("Load automaton from file");
    private AutomatonTreeView treeView = new AutomatonTreeView();
    private TraversingRadioButtonPanel radioButtonPanel = new TraversingRadioButtonPanel();
    private RunningButtonsPanel buttonsPanel = new RunningButtonsPanel();

    public AutomatonMainPanel()
    {
        super();

        this.initializeComponents();
        this.setBackground(COLOR);

        this.add(openFileButton);
        this.add(treeView);
        this.add(radioButtonPanel);
        this.add(buttonsPanel);
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
                    TreeAutomaton automaton = loadAutomaton(file);

                    AutomatonNullableProxy.getInstance().setAutomaton(automaton);
                    MessageBox.showInfoBox("SUCCESS", "Successfully loaded file " + file.getName());
                }
                catch(Exception e)
                {
                    MessageBox.showExceptionBox(e);
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

    private void initializeComponents()
    {
        openFileButton.addActionListener(this);

        fileChooser.addChoosableFileFilter(
            new FileNameExtensionFilter("XML bottom-up automaton file", "bua.xml", "xml"));
        fileChooser.setFileFilter(
            new FileNameExtensionFilter("XML top-down automaton file", "tda.xml", "xml"));
        fileChooser.setMultiSelectionEnabled(false);
    }
}
