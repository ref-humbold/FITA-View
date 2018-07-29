package refhumbold.fitaview.viewer.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.xml.sax.SAXException;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Pointer;
import refhumbold.fitaview.automaton.AutomatonIsRunningException;
import refhumbold.fitaview.automaton.TreeAutomaton;
import refhumbold.fitaview.automaton.TreeFinitenessException;
import refhumbold.fitaview.messaging.Message;
import refhumbold.fitaview.messaging.MessageReceiver;
import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.tree.TreeReader;
import refhumbold.fitaview.viewer.EmptyPanel;
import refhumbold.fitaview.viewer.TitlePanel;
import refhumbold.fitaview.viewer.UserMessageBox;
import refhumbold.fitaview.viewer.XMLFileChooser;

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

        this.initializeComponents();
        this.setBackground(Color.RED);
        this.setLayout(new BorderLayout(10, 10));

        this.add(this.titlePanel, BorderLayout.PAGE_START);
        this.add(this.zoomPanel, BorderLayout.LINE_START);
        this.add(this.drawingArea, BorderLayout.CENTER);
        this.add(new EmptyPanel(), BorderLayout.LINE_END);
        this.add(this.movingPanel, BorderLayout.PAGE_END);
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
