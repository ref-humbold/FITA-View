package refhumbold.fitaview.viewer;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class ButtonsPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = 4871313783226003288L;

    public ButtonsPanel()
    {
        super();
    }

    protected void addButtonToPanel(JPanel panel, JButton button, int xDim, int yDim)
    {
        panel.add(Box.createRigidArea(new Dimension(xDim, yDim)));
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(xDim, yDim)));
    }

    protected void initButton(JButton button, String actionCommand, int key)
    {
        button.setMnemonic(key);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
    }
}
