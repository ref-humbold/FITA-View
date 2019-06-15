package fitaview.viewer;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

import fitaview.messaging.Message;
import fitaview.messaging.MessageReceiver;
import fitaview.messaging.MessageSender;

public class TitlePanel
    extends ButtonsPanel
    implements MessageSender<String>
{
    private static final long serialVersionUID = -7262175220400657532L;

    private JLabel titleLabel = new JLabel();
    private JButton openFileButton = new JButton();
    private JButton removeButton = new JButton();
    private Set<MessageReceiver<String>> receivers = new HashSet<>();

    public TitlePanel(String name, int loadShortcut, int removeShortcut)
    {
        super();

        initializeComponents(name, loadShortcut, removeShortcut);
        setLayout(new GridLayout(2, 1));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel);
        add(createButtonsPanel());
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
        addButtonToPanel(buttonsPanel, openFileButton, 2, 0);
        addButtonToPanel(buttonsPanel, removeButton, 2, 0);
        buttonsPanel.add(Box.createHorizontalGlue());

        return buttonsPanel;
    }

    private void initializeComponents(String name, int loadShortcut, int removeShortcut)
    {
        titleLabel.setText(name.toUpperCase());
        titleLabel.setFont(new Font(null, Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        openFileButton.setText(String.format("Load %s from file", name));
        openFileButton.setMnemonic(loadShortcut);
        openFileButton.addActionListener(this);

        removeButton.setText(String.format("Remove %s", name));
        removeButton.setMnemonic(removeShortcut);
        removeButton.addActionListener(this);
    }
}
