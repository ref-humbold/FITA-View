package ref_humbold.fita_view.viewer.automaton;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.*;
import ref_humbold.fita_view.automaton.transition.NoSuchTransitionException;
import ref_humbold.fita_view.automaton.traversing.RecursiveContinuationException;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;
import ref_humbold.fita_view.tree.UndefinedTreeStateException;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class RunningButtonsPanel
    extends JPanel
    implements ActionListener, MessageReceiver
{
    private static final long serialVersionUID = 5921531603338297434L;

    private Pointer<TreeAutomaton> automatonPointer;
    private List<JButton> simpleButtons = new ArrayList<>();
    private List<JButton> continuingButtons = new ArrayList<>();

    public RunningButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        this.initializeButtons();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
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

                case "STOP RUNNING":
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
            }
        }
        catch(NoSuchTransitionException | IllegalVariableValueException | UndefinedTreeStateException | NoTraversingException | RecursiveContinuationException | EmptyTreeException e)
        {
            UserMessageBox.showException(e);
        }
    }

    @Override
    public void receive(Message<Void> message)
    {
        if(message.getSource() == automatonPointer)
        {
            this.removeAll();
            this.addComponents();
            this.revalidate();
            this.repaint();
        }
    }

    private void addComponents()
    {
        List<JButton> buttonsList = automatonPointer.get() == null ? Collections.emptyList()
                                                                   : simpleButtons;

        this.add(Box.createHorizontalGlue());
        this.add(Box.createRigidArea(new Dimension(10, 0)));

        for(JButton button : buttonsList)
        {
            this.add(button);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        this.add(Box.createHorizontalGlue());
    }

    private void initializeButtons()
    {
        JButton runButton = new JButton("RUN");
        JButton continuingRunButton = new JButton("CONTINUE RUN");
        JButton stepForwardButton = new JButton("STEP FORWARD");
        JButton continuingStepForwardButton = new JButton("CONTINUE STEP FORWARD");
        JButton stopRunningButton = new JButton("STOP RUNNING");

        runButton.setMnemonic(KeyEvent.VK_R);
        runButton.setVerticalTextPosition(AbstractButton.CENTER);
        runButton.setHorizontalTextPosition(AbstractButton.CENTER);
        runButton.setActionCommand(runButton.getText());
        runButton.addActionListener(this);

        continuingRunButton.setMnemonic(KeyEvent.VK_R);
        continuingRunButton.setVerticalTextPosition(AbstractButton.CENTER);
        continuingRunButton.setHorizontalTextPosition(AbstractButton.CENTER);
        continuingRunButton.setActionCommand(continuingRunButton.getText());
        continuingRunButton.addActionListener(this);

        stepForwardButton.setMnemonic(KeyEvent.VK_F);
        stepForwardButton.setVerticalTextPosition(AbstractButton.CENTER);
        stepForwardButton.setHorizontalTextPosition(AbstractButton.CENTER);
        stepForwardButton.setActionCommand(stepForwardButton.getText());
        stepForwardButton.addActionListener(this);

        continuingStepForwardButton.setMnemonic(KeyEvent.VK_F);
        continuingStepForwardButton.setVerticalTextPosition(AbstractButton.CENTER);
        continuingStepForwardButton.setHorizontalTextPosition(AbstractButton.CENTER);
        continuingStepForwardButton.setActionCommand(continuingStepForwardButton.getText());
        continuingStepForwardButton.addActionListener(this);

        stopRunningButton.setMnemonic(KeyEvent.VK_S);
        stopRunningButton.setVerticalTextPosition(AbstractButton.CENTER);
        stopRunningButton.setHorizontalTextPosition(AbstractButton.CENTER);
        stopRunningButton.setActionCommand(stepForwardButton.getText());
        stopRunningButton.addActionListener(this);

        simpleButtons.add(runButton);
        simpleButtons.add(stepForwardButton);
        simpleButtons.add(stopRunningButton);

        continuingButtons.add(continuingRunButton);
        continuingButtons.add(continuingStepForwardButton);
        continuingButtons.add(stopRunningButton);
    }
}
