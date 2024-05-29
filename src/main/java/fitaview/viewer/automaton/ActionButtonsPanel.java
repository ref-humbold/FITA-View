package fitaview.viewer.automaton;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import fitaview.FitaViewException;
import fitaview.automaton.AutomatonRunningModeSender;
import fitaview.automaton.BottomUpAutomaton;
import fitaview.automaton.IllegalVariableValueException;
import fitaview.automaton.InfiniteTreeAutomaton;
import fitaview.automaton.TreeAutomaton;
import fitaview.messaging.Message;
import fitaview.messaging.SignalReceiver;
import fitaview.utils.Pointer;
import fitaview.viewer.ButtonsPanel;
import fitaview.viewer.UserMessageBox;

public class ActionButtonsPanel
        extends ButtonsPanel
        implements ActionListener, SignalReceiver
{
    private static final long serialVersionUID = 5921531603338297434L;

    ButtonsType buttonsType = ButtonsType.NONE;
    private final Pointer<TreeAutomaton> automatonPointer;
    private final List<JButton> runningButtons = new ArrayList<>();
    private final List<JButton> continuingButtons = new ArrayList<>();
    private JButton stopRunningButton;
    private JButton emptinessButton;

    public ActionButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        automatonPointer.addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        initializeButtons();
        setLayout(new GridLayout(2, 1));
        setOpaque(false);
        addComponents();
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
                    BottomUpAutomaton bottomUp = (BottomUpAutomaton)automaton;

                    if(bottomUp.checkEmptiness())
                        UserMessageBox.showWarning("AUTOMATON IS EMPTY",
                                                   "No tree can be accepted by the automaton");
                    else
                        UserMessageBox.showInfo("AUTOMATON IS NON-EMPTY",
                                                "The automaton can accept at least one tree");
                    break;
            }
        }
        catch(IllegalVariableValueException | NoSuchElementException | FitaViewException e)
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
            if(automatonPointer.isEmpty())
                buttonsType = ButtonsType.NONE;
            else
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
        JPanel upperPanel = createPanel();
        JPanel lowerPanel = createPanel();

        upperPanel.add(Box.createHorizontalGlue());

        switch(buttonsType)
        {
            case RUN:
                runningButtons.forEach(button -> {
                    addButtonToPanel(upperPanel, button, 10, 0);
                });
                break;

            case CONTINUE:
                continuingButtons.forEach(button -> {
                    addButtonToPanel(upperPanel, button, 10, 0);
                });
                break;

            case NONE:
                break;
        }

        upperPanel.add(Box.createHorizontalGlue());
        lowerPanel.add(Box.createHorizontalGlue());

        if(buttonsType != ButtonsType.NONE)
        {
            addButtonToPanel(lowerPanel, stopRunningButton, 10, 0);

            if(automatonPointer.get() instanceof BottomUpAutomaton)
                addButtonToPanel(lowerPanel, emptinessButton, 10, 0);
        }

        lowerPanel.add(Box.createHorizontalGlue());
        add(upperPanel);
        add(lowerPanel);
    }

    private JPanel createPanel()
    {
        JPanel panel = new JPanel();

        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        return panel;
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

        initButton(button, text, key);

        return button;
    }

    enum ButtonsType
    {
        RUN, CONTINUE, NONE
    }
}
