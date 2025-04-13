package fitaview.tree;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

public class TreeWriterTest
{
    private TreeWriter testObject;

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void toString_WhenEmptyTree_ThenEmptyXml()
    {
        // given
        testObject = new TreeWriter(null);

        // when
        String result = testObject.toString();

        // then
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void toString_WhenSimpleTree_ThenXml()
            throws Exception
    {
        // given
        TreeNode tree = new StandardNode("1", 0, new StandardNode("2", 0, new StandardNode("3", 0),
                                                                  new StandardNode("4", 0)),
                                         new StandardNode("5", 0));

        testObject = new TreeWriter(tree);

        // when
        String result = testObject.toString();

        // then
        String expected = """
                <node label="1">
                  <node label="2">
                    <node label="3" />
                    <node label="4" />
                  </node>
                  <node label="5" />
                </node>
                """;

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    public void toString_WhenRepeat()
            throws Exception
    {
        // given
        RepeatNode repeat = new RepeatNode("5", 0);

        repeat.setLeft(new StandardNode("6", 0));
        repeat.setRight(new StandardNode("7", 0, new RecNode(repeat, 0), new StandardNode("9", 0)));

        TreeNode tree = new StandardNode("1", 0, new StandardNode("2", 0, new StandardNode("3", 0),
                                                                  new StandardNode("4", 0)),
                                         repeat);

        testObject = new TreeWriter(tree);

        // when
        String result = testObject.toString();

        // then
        String expected = """
                <node label="1">
                  <node label="2">
                    <node label="3" />
                    <node label="4" />
                  </node>
                  <repeat label="5">
                    <node label="6" />
                    <node label="7">
                      <rec />
                      <node label="9" />
                    </node>
                  </repeat>
                </node>
                """;

        Assertions.assertThat(result).isEqualTo(expected);
    }
}
