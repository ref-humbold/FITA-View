package refhumbold.fitaview.viewer.tree;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import refhumbold.fitaview.viewer.ButtonsPanel;

public class ZoomButtonsPanel
    extends ButtonsPanel
{
    private static final long serialVersionUID = 4405922702318394293L;

    private List<JButton> buttons = new ArrayList<>();
    private JLabel zoomLabel = new JLabel();
    private TreeDrawingArea drawingArea;

    public ZoomButtonsPanel(TreeDrawingArea drawingArea)
    {
        super();

        this.drawingArea = drawingArea;
        initializeComponents();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        add(Box.createVerticalGlue());
        add(zoomLabel);
        add(Box.createRigidArea(new Dimension(0, 4)));
        buttons.forEach(button -> {
            addButtonToPanel(this, button, 0, 1);
        });
        add(Box.createVerticalGlue());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        switch(Zooming.valueOf(actionEvent.getActionCommand()))
        {
            case ZOOM_ZERO:
                drawingArea.zeroZoom();
                break;

            case ZOOM_IN:
                drawingArea.zoom(1);
                break;

            case ZOOM_OUT:
                drawingArea.zoom(-1);
                break;
        }

        zoomLabel.setText(Integer.toString(drawingArea.getZoomLevel()));
    }

    private void initializeComponents()
    {
        JButton inButton = createButton(Zooming.ZOOM_IN, "zoom-in.png", KeyEvent.VK_Z);
        JButton outButton = createButton(Zooming.ZOOM_OUT, "zoom-out.png", KeyEvent.VK_X);
        JButton zeroButton = createButton(Zooming.ZOOM_ZERO, "circle-zero.png", KeyEvent.VK_0);

        buttons.add(inButton);
        buttons.add(zeroButton);
        buttons.add(outButton);

        zoomLabel.setText(Integer.toString(drawingArea.getZoomLevel()));
        zoomLabel.setFont(new Font(null, Font.BOLD, 24));
        zoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zoomLabel.setVerticalAlignment(SwingConstants.CENTER);
    }

    private JButton createButton(Zooming zooming, String iconFilename, int key)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource(iconFilename));
        JButton button = new JButton(icon);

        initButton(button, zooming.toString(), key);

        return button;
    }

    private enum Zooming
    {
        ZOOM_IN, ZOOM_OUT, ZOOM_ZERO
    }
}
