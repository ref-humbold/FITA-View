package fitaview.tree;

import java.nio.file.Path;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;

import fitaview.utils.Pair;

public class TreeReaderTest
{
    private static final String DIRECTORY = "src/test/resources/TreeReaderTest/";
    private TreeReader testObject;

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void read_WhenFiniteTree_ThenTree()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenFiniteTree_ThenTree.tree.xml");

        testObject = new TreeReader(path.toFile());

        // when
        Pair<TreeNode, Integer> result = testObject.read();

        // then
        var expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)),
                                        new StandardNode("5", 2));

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirst()).isEqualTo(expected);
        Assertions.assertThat(result.getSecond()).isEqualTo(3);
    }

    @Test
    public void read_WhenSingleRepeat_ThenTree()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenSingleRepeat_ThenTree.tree.xml");

        testObject = new TreeReader(path.toFile());

        // when
        Pair<TreeNode, Integer> result = testObject.read();

        // then
        var repeat = new RepeatNode("5", 2);

        repeat.setLeft(new StandardNode("6", 5));
        repeat.setRight(new StandardNode("7", 4, new RecNode(repeat, 9), new StandardNode("9", 8)));

        var expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)), repeat);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirst()).isEqualTo(expected);
        Assertions.assertThat(result.getSecond()).isEqualTo(4);
    }

    @Test
    public void read_WhenNestedRepeats_ThenTree()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenNestedRepeats_ThenTree.tree.xml");

        testObject = new TreeReader(path.toFile());

        // when
        Pair<TreeNode, Integer> result = testObject.read();

        // then
        var repeat5 = new RepeatNode("5", 2);
        var repeat7 = new RepeatNode("7", 11);

        repeat7.setLeft(
                new StandardNode("8", 23, new RecNode(repeat7, 47), new RecNode(repeat7, 46)));
        repeat7.setRight(new StandardNode("11", 22));

        repeat5.setLeft(new StandardNode("6", 5, repeat7, new RecNode(repeat5, 10)));
        repeat5.setRight(
                new StandardNode("13", 4, new RecNode(repeat5, 9), new StandardNode("15", 8)));

        var expected = new StandardNode("1", 1, new StandardNode("2", 3, new StandardNode("3", 7),
                                                                 new StandardNode("4", 6)),
                                        repeat5);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getFirst()).isEqualTo(expected);
        Assertions.assertThat(result.getSecond()).isEqualTo(6);
    }

    @Test
    public void read_WhenRecOutOfScope_ThenTreeParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenRecOutOfScope_ThenTreeParsingException.tree.xml");

        testObject = new TreeReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(TreeParsingException.class);
    }

    @Test
    public void read_WhenOneChild_ThenInvalidChildrenException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenOneChild_ThenInvalidChildrenException.tree.xml");

        testObject = new TreeReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(InvalidChildrenException.class);
    }

    @Test
    public void read_WhenThreeChildren_ThenTreeParsingException()
            throws Exception
    {
        // given
        var path = Path.of(DIRECTORY, "read_WhenThreeChildren_ThenTreeParsingException.tree.xml");

        testObject = new TreeReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(TreeParsingException.class);
    }

    @Test
    public void read_WhenTreeDepthIsViolated_ThenTreeDepthException()
            throws Exception
    {
        // given
        var path =
                Path.of(DIRECTORY, "read_WhenTreeDepthIsViolated_ThenTreeDepthException.tree.xml");

        testObject = new TreeReader(path.toFile());

        // then
        Assertions.assertThatThrownBy(() -> testObject.read())
                  .isInstanceOf(TreeDepthException.class);
    }
}
