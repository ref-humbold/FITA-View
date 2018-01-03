package ref_humbold.fita_view.viewer.automaton;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.automaton.TreeAutomaton;

public class AutomatonScrollTreeView
    extends JScrollPane
{
    private static final long serialVersionUID = 4304767885791508959L;

    private AutomatonTreeView treeView;

    public AutomatonScrollTreeView(Pointer<TreeAutomaton> pointer)
    {
        this.treeView = new AutomatonTreeView(pointer);

        this.setViewportView(this.treeView);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
}
