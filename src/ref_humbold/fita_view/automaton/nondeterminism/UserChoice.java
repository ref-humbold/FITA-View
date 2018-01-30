package ref_humbold.fita_view.automaton.nondeterminism;

import java.awt.Dimension;
import java.awt.Font;
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

public class UserChoice<K, R>
    implements StateChoice<K, R>, ActionListener, PropertyChangeListener
{
    private final WindowAdapter windowAdapter = new UserWindowAdapter();
    private final JFrame owner;
    private JDialog dialog;
    private ButtonGroup buttonGroup;
    private JOptionPane optionPane;
    private R current;
    private List<R> statesList;
    private Function<K, String> convertKey;
    private Function<R, String> convertResult;

    public UserChoice(JFrame owner, Function<K, String> convertKey,
                      Function<R, String> convertResult)
    {
        this.owner = owner;
        this.convertKey = convertKey;
        this.convertResult = convertResult;
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
    public R chooseState(K key, Collection<R> states)
    {
        statesList = new ArrayList<>(states);

        if(statesList.size() == 1)
            return statesList.get(0);

        createDialog(key);
        current = statesList.get(0);
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

    private void createDialog(K key)
    {
        Object[] options = new Object[]{"CHOOSE"};
        JLabel label = new JLabel(convertKey.apply(key));
        JPanel panel = new JPanel();

        label.setFont(new Font(null, Font.BOLD, 16));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(this.generateButtons());

        dialog = new JDialog(owner, true);
        optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE,
                                     JOptionPane.DEFAULT_OPTION, null, options, options[0]);

        dialog.addWindowListener(windowAdapter);

        optionPane.addPropertyChangeListener(this);
        dialog.setTitle("USER non-deterministic choice");
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
            JRadioButton button = new JRadioButton(convertResult.apply(statesList.get(i)));

            button.setActionCommand(Integer.toString(i));
            button.addActionListener(this);

            if(i == 0)
                button.setSelected(true);

            buttonGroup.add(button);
            buttonsPanel.add(button);
        }

        return buttonsPanel;
    }

    private class UserWindowAdapter
        extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent windowEvent)
        {
            JOptionPane.showMessageDialog(null, "New states haven't been chosen!", "Choose states!",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }
}
