package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.traversing.IncorrectTraversingException;
import ref_humbold.fita_view.automaton.traversing.TraversingMode;
import ref_humbold.fita_view.viewer.MessageBox;

public class TraversingRadioButtonPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = -5817636533870146512L;

    private Map<TraversingMode, JRadioButton> buttons = new HashMap<>();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private Pointer<TreeAutomaton> automatonPointer;

    public TraversingRadioButtonPanel(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;

        this.initializeButtons();
        this.setLayout(new GridLayout(0, 1));

        for(JRadioButton button : buttons.values())
        {
            buttonGroup.add(button);
            this.add(button);
        }
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
            MessageBox.showExceptionBox(e);
        }
    }

    private void initializeButtons()
    {
        for(TraversingMode mode : TraversingMode.values())
        {
            JRadioButton button = new JRadioButton(mode.toString());

            button.setActionCommand(mode.toString());
            button.addActionListener(this);
            button.setBackground(Color.CYAN);

            buttons.put(mode, button);
        }

        buttons.get(TraversingMode.BFS).setMnemonic(KeyEvent.VK_B);
        buttons.get(TraversingMode.DFS).setMnemonic(KeyEvent.VK_D);
        buttons.get(TraversingMode.LEVEL).setMnemonic(KeyEvent.VK_L);
    }
}
