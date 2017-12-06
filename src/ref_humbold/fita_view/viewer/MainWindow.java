package ref_humbold.fita_view.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

public class MainWindow
{
    private JFrame frame = new JFrame("FITA-View");
    private MainPanel treePanel = new TreePanel();
    private MainPanel automatonPanel = new AutomatonPanel();

    public MainWindow()
    {
        frame.setLayout(new GridLayout(1, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(treePanel);
        frame.add(automatonPanel);
    }

    public void start()
    {
        frame.pack();
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);
    }
}
