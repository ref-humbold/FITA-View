package fitaview.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import fitaview.automaton.TreeAutomaton;
import fitaview.tree.TreeNode;
import fitaview.utils.Pair;
import fitaview.utils.Pointer;
import fitaview.viewer.automaton.AutomatonMainPanel;
import fitaview.viewer.tree.TreeMainPanel;

public class MainWindow
{
    private final JFrame frame = new JFrame("FITA-View: Finite and Infinite Tree Automata Viewer");

    public MainWindow()
    {
        Pointer<TreeAutomaton> automatonPointer = new Pointer<>();
        Pointer<Pair<TreeNode, Integer>> treePointer = new Pointer<>();

        frame.setLayout(new GridLayout(1, 2));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800, 450));
        frame.setMaximumSize(new Dimension(1000, 750));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TreeMainPanel(automatonPointer, treePointer));
        frame.add(new AutomatonMainPanel(automatonPointer, treePointer));
    }

    public void start()
    {
        frame.pack();
        frame.setVisible(true);
    }
}
