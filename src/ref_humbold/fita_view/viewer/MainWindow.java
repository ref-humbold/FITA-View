package ref_humbold.fita_view.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import ref_humbold.fita_view.viewer.automaton.AutomatonMainPanel;
import ref_humbold.fita_view.viewer.tree.TreeMainPanel;

public class MainWindow
{
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
        frame.pack();
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);
    }
}
