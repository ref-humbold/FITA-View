package ref_humbold.fita_view.viewer.tree;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MovingButtonsPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = -1998309322600823517L;
    private static final int STEP = 10;

    private List<JButton> buttons = new ArrayList<>();
    private TreeDrawingArea drawingArea;

    public MovingButtonsPanel(TreeDrawingArea drawingArea)
    {
        super();

        this.drawingArea = drawingArea;
        this.initializeButtons();

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        this.add(Box.createHorizontalGlue());

        for(JButton button : this.buttons)
        {
            this.add(Box.createRigidArea(new Dimension(1, 0)));
            this.add(button);
            this.add(Box.createRigidArea(new Dimension(1, 0)));
        }

        this.add(Box.createHorizontalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        switch(actionEvent.getActionCommand())
        {
            case "UP":
                drawingArea.moveArea(0, STEP);
                break;

            case "DOWN":
                drawingArea.moveArea(0, -STEP);
                break;

            case "LEFT":
                drawingArea.moveArea(STEP, 0);
                break;

            case "RIGHT":
                drawingArea.moveArea(-STEP, 0);
                break;
        }
    }

    private void initializeButtons()
    {
        JButton leftButton = createButton("LEFT", "triangle-left.png", KeyEvent.VK_LEFT);
        JButton upButton = createButton("UP", "triangle-up.png", KeyEvent.VK_UP);
        JButton downButton = createButton("DOWN", "triangle-down.png", KeyEvent.VK_DOWN);
        JButton rightButton = createButton("RIGHT", "triangle-right.png", KeyEvent.VK_RIGHT);

        buttons.add(leftButton);
        buttons.add(upButton);
        buttons.add(downButton);
        buttons.add(rightButton);
    }

    private JButton createButton(String direction, String iconFilename, int key)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconFilename));
        JButton button = new JButton(icon);

        button.setMnemonic(key);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setActionCommand(direction);
        button.addActionListener(this);

        return button;
    }
}
