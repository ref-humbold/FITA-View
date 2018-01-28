package ref_humbold.fita_view.viewer.automaton;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AutomatonRunningModeSender;
import ref_humbold.fita_view.automaton.InfiniteTreeAutomaton;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.SignalReceiver;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class ActionButtonsPanel
    extends JPanel
    implements ActionListener, SignalReceiver
{
    private static final long serialVersionUID = 5921531603338297434L;

    private ButtonsType buttonsType = ButtonsType.NONE;
    private Pointer<TreeAutomaton> automatonPointer;
    private List<JButton> runningButtons = new ArrayList<>();
    private List<JButton> continuingButtons = new ArrayList<>();
    private JButton stopRunningButton;
    private JButton emptinessButton;

    public ActionButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        this.initializeButtons();
        this.setLayout(new GridLayout(2, 1));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        this.addComponents();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            TreeAutomaton automaton = automatonPointer.get();
            InfiniteTreeAutomaton infiniteAutomaton;

            switch(actionEvent.getActionCommand())
            {
                case "RUN":
                    automaton.run();
                    break;

                case "STEP FORWARD":
                    automaton.makeStepForward();
                    break;

                case "STOP TRAVERSING":
                    automaton.stopTraversing();
                    break;

                case "CONTINUE RUN":
                    infiniteAutomaton = (InfiniteTreeAutomaton)automaton;

                    infiniteAutomaton.continueRecursive();
                    infiniteAutomaton.run();
                    break;

                case "CONTINUE STEP FORWARD":
                    infiniteAutomaton = (InfiniteTreeAutomaton)automaton;

                    infiniteAutomaton.continueRecursive();
                    infiniteAutomaton.makeStepForward();
                    break;

                case "CHECK EMPTINESS":
                    if(automaton.checkEmptiness())
                        UserMessageBox.showInfo("AUTOMATON IS NON-EMPTY",
                                                "The automaton can accept at least one tree.");
                    else
                        UserMessageBox.showInfo("AUTOMATON IS EMPTY",
                                                "No tree can be accepted by the automaton.");
                    break;
            }
        }
        catch(Exception e)
        {
            UserMessageBox.showException(e);
        }
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
        {
            buttonsType = automatonPointer.isEmpty() ? ButtonsType.NONE : ButtonsType.RUN;
            reload();
        }
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance())
        {
            if(!automatonPointer.isEmpty())
                switch(automatonPointer.get().getRunningMode())
                {
                    case RUNNING:
                    case STOPPED:
                    case FINISHED:
                        buttonsType = ButtonsType.RUN;
                        break;

                    case CONTINUING:
                        buttonsType = ButtonsType.CONTINUE;
                        break;
                }

            reload();
        }
    }

    private void reload()
    {
        removeAll();
        addComponents();
        revalidate();
        repaint();
    }

    private void addComponents()
    {
        JPanel upperPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel lowerPanel = new JPanel(new BoxLayout(this, BoxLayout.X_AXIS));

        upperPanel.add(Box.createHorizontalGlue());

        switch(buttonsType)
        {
            case RUN:
                runningButtons.forEach(button -> {
                    upperPanel.add(Box.createRigidArea(new Dimension(1, 0)));
                    upperPanel.add(button);
                    upperPanel.add(Box.createRigidArea(new Dimension(1, 0)));
                });
                break;

            case CONTINUE:
                continuingButtons.forEach(button -> {
                    upperPanel.add(Box.createRigidArea(new Dimension(1, 0)));
                    upperPanel.add(button);
                    upperPanel.add(Box.createRigidArea(new Dimension(1, 0)));
                });
                break;

            case NONE:
                break;
        }

        upperPanel.add(Box.createHorizontalGlue());

        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        lowerPanel.add(stopRunningButton);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        lowerPanel.add(emptinessButton);
        lowerPanel.add(Box.createRigidArea(new Dimension(1, 0)));
        lowerPanel.add(Box.createHorizontalGlue());
    }

    private void initializeButtons()
    {
        JButton runButton = createButton("RUN", KeyEvent.VK_R);
        JButton continuingRunButton = createButton("CONTINUE RUN", KeyEvent.VK_R);
        JButton stepForwardButton = createButton("STEP FORWARD", KeyEvent.VK_F);
        JButton continuingStepForwardButton = createButton("CONTINUE STEP FORWARD", KeyEvent.VK_F);

        stopRunningButton = createButton("STOP TRAVERSING", KeyEvent.VK_S);
        emptinessButton = createButton("CHECK EMPTINESS", KeyEvent.VK_E);

        runningButtons.add(runButton);
        runningButtons.add(stepForwardButton);

        continuingButtons.add(continuingRunButton);
        continuingButtons.add(continuingStepForwardButton);
    }

    private JButton createButton(String text, int key)
    {
        JButton button = new JButton(text);

        button.setMnemonic(key);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setActionCommand(text);
        button.addActionListener(this);

        return button;
    }

    private enum ButtonsType
    {
        RUN, CONTINUE, NONE
    }
}
