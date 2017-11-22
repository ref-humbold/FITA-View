package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;

public class TreeWriter
{
    private TreeVertex tree;

    public TreeWriter(TreeVertex tree)
    {
        this.tree = tree;
    }

    public void toFile(String filename)
        throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(filename + ".tree"));

        fileWriter.write(toString());
        fileWriter.close();
    }

    @Override
    public String toString()
    {
        TreeXMLBuilder builder = new TreeXMLBuilder();

        if(tree == null)
            return "<null />";

        return builder.build(tree).toString();
    }

    private class TreeXMLBuilder
    {
        private TreeVertex tree;
        private TreeXMLBuilder parent;
        private StringBuilder body = new StringBuilder();

        TreeXMLBuilder()
        {
        }

        private TreeXMLBuilder(TreeVertex tree, TreeXMLBuilder parent)
        {
            this.tree = tree;
            this.parent = parent;
        }

        TreeXMLBuilder build(TreeVertex tree)
        {
            if(tree == null)
                return this;

            TreeXMLBuilder builder = startTree(tree);

            if(!isRec(tree) && hasChildren(tree))
                builder = builder.build(tree.getLeft()).build(tree.getRight());

            return builder.endTree();
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

        private TreeXMLBuilder startTree(TreeVertex tree)
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

        private boolean isRec(TreeVertex tree)
        {
            return tree.getTypename().equals("rec");
        }

        private boolean hasChildren(TreeVertex tree)
        {
            return !isRec(tree) && tree.getLeft() != null && tree.getRight() != null;
        }
    }
}
