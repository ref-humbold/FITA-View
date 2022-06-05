package fitaview.tree;

import java.util.Collections;

class TreeXMLBuilder
{
    private TreeNode currentTree;
    private TreeXMLBuilder parent;
    private final StringBuilder body = new StringBuilder();

    TreeXMLBuilder()
    {
    }

    private TreeXMLBuilder(TreeNode currentTree, TreeXMLBuilder parent)
    {
        this.currentTree = currentTree;
        this.parent = parent;
    }

    static boolean isNull(TreeNode tree)
    {
        return tree == null || tree.isNull();
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        if(isNull(currentTree))
            return body.toString();

        output.append("<");
        output.append(getNodeName(currentTree));

        if(isNode(currentTree))
        {
            output.append(" label=\"");
            output.append(currentTree.getLabel());
            output.append("\"");
        }

        if(isNode(currentTree) && !currentTree.isLeaf())
        {
            output.append(">");
            output.append(indentBody());
            output.append("</");
            output.append(getNodeName(currentTree));
            output.append(">\n");
        }
        else
            output.append(" />\n");

        return output.toString();
    }

    TreeXMLBuilder build(TreeNode tree)
    {
        if(isNull(tree))
            return this;

        TreeXMLBuilder builder = startTree(tree);

        if(isNode(tree) && !tree.isLeaf())
            builder = builder.build(tree.getLeft()).build(tree.getRight());

        return builder.endTree();
    }

    private StringBuilder indentBody()
    {
        String indentString = String.join("", Collections.nCopies(2, " "));
        StringBuilder indented = new StringBuilder(String.format("\n%s", body.toString().trim()));
        int indexStart = 0;

        while(indexStart >= 0)
        {
            int newlineIndex = indented.indexOf("\n", indexStart);

            if(newlineIndex >= 0)
            {
                indented.insert(newlineIndex + 1, indentString);
                indexStart = newlineIndex + indentString.length();
            }
            else
            {
                indented.append("\n");
                indexStart = newlineIndex;
            }
        }

        return indented;
    }

    private TreeXMLBuilder startTree(TreeNode tree)
    {
        return new TreeXMLBuilder(tree, this);
    }

    private TreeXMLBuilder endTree()
    {
        parent.addContent(toString());

        return parent;
    }

    private void addContent(String content)
    {
        body.append(content);
    }

    private boolean isNode(TreeNode tree)
    {
        return tree.getType() == NodeType.NODE || tree.getType() == NodeType.REPEAT;
    }

    private String getNodeName(TreeNode tree)
    {
        return tree.getType().toString().toLowerCase();
    }
}
