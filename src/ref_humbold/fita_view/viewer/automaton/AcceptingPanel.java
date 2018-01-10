package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonRunningMode;
import ref_humbold.fita_view.automaton.AutomatonRunningModeSender;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.UndefinedAcceptanceException;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;
import ref_humbold.fita_view.message.ParameterizedMessageReceiver;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;

public class AcceptingPanel
    extends JPanel
    implements ParameterizedMessageReceiver<AutomatonRunningMode>, MessageReceiver
{
    private static final long serialVersionUID = -6432696545183930929L;

    private Pointer<TreeAutomaton> automatonPointer;
    private JLabel acceptanceLabel = new JLabel();

    public AcceptingPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        this.initializeComponents();
        this.setTreeUndefined();

        this.add(acceptanceLabel);
    }

    @Override
    public void receiveParameterized(Message<AutomatonRunningMode> message)
    {
        switch(message.getParam())
        {
            case FINISHED:
                try
                {
                    if(automatonPointer.get() != null)
                        if(automatonPointer.get().isAccepted())
                            setTreeAccepted();
                        else
                            setTreeDenied();
                }
                catch(UndefinedAcceptanceException | UndefinedTreeStateException e)
                {
                    setTreeUndefined();
                }
                break;

            default:
                setTreeUndefined();
                break;
        }
    }

    @Override
    public void receive(Message<Void> message)
    {
        setTreeUndefined();
    }

    private void initializeComponents()
    {
        acceptanceLabel.setForeground(Color.WHITE);
        acceptanceLabel.setFont(new Font(null, Font.BOLD | Font.ITALIC, 14));
        acceptanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        acceptanceLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private void setTreeAccepted()
    {
        setBackground(Color.GREEN);
        acceptanceLabel.setText("TREE ACCEPTED :)");
    }

    private void setTreeDenied()
    {
        setBackground(new Color(192, 0, 0));
        acceptanceLabel.setText("TREE NOT ACCEPTED :(");
    }

    private void setTreeUndefined()
    {
        setBackground(Color.GRAY);
        acceptanceLabel.setText("-----");
    }
}
