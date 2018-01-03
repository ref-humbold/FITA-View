package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AbstractTreeAutomaton;
import ref_humbold.fita_view.automaton.NonDeterministicAutomaton;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoiceFactory;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoiceMode;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.MessageReceiver;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class ModifyingButtonsPanel
    extends JPanel
    implements ActionListener, MessageReceiver
{
    private static final long serialVersionUID = -5817636533870146512L;

    private ButtonGroup traversingGroup = new ButtonGroup();
    private ButtonGroup nonDeterminismGroup = new ButtonGroup();
    private JPanel traversingPanel = new JPanel();
    private JPanel nonDeterminismPanel = new JPanel();
    private Pointer<TreeAutomaton> automatonPointer;

    public ModifyingButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);

        this.initializeComponents();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);

        this.addComponents();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();

        if(TraversingFactory.isCorrectMode(actionCommand))
            try
            {
                automatonPointer.get().setTraversing(TraversingMode.valueOf(actionCommand));
            }
            catch(IncorrectTraversingException e)
            {
                UserMessageBox.showException(e);
            }
        else if(StateChoiceFactory.isCorrectMode(actionCommand))
        {
            NonDeterministicAutomaton automaton = (NonDeterministicAutomaton)automatonPointer.get();

            automaton.setChoice(
                StateChoiceFactory.createChoice(StateChoiceMode.valueOf(actionCommand)));
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

    private void initializeComponents()
    {
        List<JRadioButton> traversingButtons = new ArrayList<>();
        List<JRadioButton> nonDeterminismButtons = new ArrayList<>();

        for(TraversingMode mode : TraversingMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);
            button.setMnemonic(mode.toString().charAt(0));

            traversingButtons.add(button);
            traversingGroup.add(button);
        }

        for(StateChoiceMode mode : StateChoiceMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);

            nonDeterminismButtons.add(button);
            nonDeterminismGroup.add(button);
        }

        traversingPanel.setLayout(new GridLayout(3, 1));

        for(JRadioButton button : traversingButtons)
            traversingPanel.add(button);

        nonDeterminismPanel.setLayout(new GridLayout(4, 1));

        for(JRadioButton button : nonDeterminismButtons)
            nonDeterminismPanel.add(button);
    }

    private void addComponents()
    {
        TreeAutomaton automaton = automatonPointer.get();

        this.add(Box.createVerticalGlue());

        if(automaton instanceof AbstractTreeAutomaton)
            this.add(traversingPanel);

        if(automaton instanceof NonDeterministicAutomaton)
        {
            this.add(Box.createVerticalGlue());
            this.add(nonDeterminismPanel);
        }

        this.add(Box.createVerticalGlue());
    }
}
