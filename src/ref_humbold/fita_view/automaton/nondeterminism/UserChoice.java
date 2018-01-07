package ref_humbold.fita_view.automaton.nondeterminism;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

import ref_humbold.fita_view.Pair;

public class UserChoice
    implements StateChoice, ActionListener, PropertyChangeListener
{
    private JFrame owner;
    private JDialog dialog;
    private ButtonGroup buttonGroup;
    private JOptionPane optionPane;
    private Pair<String, String> current;
    private List<Pair<String, String>> statesList;

    public UserChoice(JFrame owner)
    {
        this.owner = owner;
    }

    /*
    public static void main(String[] args)
    {
        UserChoice userChoice = new UserChoice(null);

        Pair<String, String> chosen = userChoice.chooseState(
            Arrays.asList(Pair.make("A", "B"), Pair.make("C", "D"), Pair.make("E", "F"),
                          Pair.make("G", "H")));

        System.out.println(chosen);
    }
    */

    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.USER;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        current = statesList.get(Integer.parseInt(actionEvent.getActionCommand()));
    }

    @Override
    public Pair<String, String> chooseState(Collection<Pair<String, String>> states)
    {
        statesList = new ArrayList<>(states);

        if(statesList.size() == 1)
            return statesList.get(0);

        createDialog();
        dialog.setVisible(true);

        return current;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        if(dialog.isVisible() && event.getSource() == optionPane && Objects.equals(
            event.getPropertyName(), JOptionPane.VALUE_PROPERTY))
            dialog.dispose();
    }

    private void createDialog()
    {
        Object[] options = new Object[]{"CHOOSE"};
        JPanel buttonsPanel = this.generateButtons();

        dialog = new JDialog(owner, true);
        optionPane = new JOptionPane(buttonsPanel, JOptionPane.QUESTION_MESSAGE,
                                     JOptionPane.DEFAULT_OPTION, null, options, options[0]);

        dialog.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                JOptionPane.showMessageDialog(null, "New states haven't been chosen!",
                                              "Choose states!", JOptionPane.ERROR_MESSAGE);
            }
        });

        optionPane.addPropertyChangeListener(this);
        dialog.setTitle("user non-deterministic choice");
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.setContentPane(optionPane);
        dialog.pack();
    }

    private JPanel generateButtons()
    {
        JPanel buttonsPanel = new JPanel(new GridLayout(statesList.size(), 1));

        buttonGroup = new ButtonGroup();

        for(int i = 0; i < statesList.size(); ++i)
        {
            Pair<String, String> values = statesList.get(i);

            JRadioButton button = new JRadioButton(
                "LEFT VALUE = \'" + values.getFirst() + "\'    RIGHT VALUE = \'"
                    + values.getSecond() + "\'");

            button.setActionCommand(Integer.toString(i));
            button.addActionListener(this);

            if(i == 0)
                button.setSelected(true);

            buttonGroup.add(button);
            buttonsPanel.add(button);
        }

        return buttonsPanel;
    }
}
