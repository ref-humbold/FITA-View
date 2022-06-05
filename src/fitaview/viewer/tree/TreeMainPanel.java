package fitaview.viewer.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.xml.sax.SAXException;

import fitaview.automaton.AutomatonIsRunningException;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.TreeFinitenessException;
import fitaview.messaging.Message;
import fitaview.messaging.MessageReceiver;
import fitaview.tree.TreeNode;
import fitaview.tree.TreeReader;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.viewer.EmptyPanel;
import fitaview.viewer.TitlePanel;
import fitaview.viewer.UserMessageBox;
import fitaview.viewer.XMLFileChooser;

public class TreeMainPanel
        extends JPanel
        implements MessageReceiver<String>
{
    private static final long serialVersionUID = 5944023926285119879L;

    private Pointer<TreeAutomaton> automatonPointer;
    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private TitlePanel titlePanel = new TitlePanel("tree", KeyEvent.VK_T, KeyEvent.VK_M);
    private TreeDrawingArea drawingArea;
    private MovingButtonsPanel movingPanel;
    private ZoomButtonsPanel zoomPanel;

    public TreeMainPanel(Pointer<TreeAutomaton> automatonPointer,
                         Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.treePointer = treePointer;

        initializeComponents();
        setBackground(Color.RED);
        setLayout(new BorderLayout(10, 10));

        add(titlePanel, BorderLayout.PAGE_START);
        add(zoomPanel, BorderLayout.LINE_START);
        add(drawingArea, BorderLayout.CENTER);
        add(new EmptyPanel(), BorderLayout.LINE_END);
        add(movingPanel, BorderLayout.PAGE_END);
    }

    @Override
    public void receiveMessage(Message<String> message)
    {
        if(!automatonPointer.isEmpty() && automatonPointer.get().isRunning())
        {
            UserMessageBox.showException(
                    new AutomatonIsRunningException("Automaton is currently running on tree!"));

            return;
        }

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

            if(!automatonPointer.isEmpty())
                try
                {
                    automatonPointer.get().setTree(null);
                }
                catch(TreeFinitenessException e)
                {
                    UserMessageBox.showException(e);
                }
        }
    }

    private File chooseFile()
    {
        int result = XMLFileChooser.getInstance().showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION)
            return XMLFileChooser.getInstance().getSelectedFile();

        return null;
    }

    private Pair<TreeNode, Integer> loadTree(File file)
            throws SAXException, IOException
    {
        TreeReader reader = new TreeReader(file);

        return reader.read();
    }

    private void initializeComponents()
    {
        titlePanel.addReceiver(this);
        drawingArea = new TreeDrawingArea(treePointer);
        movingPanel = new MovingButtonsPanel(drawingArea);
        zoomPanel = new ZoomButtonsPanel(drawingArea);
    }
}
