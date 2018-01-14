package ref_humbold.fita_view.viewer.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.xml.sax.SAXException;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.FileFormatException;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.tree.TreeReader;
import ref_humbold.fita_view.viewer.EmptyPanel;
import ref_humbold.fita_view.viewer.TitlePanel;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class TreeMainPanel
    extends JPanel
    implements MessageReceiver<String>
{
    private static final long serialVersionUID = 5944023926285119879L;

    private Pointer<TreeAutomaton> automatonPointer;
    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private JFileChooser fileChooser = new JFileChooser();
    private TitlePanel titlePanel = new TitlePanel("tree");
    private TreeDrawingArea drawingArea;
    private MovingButtonsPanel buttonsPanel;

    public TreeMainPanel(Pointer<TreeAutomaton> automatonPointer,
                         Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.treePointer = treePointer;

        this.initializeComponents();
        this.setBackground(Color.RED);
        this.setLayout(new BorderLayout(10, 10));

        this.add(this.titlePanel, BorderLayout.PAGE_START);
        this.add(new EmptyPanel(), BorderLayout.LINE_START);
        this.add(this.drawingArea, BorderLayout.CENTER);
        this.add(new EmptyPanel(), BorderLayout.LINE_END);
        this.add(this.buttonsPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void receiveMessage(Message<String> message)
    {
        if(Objects.equals(message.getParam(), "openFileButton"))
        {
            File file = chooseFile();

            if(file != null)
                try
                {
                    Pair<TreeNode, Integer> tree = loadTree(file);

                    treePointer.set(tree);

                    if(!automatonPointer.isEmpty())
                        automatonPointer.get().setTree(treePointer.get().getFirst());

                    UserMessageBox.showInfo("SUCCESS",
                                            "Successfully loaded file " + file.getName());
                }
                catch(Exception e)
                {
                    treePointer.delete();
                    UserMessageBox.showException(e);
                }
        }
        else if(Objects.equals(message.getParam(), "removeButton"))
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

    private Pair<TreeNode, Integer> loadTree(File file)
        throws FileFormatException, SAXException, IOException
    {
        TreeReader reader = new TreeReader(file);

        return reader.read();
    }

    private void initializeComponents()
    {
        titlePanel.addReceiver(this);

        fileChooser.setFileFilter(new FileNameExtensionFilter("XML tree file", "tree.xml", "xml"));
        fileChooser.setMultiSelectionEnabled(false);

        drawingArea = new TreeDrawingArea(treePointer);
        buttonsPanel = new MovingButtonsPanel(drawingArea);
    }
}
