package refhumbold.fitaview.viewer.automaton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import refhumbold.fitaview.FITAViewException;
import refhumbold.fitaview.Pointer;
import refhumbold.fitaview.automaton.AbstractTreeAutomaton;
import refhumbold.fitaview.automaton.NonDeterministicAutomaton;
import refhumbold.fitaview.automaton.TreeAutomaton;
import refhumbold.fitaview.automaton.nondeterminism.StateChoice;
import refhumbold.fitaview.automaton.nondeterminism.StateChoiceFactory;
import refhumbold.fitaview.automaton.nondeterminism.StateChoiceMode;
import refhumbold.fitaview.automaton.traversing.TraversingFactory;
import refhumbold.fitaview.automaton.traversing.TraversingMode;
import refhumbold.fitaview.automaton.traversing.TreeTraversing;
import refhumbold.fitaview.messaging.Message;
import refhumbold.fitaview.messaging.SignalReceiver;
import refhumbold.fitaview.viewer.UserMessageBox;

public class ModifyingRadioButtonsPanel
    extends JPanel
    implements ActionListener, SignalReceiver
{
    private static final long serialVersionUID = -5817636533870146512L;

    private Pointer<TreeAutomaton> automatonPointer;
    private ButtonGroup traversingGroup = new ButtonGroup();
    private ButtonGroup nonDeterminismGroup = new ButtonGroup();
    private Map<StateChoiceMode, JRadioButton> nonDeterminismButtons = new HashMap<>();
    private Map<TraversingMode, JRadioButton> traversingButtons = new HashMap<>();
    private JPanel traversingPanel = new JPanel();
    private JPanel nonDeterminismPanel = new JPanel();

    public ModifyingRadioButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        automatonPointer.addReceiver(this);

        initializeComponents();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        addComponents();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        String actionCommand = actionEvent.getActionCommand();
        TreeAutomaton automaton = automatonPointer.get();

        if(TraversingFactory.isCorrectMode(actionCommand))
            try
            {
                automaton.setTraversing(TraversingMode.valueOf(actionCommand));
            }
            catch(FITAViewException e)
            {
                TreeTraversing traversing = automaton.getTraversing();

                traversingGroup.clearSelection();

                if(traversing != null)
                    traversingButtons.get(traversing.getMode()).setSelected(true);

                UserMessageBox.showException(e);
            }
        else if(StateChoiceFactory.isCorrectMode(actionCommand))
            setChoice(actionCommand, automaton);
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        traversingGroup.clearSelection();
        nonDeterminismGroup.clearSelection();
        removeAll();
        addComponents();
        revalidate();
        repaint();
    }

    private void initializeComponents()
    {
        for(TraversingMode mode : TraversingMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);
            button.setMnemonic(mode.toString().charAt(0));

            traversingButtons.put(mode, button);
            traversingGroup.add(button);
        }

        for(StateChoiceMode mode : StateChoiceMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);

            nonDeterminismButtons.put(mode, button);
            nonDeterminismGroup.add(button);
        }

        traversingPanel.setLayout(new GridLayout(TraversingMode.values().length, 1));
        traversingButtons.values().forEach(button -> traversingPanel.add(button));
        nonDeterminismPanel.setLayout(new GridLayout(StateChoiceMode.values().length, 1));
        nonDeterminismButtons.values().forEach(button -> nonDeterminismPanel.add(button));
    }

    private void addComponents()
    {
        TreeAutomaton automaton = automatonPointer.get();

        add(Box.createVerticalGlue());

        if(automaton != null)
        {
            if(automaton instanceof AbstractTreeAutomaton)
                add(traversingPanel);

            if(automaton instanceof NonDeterministicAutomaton)
            {
                add(Box.createVerticalGlue());
                add(nonDeterminismPanel);
            }
        }

        add(Box.createVerticalGlue());
    }

    @SuppressWarnings("unchecked")
    private <K, R> void setChoice(String actionCommand, TreeAutomaton automaton)
    {
        NonDeterministicAutomaton<K, R> nonDeterministicAutomaton = (NonDeterministicAutomaton<K, R>)automaton;

        try
        {
            if(StateChoiceMode.valueOf(actionCommand) == StateChoiceMode.USER)
            {
                nonDeterministicAutomaton.setChoice(StateChoiceFactory.createUserChoice(
                    nonDeterministicAutomaton::convertKeyToString,
                    nonDeterministicAutomaton::convertResultToString));
            }
            else
            {
                nonDeterministicAutomaton.setChoice(StateChoiceFactory.createAutomatedChoice(
                    StateChoiceMode.valueOf(actionCommand)));
            }
        }
        catch(FITAViewException e)
        {
            StateChoice<K, R> choice = nonDeterministicAutomaton.getChoice();

            nonDeterminismGroup.clearSelection();

            if(choice != null)
                nonDeterminismButtons.get(choice.getMode()).setSelected(true);

            UserMessageBox.showException(e);
        }
    }
}
