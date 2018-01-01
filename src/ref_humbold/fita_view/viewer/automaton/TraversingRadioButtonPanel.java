package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.viewer.UserMessageBox;

public class TraversingRadioButtonPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = -5817636533870146512L;

    private List<JRadioButton> buttons = new ArrayList<>();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private Pointer<TreeAutomaton> automatonPointer;

    public TraversingRadioButtonPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;

        this.initializeButtons();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);

        this.add(Box.createVerticalGlue());
        this.add(createButtonsPanel());
        this.add(Box.createVerticalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        try
        {
            automatonPointer.get()
                            .setTraversing(TraversingMode.valueOf(actionEvent.getActionCommand()));
        }
        catch(IncorrectTraversingException e)
        {
            UserMessageBox.showException(e);
        }
    }

    private JPanel createButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new GridLayout(3, 1));

        for(JRadioButton button : buttons)
            buttonsPanel.add(button);

        return buttonsPanel;
    }

    private void initializeButtons()
    {
        for(TraversingMode mode : TraversingMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);
            button.setMnemonic(mode.toString().charAt(0));

            buttons.add(button);
            buttonGroup.add(button);
        }
    }
}
