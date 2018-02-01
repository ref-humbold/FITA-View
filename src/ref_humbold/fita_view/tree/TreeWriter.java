package ref_humbold.fita_view.tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TreeWriter
{
    private TreeNode tree;

    public TreeWriter(TreeNode tree)
    {
        this.tree = tree;
    }

    /**
     * Writing tree to XML file.
     * @param filename name of the file without extension
     * @throws IOException if any IO error occurs
     */
    public void toFile(String filename)
        throws IOException
    {
        FileWriter fileWriter = new FileWriter(new File(filename + ".tree"));

        fileWriter.write(toString());
        fileWriter.close();
    }

    /**
     * @return text version of tree as XML
     */
    @Override
    public String toString()
    {
        TreeXMLBuilder builder = new TreeXMLBuilder();

        if(TreeXMLBuilder.isNull(tree))
            return "";

        return builder.build(tree).toString();
    }
}
