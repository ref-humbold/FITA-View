package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.AbstractTreeAutomaton;
import ref_humbold.fita_view.automaton.NonDeterministicAutomaton;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoiceFactory;
import ref_humbold.fita_view.automaton.nondeterminism.StateChoiceMode;
import ref_humbold.fita_view.automaton.traversing.TraversingFactory;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.SignalReceiver;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class ModifyingButtonsPanel
    extends JPanel
    implements ActionListener, SignalReceiver
{
    private static final long serialVersionUID = -5817636533870146512L;

    private Pointer<TreeAutomaton> automatonPointer;
    private ButtonGroup traversingGroup = new ButtonGroup();
    private ButtonGroup nonDeterminismGroup = new ButtonGroup();
    private Map<TraversingMode, JRadioButton> traversingButtons = new HashMap<>();
    private JPanel traversingPanel = new JPanel();
    private JPanel nonDeterminismPanel = new JPanel();

    public ModifyingButtonsPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);

        this.initializeComponents();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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
            catch(Exception e)
            {
                TreeTraversing traversing = automatonPointer.get().getTraversing();

                traversingGroup.clearSelection();

                if(traversing != null)
                    traversingButtons.get(traversing.getMode()).setSelected(true);

                UserMessageBox.showException(e);
            }
        else if(StateChoiceFactory.isCorrectMode(actionCommand))
            setChoice(actionCommand, automatonPointer.get());
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        removeAll();
        addComponents();
        revalidate();
        repaint();
    }

    private void initializeComponents()
    {
        List<JRadioButton> nonDeterminismButtons = new ArrayList<>();

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

            nonDeterminismButtons.add(button);
            nonDeterminismGroup.add(button);
        }

        traversingPanel.setLayout(new GridLayout(TraversingMode.values().length, 1));
        nonDeterminismPanel.setLayout(new GridLayout(StateChoiceMode.values().length, 1));

        traversingButtons.values().forEach(button -> traversingPanel.add(button));
        nonDeterminismButtons.forEach(button -> nonDeterminismPanel.add(button));
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
    private <T> void setChoice(String actionCommand, TreeAutomaton automaton)
    {
        NonDeterministicAutomaton<T> nonDeterministicAutomaton = (NonDeterministicAutomaton<T>)automaton;

        nonDeterministicAutomaton.setChoice(
            StateChoiceFactory.createChoice(StateChoiceMode.valueOf(actionCommand),
                                            (JFrame)SwingUtilities.windowForComponent(this),
                                            nonDeterministicAutomaton::convertToString));
    }
}
