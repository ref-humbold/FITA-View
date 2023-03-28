package fitaview.viewer.tree;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import fitaview.viewer.ButtonsPanel;

public class MovingButtonsPanel
        extends ButtonsPanel
{
    private static final long serialVersionUID = -1998309322600823517L;

    private final List<JButton> buttons = new ArrayList<>();
    private final TreeDrawingArea drawingArea;

    public MovingButtonsPanel(TreeDrawingArea drawingArea)
    {
        super();

        this.drawingArea = drawingArea;
        initializeButtons();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(Box.createHorizontalGlue());
        buttons.forEach(button -> {
            addButtonToPanel(this, button, 1, 0);
        });
        add(Box.createHorizontalGlue());
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
                drawingArea.moveArea(0, 1);
                break;

            case DOWN:
                drawingArea.moveArea(0, -1);
                break;

            case LEFT:
                drawingArea.moveArea(1, 0);
                break;

            case RIGHT:
                drawingArea.moveArea(-1, 0);
                break;
        }
    }

    private void initializeButtons()
    {
        JButton leftButton = createButton(Direction.LEFT, "/triangle-left.png", KeyEvent.VK_LEFT);
        JButton upButton = createButton(Direction.UP, "/triangle-up.png", KeyEvent.VK_UP);
        JButton downButton = createButton(Direction.DOWN, "/triangle-down.png", KeyEvent.VK_DOWN);
        JButton rightButton =
                createButton(Direction.RIGHT, "/triangle-right.png", KeyEvent.VK_RIGHT);
        JButton centreButton = createButton(Direction.CENTRE, "/circle-dot.png", KeyEvent.VK_HOME);

        buttons.add(leftButton);
        buttons.add(upButton);
        buttons.add(centreButton);
        buttons.add(downButton);
        buttons.add(rightButton);
    }

    private JButton createButton(Direction direction, String iconFilename, int key)
    {
        URL resource = Objects.requireNonNull(getClass().getResource(iconFilename));
        ImageIcon icon = new ImageIcon(resource);
        JButton button = new JButton(icon);

        initButton(button, direction.toString(), key);

        return button;
    }

    private enum Direction
    {
        LEFT, RIGHT, UP, DOWN, CENTRE
    }
}
