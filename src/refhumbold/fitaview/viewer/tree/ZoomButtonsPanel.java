package refhumbold.fitaview.viewer.tree;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ZoomButtonsPanel
    extends JPanel
    implements ActionListener
{
    private static final long serialVersionUID = 4405922702318394293L;

    private List<JButton> buttons = new ArrayList<>();
    private JLabel zoomLabel = new JLabel();
    private TreeDrawingArea drawingArea;

    public ZoomButtonsPanel(TreeDrawingArea drawingArea)
    {
        super();

        this.drawingArea = drawingArea;
        this.initializeComponents();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        this.add(Box.createVerticalGlue());
        this.add(zoomLabel);
        this.add(Box.createRigidArea(new Dimension(0, 4)));
        this.buttons.forEach(button -> {
            this.add(Box.createRigidArea(new Dimension(0, 1)));
            this.add(button);
            this.add(Box.createRigidArea(new Dimension(0, 1)));
        });
        this.add(Box.createVerticalGlue());
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

        button.setMnemonic(key);
        button.setVerticalTextPosition(AbstractButton.CENTER);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        button.setActionCommand(zooming.toString());
        button.addActionListener(this);

        return button;
    }

    private enum Zooming
    {
        ZOOM_IN, ZOOM_OUT, ZOOM_ZERO
    }
}
