package ref_humbold.fita_view.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import ref_humbold.fita_view.viewer.automaton.AutomatonMainPanel;
import ref_humbold.fita_view.viewer.tree.TreeMainPanel;

public class MainWindow
{
    public static final Dimension PREFERRED_SIZE = new Dimension(800, 600);
    public static final Dimension MAXIMUM_SIZE = new Dimension(1000, 750);
    public static final Dimension MINIMUM_SIZE = new Dimension(800, 450);

    private JFrame frame = new JFrame("FITA-View");
    private TreeMainPanel treeMainPanel = new TreeMainPanel();
    private AutomatonMainPanel automatonMainPanel = new AutomatonMainPanel();

    public MainWindow()
    {
        frame.setLayout(new GridLayout(1, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(treeMainPanel);
        frame.add(automatonMainPanel);
    }

    public void start()
    {
        frame.setPreferredSize(PREFERRED_SIZE);
        frame.setMinimumSize(MINIMUM_SIZE);
        frame.setMaximumSize(MAXIMUM_SIZE);
        frame.setVisible(true);
        frame.pack();
    }
}
