package fitaview.viewer.automaton;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fitaview.FITAViewException;
import fitaview.automaton.AutomatonRunningModeSender;
import fitaview.automaton.TreeAutomaton;
import fitaview.messaging.Message;
import fitaview.messaging.SignalReceiver;
import fitaview.utils.Pointer;
import fitaview.viewer.UserMessageBox;

public class AcceptancePanel
        extends JPanel
        implements SignalReceiver
{
    static final Color DARK_RED = new Color(192, 0, 0);
    private static final long serialVersionUID = -6432696545183930929L;
    final JLabel acceptanceLabel = new JLabel();
    private final Pointer<TreeAutomaton> automatonPointer;

    public AcceptancePanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        automatonPointer.addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        initializeComponents();
        setTreeUndefined();
        add(acceptanceLabel);
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
            setTreeUndefined();
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance()
                && !automatonPointer.isEmpty())
            switch(automatonPointer.get().getRunningMode())
            {
                case FINISHED:
                    try
                    {
                        if(!automatonPointer.isEmpty())
                            if(automatonPointer.get().isAccepted())
                                setTreeAccepted();
                            else
                                setTreeRejected();
                    }
                    catch(FITAViewException e)
                    {
                        UserMessageBox.showException(e);
                        setTreeUndefined();
                    }
                    break;

                case CONTINUING:
                    try
                    {
                        if(!automatonPointer.isEmpty())
                        {
                            Boolean accepted = automatonPointer.get().isAccepted();

                            if(accepted == null)
                                setTreeUndefined();
                            else if(accepted)
                                setTreeAccepted();
                            else
                                setTreeRejected();
                        }
                    }
                    catch(FITAViewException e)
                    {
                        UserMessageBox.showException(e);
                        setTreeUndefined();
                    }
                    break;

                default:
                    setTreeUndefined();
                    break;
            }
    }

    private void initializeComponents()
    {
        acceptanceLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
        acceptanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        acceptanceLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void setTreeAccepted()
    {
        setBackground(Color.GREEN);
        acceptanceLabel.setForeground(Color.BLACK);
        acceptanceLabel.setText("TREE ACCEPTED :)");
        UserMessageBox.showInfo("ACCEPTED", "Automaton has accepted the tree");
    }

    private void setTreeRejected()
    {
        setBackground(DARK_RED);
        acceptanceLabel.setForeground(Color.WHITE);
        acceptanceLabel.setText("TREE REJECTED :(");
        UserMessageBox.showInfo("REJECTED", "Automaton has rejected the tree");
    }

    private void setTreeUndefined()
    {
        setBackground(Color.DARK_GRAY);
        acceptanceLabel.setForeground(Color.WHITE);
        acceptanceLabel.setText("------");
    }
}
