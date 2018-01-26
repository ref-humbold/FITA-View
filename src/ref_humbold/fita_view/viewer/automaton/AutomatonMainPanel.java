package ref_humbold.fita_view.viewer.automaton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.xml.sax.SAXException;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonReader;
import ref_humbold.fita_view.automaton.FileFormatException;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.tree.TreeNode;
import ref_humbold.fita_view.viewer.EmptyPanel;
import ref_humbold.fita_view.viewer.TitlePanel;
import ref_humbold.fita_view.viewer.UserMessageBox;
import ref_humbold.fita_view.viewer.XMLFileChooser;

public class AutomatonMainPanel
    extends JPanel
    implements MessageReceiver<String>
{
    private static final long serialVersionUID = -7678389910832412322L;

    private Pointer<TreeAutomaton> automatonPointer;
    private Pointer<Pair<TreeNode, Integer>> treePointer;
    private TitlePanel titlePanel = new TitlePanel("automaton");
    private AcceptancePanel acceptancePanel;
    private AutomatonScrollTreeView scrollTreeView;
    private TransitionDrawingArea transitionDrawingArea;
    private ModifyingButtonsPanel modifyingButtonsPanel;
    private ActionButtonsPanel actionButtonsPanel;

    public AutomatonMainPanel(Pointer<TreeAutomaton> automatonPointer,
                              Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.treePointer = treePointer;

        this.initializeComponents();
        this.setBackground(Color.BLUE);
        this.setLayout(new BorderLayout(10, 10));

        this.addComponents();
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
                    TreeAutomaton automaton = loadAutomaton(file);

                    automaton.setSendingMessages(true);
                    automatonPointer.set(automaton);

                    if(!treePointer.isEmpty())
                        automatonPointer.get().setTree(treePointer.get().getFirst());

                    UserMessageBox.showInfo("SUCCESS",
                                            "Successfully loaded file " + file.getName());
                }
                catch(Exception e)
                {
                    automatonPointer.delete();
                    UserMessageBox.showException(e);
                }
        }
        else if(Objects.equals(message.getParam(), "removeButton"))
        {
            automatonPointer.delete();
        }
    }

    private void addComponents()
    {
        JPanel centralPanel = new JPanel(new BorderLayout(5, 5));
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));

        infoPanel.add(this.scrollTreeView);
        infoPanel.add(this.transitionDrawingArea);
        centralPanel.add(infoPanel, BorderLayout.CENTER);
        centralPanel.add(this.acceptancePanel, BorderLayout.PAGE_END);
        centralPanel.setOpaque(false);

        this.add(this.titlePanel, BorderLayout.PAGE_START);
        this.add(new EmptyPanel(), BorderLayout.LINE_START);
        this.add(centralPanel, BorderLayout.CENTER);
        this.add(this.modifyingButtonsPanel, BorderLayout.LINE_END);
        this.add(this.actionButtonsPanel, BorderLayout.PAGE_END);
    }

    private File chooseFile()
    {
        int result = XMLFileChooser.getInstance().showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION)
            return XMLFileChooser.getInstance().getSelectedFile();

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
        titlePanel.addReceiver(this);

        acceptancePanel = new AcceptancePanel(automatonPointer);
        scrollTreeView = new AutomatonScrollTreeView(automatonPointer);
        transitionDrawingArea = new TransitionDrawingArea(automatonPointer);
        modifyingButtonsPanel = new ModifyingButtonsPanel(automatonPointer);
        actionButtonsPanel = new ActionButtonsPanel(automatonPointer);
    }
}
