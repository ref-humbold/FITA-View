package ref_humbold.fita_view.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import ref_humbold.fita_view.viewer.automaton.AutomatonMainPanel;
import ref_humbold.fita_view.viewer.tree.TreeMainPanel;

public class MainWindow
{
    private JFrame frame = new JFrame("FITA-View");

    public MainWindow()
    {
        frame.setLayout(new GridLayout(1, 2));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800, 450));
        frame.setMaximumSize(new Dimension(1000, 750));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new TreeMainPanel());
        frame.add(new AutomatonMainPanel());
    }

    public void start()
    {
        frame.pack();
        frame.setVisible(true);
    }
}
