package fitaview.tree;

import java.util.Collections;

class TreeXmlBuilder
{
    private TreeNode currentTree;
    private TreeXmlBuilder parent;
    private final StringBuilder body = new StringBuilder();

    TreeXmlBuilder()
    {
    }

    private TreeXmlBuilder(TreeNode currentTree, TreeXmlBuilder parent)
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

    TreeXmlBuilder build(TreeNode tree)
    {
        if(isNull(tree))
            return this;

        TreeXmlBuilder builder = startTree(tree);

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

    private TreeXmlBuilder startTree(TreeNode tree)
    {
        return new TreeXmlBuilder(tree, this);
    }

    private TreeXmlBuilder endTree()
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
