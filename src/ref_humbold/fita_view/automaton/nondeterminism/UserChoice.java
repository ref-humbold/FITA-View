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
import java.util.function.Function;
import javax.swing.*;

public class UserChoice<T>
    implements StateChoice<T>, ActionListener, PropertyChangeListener
{
    private JFrame owner;
    private JDialog dialog;
    private ButtonGroup buttonGroup;
    private JOptionPane optionPane;
    private T current;
    private List<T> statesList;
    private Function<T, String> convert;

    public UserChoice(JFrame owner, Function<T, String> convert)
    {
        this.owner = owner;
        this.convert = convert;
    }

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
    public T chooseState(Collection<T> states)
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
            T value = statesList.get(i);
            JRadioButton button = new JRadioButton(convert.apply(value));

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
