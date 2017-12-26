package ref_humbold.fita_view.automaton;

import java.util.Collection;

import ref_humbold.fita_view.tree.TreeVertex;

public abstract class InfiniteTreeAutomaton
    extends SimpleTreeAutomaton
{
    public InfiniteTreeAutomaton(Collection<Variable> variables, Collection<String> alphabet)
    {
        super(variables, alphabet);
    }

    @Override
    public void setTree(TreeVertex tree)
        throws TreeFinitenessException, EmptyTreeException
    {
        if(!containsRecursiveNode(tree))
            throw new TreeFinitenessException("Specified tree is finite.");

        super.setTree(tree);
    }
}
