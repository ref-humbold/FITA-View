package ref_humbold.fita_view.viewer.tree;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.tree.TreeNode;

public class TreeScrollDrawingArea
    extends JScrollPane
{
    private static final long serialVersionUID = 8365928415619342125L;

    private TreeDrawingArea drawingArea;

    public TreeScrollDrawingArea(Pointer<TreeNode> pointer)
    {
        this.drawingArea = new TreeDrawingArea(pointer);

        this.setViewportView(this.drawingArea);
        this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
}
