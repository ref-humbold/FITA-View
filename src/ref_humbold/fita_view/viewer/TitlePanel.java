package ref_humbold.fita_view.viewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import ref_humbold.fita_view.message.Message;
import ref_humbold.fita_view.message.ParameterizedMessageReceiver;
import ref_humbold.fita_view.message.ParameterizedMessageSender;

public class TitlePanel
    extends JPanel
    implements ActionListener, ParameterizedMessageSender<String>
{
    private static final long serialVersionUID = -7262175220400657532L;

    private JLabel titleLabel = new JLabel();
    private JButton openFileButton = new JButton();
    private JButton removeButton = new JButton();
    private Set<ParameterizedMessageReceiver<String>> receivers = new HashSet<>();

    public TitlePanel(String name)
    {
        super();

        this.initializeComponents(name);
        this.setLayout(new GridLayout(2, 1));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        this.add(titleLabel);
        this.add(createButtonsPanel());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource() == openFileButton)
            send(new Message<>(this, "openFileButton"));
        else if(actionEvent.getSource() == removeButton)
            send(new Message<>(this, "removeButton"));
    }

    @Override
    public void addReceiver(ParameterizedMessageReceiver<String> receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(ParameterizedMessageReceiver<String> receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void send(Message<String> message)
    {
        for(ParameterizedMessageReceiver<String> r : receivers)
            r.receiveParameterized(message);
    }

    private JPanel createButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel();

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setOpaque(false);

        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(openFileButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonsPanel.add(removeButton);
        buttonsPanel.add(Box.createHorizontalGlue());

        return buttonsPanel;
    }

    private void initializeComponents(String name)
    {
        titleLabel.setText(name.toUpperCase());
        titleLabel.setFont(new Font(null, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        openFileButton.setText("Load " + name + " from file");
        openFileButton.setMnemonic(name.charAt(0));
        openFileButton.addActionListener(this);

        removeButton.setText("Remove " + name);
        removeButton.setMnemonic(name.charAt(name.length() - 1));
        removeButton.addActionListener(this);
    }
}
