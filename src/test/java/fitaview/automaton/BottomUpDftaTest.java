package fitaview.automaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fitaview.automaton.traversing.TraversingMode;
import fitaview.tree.RecNode;
import fitaview.tree.RepeatNode;
import fitaview.tree.StandardNode;
import fitaview.tree.TreeNode;
import fitaview.tree.UndefinedStateValueException;
import fitaview.utils.Pair;
import fitaview.utils.Triple;

public class BottomUpDftaTest
{
    private BottomUpDfta testObject;
    private final List<Variable> variables =
            Arrays.asList(new Variable(1, "X", "T", "F"), new Variable(2, "#", "!", "@", "$", "&"));
    private final List<String> alphabet = Arrays.asList("0", "1", "and", "or", "impl");
    private final Map<Variable, Pair<String, Boolean>> accepts = new HashMap<>();

    public BottomUpDftaTest()
            throws Exception
    {
        accepts.put(variables.get(0), Pair.make("T", true));
        accepts.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new BottomUpDfta(variables, alphabet);
        testObject.addTransition(variables.get(0), "X", "X", "0", "F");
        testObject.addTransition(variables.get(0), "X", "X", "1", "T");
        testObject.addTransition(variables.get(0), "T", "T", "and", "T");
        testObject.addTransition(variables.get(0), "F", Wildcard.EVERY_VALUE, "and",
                                 Wildcard.LEFT_VALUE);
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "F", "and",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "or",
                                 Wildcard.LEFT_VALUE);
        testObject.addTransition(variables.get(0), Wildcard.EVERY_VALUE, "T", "or",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "F", "F", "or", "F");
        testObject.addTransition(variables.get(0), "T", Wildcard.EVERY_VALUE, "impl",
                                 Wildcard.RIGHT_VALUE);
        testObject.addTransition(variables.get(0), "F", Wildcard.EVERY_VALUE, "impl", "T");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "0",
                                 "!");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "1",
                                 "!");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                 "and", "&");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "or",
                                 "$");
        testObject.addTransition(variables.get(1), Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE,
                                 "impl", "@");
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void getTypeName_ThenFullName()
    {
        // when
        String result = testObject.getTypeName();

        // then
        Assertions.assertThat(result).isEqualTo("Bottom-up deterministic finite tree automaton");
    }

    @Test
    public void setTree_WhenFiniteTree_ThenTree()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("and", 1, new StandardNode("1", 3),
                                         new StandardNode("or", 2, new StandardNode("0", 5),
                                                          new StandardNode("1", 4)));

        // when
        testObject.setTree(node);

        // then
        Assertions.assertThat(testObject.tree).isNotNull().isSameAs(node);
    }

    @Test
    public void setTree_WhenEmptyTree_ThenNull()
            throws Exception
    {
        // when
        testObject.setTree(null);

        // then
        Assertions.assertThat(testObject.tree).isNull();
    }

    @Test
    public void setTree_WhenInfiniteTree_ThenTreeFinitenessException()
            throws Exception
    {
        // given
        RepeatNode node2 = new RepeatNode("0", 2);
        TreeNode node4 = new StandardNode("1", 4);
        TreeNode node5 =
                new StandardNode("3", 5, new StandardNode("1", 11), new RecNode(node2, 10));
        TreeNode node1 = new StandardNode("2", 1, new StandardNode("0", 3), node2);

        node2.setLeft(node5);
        node2.setRight(node4);

        // then
        Assertions.assertThatThrownBy(() -> testObject.setTree(node1))
                  .isInstanceOf(TreeFinitenessException.class);
    }

    @Test
    public void run_ThenTraversedToEnd()
            throws Exception
    {
        // given
        TreeNode node13 = new StandardNode("1", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("1", 11);
        TreeNode node10 = new StandardNode("0", 10);
        TreeNode node7 = new StandardNode("1", 7);
        TreeNode node6 = new StandardNode("or", 6, node13, node12);
        TreeNode node5 = new StandardNode("and", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("and", 3, node7, node6);
        TreeNode node2 = new StandardNode("or", 2, node5, node4);
        TreeNode node1 = new StandardNode("impl", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.run();

        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("@");
    }

    @Test
    public void run_WhenNoTraversing_ThenNoTraversingStrategyException()
    {
        // given
        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // then
        Assertions.assertThatThrownBy(() -> testObject.run())
                  .isInstanceOf(NoTraversingStrategyException.class);
    }

    @Test
    public void run_WhenNoTree_ThenNoTreeException()
            throws Exception
    {
        // given
        testObject.setTraversing(TraversingMode.LEVEL);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // then
        Assertions.assertThatThrownBy(() -> testObject.run()).isInstanceOf(NoTreeException.class);
    }

    @Test
    public void makeStepForward_ThenSingleStep()
            throws Exception
    {
        // given
        TreeNode node13 = new StandardNode("1", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("1", 11);
        TreeNode node10 = new StandardNode("0", 10);
        TreeNode node7 = new StandardNode("1", 7);
        TreeNode node6 = new StandardNode("or", 6, node13, node12);
        TreeNode node5 = new StandardNode("and", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("and", 3, node7, node6);
        TreeNode node2 = new StandardNode("or", 2, node5, node4);
        TreeNode node1 = new StandardNode("impl", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isNull();
    }

    @Test
    public void makeStepForward_WhenMultipleCalls_ThenConsecutiveSteps()
            throws Exception
    {
        // given
        TreeNode node13 = new StandardNode("1", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("1", 11);
        TreeNode node10 = new StandardNode("0", 10);
        TreeNode node7 = new StandardNode("1", 7);
        TreeNode node6 = new StandardNode("or", 6, node13, node12);
        TreeNode node5 = new StandardNode("and", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("and", 3, node7, node6);
        TreeNode node2 = new StandardNode("or", 2, node5, node4);
        TreeNode node1 = new StandardNode("impl", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isNull();

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isNull();

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isNull();

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("@");
    }

    @Test
    public void makeStepForward_WhenNoTraversing_ThenNoTraversingStrategyException()
    {
        // given
        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // then
        Assertions.assertThatThrownBy(() -> testObject.makeStepForward())
                  .isInstanceOf(NoTraversingStrategyException.class);
    }

    @Test
    public void makeStepForward_WhenNoTree_ThenNoTreeException()
            throws Exception
    {
        // given
        testObject.setTraversing(TraversingMode.LEVEL);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // then
        Assertions.assertThatThrownBy(() -> testObject.makeStepForward())
                  .isInstanceOf(NoTreeException.class);
    }

    @Test
    public void makeStepForward_run_ThenTraversedToEnd()
            throws Exception
    {
        // given
        TreeNode node13 = new StandardNode("1", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("1", 11);
        TreeNode node10 = new StandardNode("0", 10);
        TreeNode node7 = new StandardNode("1", 7);
        TreeNode node6 = new StandardNode("or", 6, node13, node12);
        TreeNode node5 = new StandardNode("and", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("and", 3, node7, node6);
        TreeNode node2 = new StandardNode("or", 2, node5, node4);
        TreeNode node1 = new StandardNode("impl", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isNull();

        // when
        testObject.run();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("T");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("&");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("F");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("@");
    }

    @Test
    public void isAccepted_WhenAutomatonHasRunAndAccepts_ThenTrue()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                     new StandardNode("1", 7, null,
                                                                                      null),
                                                                     new StandardNode("or", 6,
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                         new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                    new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                    new StandardNode(
                                                                                            "1", 10,
                                                                                            null,
                                                                                            null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node);
        testObject.run();

        // when
        boolean result = testObject.isAccepted();

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void isAccepted_WhenAutomatonHasRunAndNotAccepts_ThenFalse()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                     new StandardNode("1", 7, null,
                                                                                      null),
                                                                     new StandardNode("or", 6,
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                         new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                    new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                    new StandardNode(
                                                                                            "0", 10,
                                                                                            null,
                                                                                            null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node);
        testObject.run();

        // when
        boolean result = testObject.isAccepted();

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void isAccepted_WhenAutomatonHasNotRun_ThenUndefinedStateValueException()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                     new StandardNode("1", 7, null,
                                                                                      null),
                                                                     new StandardNode("or", 6,
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                         new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                    new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                    new StandardNode(
                                                                                            "0", 10,
                                                                                            null,
                                                                                            null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts);
        testObject.setTree(node);

        // then
        Assertions.assertThatThrownBy(() -> testObject.isAccepted())
                  .isInstanceOf(UndefinedStateValueException.class);
    }

    @Test
    public void isAccepted_WhenAutomatonHasNoAcceptingStates_ThenUndefinedAcceptanceException()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("impl", 1, new StandardNode("and", 3,
                                                                     new StandardNode("1", 7, null,
                                                                                      null),
                                                                     new StandardNode("or", 6,
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              13,
                                                                                              null,
                                                                                              null),
                                                                                      new StandardNode(
                                                                                              "1",
                                                                                              12,
                                                                                              null,
                                                                                              null))),
                                         new StandardNode("or", 2, new StandardNode("and", 5,
                                                                                    new StandardNode(
                                                                                            "1", 11,
                                                                                            null,
                                                                                            null),
                                                                                    new StandardNode(
                                                                                            "1", 10,
                                                                                            null,
                                                                                            null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.setTree(node);
        testObject.run();

        // then
        Assertions.assertThatThrownBy(() -> testObject.isAccepted())
                  .isInstanceOf(UndefinedAcceptanceException.class);
    }

    @Test
    public void isAccepted_WhenAutomatonHasEmptyTree_ThenNoTreeException()
    {
        Assertions.assertThatThrownBy(() -> testObject.isAccepted())
                  .isInstanceOf(NoTreeException.class);
    }

    @Test
    public void isInAlphabet_WhenValueInAlphabet_ThenTrue()
    {
        // when
        boolean result0 = testObject.isInAlphabet("0");
        boolean result1 = testObject.isInAlphabet("1");
        boolean resultAnd = testObject.isInAlphabet("and");
        boolean resultOr = testObject.isInAlphabet("or");
        boolean resultImpl = testObject.isInAlphabet("impl");

        // then
        Assertions.assertThat(result0).isTrue();
        Assertions.assertThat(result1).isTrue();
        Assertions.assertThat(resultAnd).isTrue();
        Assertions.assertThat(resultOr).isTrue();
        Assertions.assertThat(resultImpl).isTrue();
    }

    @Test
    public void isInAlphabet_WhenValueOutOfAlphabet_ThenFalse()
    {
        // when
        boolean result2 = testObject.isInAlphabet("2");
        boolean resultIff = testObject.isInAlphabet("iff");

        // then
        Assertions.assertThat(result2).isFalse();
        Assertions.assertThat(resultIff).isFalse();
    }

    @Test
    public void getTransitionWithStrings_ThenTransitionsMap()
    {
        // when
        Map<Pair<Variable, String>, String> result = testObject.getTransitionAsStrings();

        // then
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(
                Pair.make(variables.get(0), testObject.keyToString(Triple.make("X", "X", "0"))),
                testObject.valueToString("F"));
        expected.put(
                Pair.make(variables.get(0), testObject.keyToString(Triple.make("X", "X", "1"))),
                testObject.valueToString("T"));
        expected.put(
                Pair.make(variables.get(0), testObject.keyToString(Triple.make("T", "T", "and"))),
                testObject.valueToString("T"));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                             Triple.make("F", Wildcard.EVERY_VALUE, "and"))),
                     testObject.valueToString(Wildcard.LEFT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, "F", "and"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                             Triple.make("T", Wildcard.EVERY_VALUE, "or"))),
                     testObject.valueToString(Wildcard.LEFT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, "T", "or"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(
                Pair.make(variables.get(0), testObject.keyToString(Triple.make("F", "F", "or"))),
                testObject.valueToString("F"));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                             Triple.make("T", Wildcard.EVERY_VALUE, "impl"))),
                     testObject.valueToString(Wildcard.RIGHT_VALUE));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(
                Triple.make("F", Wildcard.EVERY_VALUE, "impl"))), testObject.valueToString("T"));

        expected.put(Pair.make(variables.get(1), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "0"))),
                     testObject.valueToString("!"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "1"))),
                     testObject.valueToString("!"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "and"))),
                     testObject.valueToString("&"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "or"))),
                     testObject.valueToString("$"));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(
                             Triple.make(Wildcard.EVERY_VALUE, Wildcard.EVERY_VALUE, "impl"))),
                     testObject.valueToString("@"));

        Assertions.assertThat(result).isNotNull().containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    public void checkEmptiness_WhenNotEmpty_ThenFalse()
            throws Exception
    {
        // given
        testObject.addAcceptanceConditions(accepts);

        // when
        boolean result = testObject.checkEmptiness();

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void checkEmptiness_WhenEmpty_ThenTrue()
            throws Exception
    {
        // given
        Map<Variable, Pair<String, Boolean>> testAccepts = new HashMap<>();

        testAccepts.put(variables.get(0), Pair.make("X", true));
        testAccepts.put(variables.get(1), Pair.make(Wildcard.EVERY_VALUE, true));

        testObject.addAcceptanceConditions(testAccepts);

        // when
        boolean result = testObject.checkEmptiness();

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void checkEmptiness_WhenNoAcceptance_ThenUndefinedAcceptanceException()
    {
        Assertions.assertThatThrownBy(() -> testObject.checkEmptiness())
                  .isInstanceOf(UndefinedAcceptanceException.class);
    }
}
