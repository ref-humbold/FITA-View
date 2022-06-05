package fitaview.automaton.nondeterminism;

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

import fitaview.automaton.Variable;
import fitaview.viewer.UserMessageBox;

public class UserChoice<K, R>
        implements StateChoice<K, R>, ActionListener, PropertyChangeListener
{
    private final WindowAdapter windowAdapter = new UserWindowAdapter();
    private final JFrame frame = new JFrame();
    private JDialog dialog;
    private JOptionPane optionPane;
    private R current;
    private List<R> resultStates;
    private final Function<K, String> convertKey;
    private final Function<R, String> convertResult;

    public UserChoice(Function<K, String> convertKey, Function<R, String> convertResult)
    {
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
        current = resultStates.get(Integer.parseInt(actionEvent.getActionCommand()));
    }

    @Override
    public R chooseState(Variable var, K key, Collection<R> states)
    {
        ArrayList<R> statesList = new ArrayList<>(states);

        if(statesList.size() == 1)
            return statesList.get(0);

        resultStates = statesList;
        createDialog(var, key);
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

    private void createDialog(Variable var, K key)
    {
        Object[] options = new Object[]{"CHOOSE"};
        JLabel label =
                new JLabel(String.format("%s -- %s", var.getVarName(), convertKey.apply(key)));
        JPanel panel = new JPanel();

        label.setFont(new Font(null, Font.BOLD, 16));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(generateButtons());

        dialog = new JDialog(frame, "USER non-deterministic choice", true);
        optionPane =
                new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION,
                                null, options, options[0]);

        optionPane.addPropertyChangeListener(this);
        dialog.addWindowListener(windowAdapter);
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.setContentPane(optionPane);
        dialog.pack();
    }

    private JPanel generateButtons()
    {
        JPanel buttonsPanel = new JPanel(new GridLayout(resultStates.size(), 1));
        ButtonGroup buttonGroup = new ButtonGroup();

        for(int i = 0; i < resultStates.size(); ++i)
        {
            JRadioButton button = new JRadioButton(convertResult.apply(resultStates.get(i)));

            button.setActionCommand(Integer.toString(i));
            button.addActionListener(this);

            if(i == 0)
                button.setSelected(true);

            buttonGroup.add(button);
            buttonsPanel.add(button);
        }

        return buttonsPanel;
    }

    private static class UserWindowAdapter
            extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent windowEvent)
        {
            UserMessageBox.showException(
                    new StateNotChosenException("New states haven't been chosen! Choose states!"));
        }
    }
}
