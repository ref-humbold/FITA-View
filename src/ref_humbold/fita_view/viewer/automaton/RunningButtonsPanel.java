package ref_humbold.fita_view.viewer.automaton;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RunningButtonsPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = 5921531603338297434L;

    private List<JButton> buttons = new ArrayList<>();

    public RunningButtonsPanel()
    {
        super();

        this.initializeButtons();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);

        this.add(Box.createHorizontalGlue());
        this.add(Box.createRigidArea(new Dimension(10, 0)));

        for(JButton button : buttons)
        {
            this.add(button);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
        }

        this.add(Box.createHorizontalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        switch(actionEvent.getActionCommand())
        {
            case "RUN":
                break;

            case "STEP FORWARD":
                break;
        }
    }

    private void initializeButtons()
    {
        JButton runButton = new JButton("RUN");
        JButton stepForwardButton = new JButton("STEP FORWARD");

        runButton.setMnemonic(KeyEvent.VK_R);
        runButton.setVerticalTextPosition(AbstractButton.CENTER);
        runButton.setHorizontalTextPosition(AbstractButton.CENTER);
        runButton.setActionCommand("RUN");
        runButton.addActionListener(this);

        stepForwardButton.setMnemonic(KeyEvent.VK_F);
        stepForwardButton.setVerticalTextPosition(AbstractButton.CENTER);
        stepForwardButton.setHorizontalTextPosition(AbstractButton.CENTER);
        stepForwardButton.setActionCommand("STEP FORWARD");
        stepForwardButton.addActionListener(this);

        buttons.add(runButton);
        buttons.add(stepForwardButton);
    }
}
