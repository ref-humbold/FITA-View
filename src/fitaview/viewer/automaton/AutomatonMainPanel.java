package fitaview.viewer.automaton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.xml.sax.SAXException;

import fitaview.automaton.AutomatonIsRunningException;
import fitaview.automaton.AutomatonReader;
import fitaview.automaton.TreeAutomaton;
import fitaview.automaton.TreeFinitenessException;
import fitaview.messaging.Message;
import fitaview.messaging.MessageReceiver;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.viewer.EmptyPanel;
import fitaview.viewer.TitlePanel;
import fitaview.viewer.UserMessageBox;
import fitaview.viewer.XMLFileChooser;

public class AutomatonMainPanel
        extends JPanel
        implements MessageReceiver<String>
{
    private static final long serialVersionUID = -7678389910832412322L;

    private final Pointer<TreeAutomaton> automatonPointer;
    private final Pointer<Pair<TreeNode, Integer>> treePointer;
    private final TitlePanel titlePanel = new TitlePanel("automaton", KeyEvent.VK_A, KeyEvent.VK_N);
    private AcceptancePanel acceptancePanel;
    private AutomatonScrollTreeView scrollTreeView;
    private TransitionDrawingArea transitionDrawingArea;
    private ModifyingRadioButtonsPanel modifyingRadioButtonsPanel;
    private ActionButtonsPanel actionButtonsPanel;

    public AutomatonMainPanel(Pointer<TreeAutomaton> automatonPointer,
                              Pointer<Pair<TreeNode, Integer>> treePointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.treePointer = treePointer;

        initializeComponents();
        setBackground(Color.BLUE);
        setLayout(new BorderLayout(10, 10));
        addComponents();
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
                    TreeAutomaton automaton = loadAutomaton(file);

                    automaton.setSendingMessages(true);
                    automatonPointer.set(automaton);

                    if(treePointer.isEmpty())
                        automatonPointer.get().setTree(null);
                    else
                        automatonPointer.get().setTree(treePointer.get().getFirst());

                    UserMessageBox.showInfo("SUCCESS", String.format("Successfully loaded file %s",
                                                                     file.getName()));
                }
                catch(IOException | TreeFinitenessException | SAXException e)
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

        infoPanel.add(scrollTreeView);
        infoPanel.add(transitionDrawingArea);
        centralPanel.add(infoPanel, BorderLayout.CENTER);
        centralPanel.add(acceptancePanel, BorderLayout.PAGE_END);
        centralPanel.setOpaque(false);

        add(titlePanel, BorderLayout.PAGE_START);
        add(new EmptyPanel(), BorderLayout.LINE_START);
        add(centralPanel, BorderLayout.CENTER);
        add(modifyingRadioButtonsPanel, BorderLayout.LINE_END);
        add(actionButtonsPanel, BorderLayout.PAGE_END);
    }

    private File chooseFile()
    {
        int result = XMLFileChooser.getInstance().showOpenDialog(this);

        if(result == JFileChooser.APPROVE_OPTION)
            return XMLFileChooser.getInstance().getSelectedFile();

        return null;
    }

    private TreeAutomaton loadAutomaton(File file)
            throws IOException, SAXException
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
        modifyingRadioButtonsPanel = new ModifyingRadioButtonsPanel(automatonPointer);
        actionButtonsPanel = new ActionButtonsPanel(automatonPointer);
    }
}
