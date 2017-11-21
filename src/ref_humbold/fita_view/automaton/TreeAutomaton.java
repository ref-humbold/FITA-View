package ref_humbold.fita_view.automaton;

import ref_humbold.fita_view.automaton.traversing.TreeTraversing;
import ref_humbold.fita_view.tree.TreeVertex;

public interface TreeAutomaton
{
    void setTraversing(TreeTraversing traversing);

    void setTree(TreeVertex tree);

    boolean isInAlphabet(String label);

    TreeVertex generateTree();

    void run();

    void makeStepForward();

    void makeStepBackward();
}
