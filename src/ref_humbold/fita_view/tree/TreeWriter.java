package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class TreeWriter
{
    private TreeVertex tree;
    private TreeWriter parent;
    private StringBuilder body = new StringBuilder();

    public TreeWriter()
    {
    }

    private TreeWriter(TreeVertex tree, TreeWriter parent)
    {
        this.tree = tree;
        this.parent = parent;
    }

    public TreeWriter write(TreeVertex tree)
    {
        if(tree == null)
            return this;

        TreeWriter writer = startTree(tree);

        if(!isRec(tree) && hasChildren(tree))
            writer = writer.write(tree.getLeft()).write(tree.getRight());

        return writer.endTree();
    }

    public void toFile(String filename)
        throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(filename));

        fileWriter.write(toString());
        fileWriter.close();
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        if(tree == null)
            return body.toString();

        output.append("<");
        output.append(tree.getTypename());

        if(!isRec(tree))
        {
            output.append(" label=\"");
            output.append(tree.getLabel());
            output.append("\"");
        }

        if(hasChildren(tree))
        {
            output.append(">");
            output.append(indentBody().toString());
            output.append("</");
            output.append(tree.getTypename());
            output.append(">\n");
        }
        else
            output.append(" />\n");

        return output.toString();
    }

    private StringBuilder indentBody()
    {
        String indentString = String.join("", Collections.nCopies(2, " "));
        StringBuilder indented = new StringBuilder("\n" + body.toString().trim());
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

    private TreeWriter startTree(TreeVertex tree)
    {
        return new TreeWriter(tree, this);
    }

    private TreeWriter endTree()
    {
        parent.addContent(toString());

        return parent;
    }

    private void addContent(String content)
    {
        body.append(content);
    }

    private boolean isRec(TreeVertex tree)
    {
        return tree.getTypename().equals("rec");
    }

    private boolean hasChildren(TreeVertex tree)
    {
        return !isRec(tree) && tree.getLeft() != null && tree.getRight() != null;
    }
}
