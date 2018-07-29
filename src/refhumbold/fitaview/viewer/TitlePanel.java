package refhumbold.fitaview.viewer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import refhumbold.fitaview.messaging.Message;
import refhumbold.fitaview.messaging.MessageReceiver;
import refhumbold.fitaview.messaging.MessageSender;

public class TitlePanel
    extends JPanel
    implements ActionListener, MessageSender<String>
{
    private static final long serialVersionUID = -7262175220400657532L;

    private JLabel titleLabel = new JLabel();
    private JButton openFileButton = new JButton();
    private JButton removeButton = new JButton();
    private Set<MessageReceiver<String>> receivers = new HashSet<>();

    public TitlePanel(String name, int loadShortcut, int removeShortcut)
    {
        super();

        this.initializeComponents(name, loadShortcut, removeShortcut);
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
            send("openFileButton");
        else if(actionEvent.getSource() == removeButton)
            send("removeButton");
    }

    @Override
    public void addReceiver(MessageReceiver<String> receiver)
    {
        receivers.add(receiver);
    }

    @Override
    public void removeReceiver(MessageReceiver<String> receiver)
    {
        receivers.remove(receiver);
    }

    @Override
    public void sendMessage(Message<String> message)
    {
        receivers.forEach(r -> r.receiveMessage(message));
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

    private void initializeComponents(String name, int loadShortcut, int removeShortcut)
    {
        titleLabel.setText(name.toUpperCase());
        titleLabel.setFont(new Font(null, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        openFileButton.setText("Load " + name + " from file");
        openFileButton.setMnemonic(loadShortcut);
        openFileButton.addActionListener(this);

        removeButton.setText("Remove " + name);
        removeButton.setMnemonic(removeShortcut);
        removeButton.addActionListener(this);
    }
}
