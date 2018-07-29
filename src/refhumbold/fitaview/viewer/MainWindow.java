package refhumbold.fitaview.viewer;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JFrame;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Pointer;
import refhumbold.fitaview.automaton.TreeAutomaton;
import refhumbold.fitaview.tree.TreeNode;
import refhumbold.fitaview.viewer.automaton.AutomatonMainPanel;
import refhumbold.fitaview.viewer.tree.TreeMainPanel;

public class MainWindow
{
    private JFrame frame = new JFrame("FITA-View: Finite and Infinite Tree Automata Viewer");
    private Pointer<TreeAutomaton> automatonPointer = new Pointer<>();
    private Pointer<Pair<TreeNode, Integer>> treePointer = new Pointer<>();

    public MainWindow()
    {
        this.frame.setLayout(new GridLayout(1, 2));
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.setMinimumSize(new Dimension(800, 450));
        this.frame.setMaximumSize(new Dimension(1000, 750));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.frame.add(new TreeMainPanel(this.automatonPointer, this.treePointer));
        this.frame.add(new AutomatonMainPanel(this.automatonPointer, this.treePointer));
    }

    public void start()
    {
        frame.pack();
        frame.setVisible(true);
    }
}
