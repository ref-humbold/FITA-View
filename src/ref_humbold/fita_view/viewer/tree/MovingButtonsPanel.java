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
    static final int STEP = 10;
    private static final long serialVersionUID = -1998309322600823517L;

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
        switch(Direction.valueOf(actionEvent.getActionCommand()))
        {
            case CENTRE:
                drawingArea.centralize();
                break;

            case UP:
                drawingArea.moveArea(0, STEP);
                break;

            case DOWN:
                drawingArea.moveArea(0, -STEP);
                break;

            case LEFT:
                drawingArea.moveArea(STEP, 0);
                break;

            case RIGHT:
                drawingArea.moveArea(-STEP, 0);
                break;
        }
    }

    private void initializeButtons()
    {
        JButton leftButton = createButton(Direction.LEFT, "triangle-left.png", KeyEvent.VK_LEFT);
        JButton upButton = createButton(Direction.UP, "triangle-up.png", KeyEvent.VK_UP);
        JButton downButton = createButton(Direction.DOWN, "triangle-down.png", KeyEvent.VK_DOWN);
        JButton rightButton = createButton(Direction.RIGHT, "triangle-right.png",
                                           KeyEvent.VK_RIGHT);
        JButton centreButton = createButton(Direction.CENTRE, "circle-dot.png", KeyEvent.VK_HOME);

        buttons.add(leftButton);
        buttons.add(upButton);
        buttons.add(centreButton);
        buttons.add(downButton);
        buttons.add(rightButton);
    }

    private JButton createButton(Direction direction, String iconFilename, int key)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconFilename));
        JButton button = new JButton(icon);

        button.setMnemonic(key);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setActionCommand(direction.toString());
        button.addActionListener(this);

        return button;
    }

    private enum Direction
    {
        LEFT, RIGHT, UP, DOWN, CENTRE
    }
}
