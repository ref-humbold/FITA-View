package fitaview.automaton;

import java.util.ArrayList;
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

public class TopDownDftaTest
{
    private TopDownDfta testObject;
    private final List<Variable> variables;
    private final List<String> alphabet = Arrays.asList("0", "1", "2", "3", "4");
    private final List<Map<Variable, Pair<String, Boolean>>> accepts =
            Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>());

    public TopDownDftaTest()
            throws Exception
    {
        variables = Arrays.asList(new Variable(1, "A", "B"), new Variable(2, "!", "@", "#", "$"));
        accepts.get(0).put(variables.get(0), Pair.make("A", true));
        accepts.get(0).put(variables.get(1), Pair.make("@", true));
        accepts.get(1).put(variables.get(0), Pair.make("B", true));
        accepts.get(1).put(variables.get(1), Pair.make("$", true));
        accepts.get(2).put(variables.get(0), Pair.make(Wildcard.EVERY_VALUE, true));
        accepts.get(2).put(variables.get(1), Pair.make("#", true));
    }

    @Before
    public void setUp()
            throws Exception
    {
        testObject = new TopDownDfta(variables, alphabet);
        testObject.addTransition(variables.get(0), "A", "0", "A", "B");
        testObject.addTransition(variables.get(0), "A", "1", "A", "A");
        testObject.addTransition(variables.get(0), "A", "2", "B", "B");
        testObject.addTransition(variables.get(0), "A", "3", "A", "A");
        testObject.addTransition(variables.get(0), "A", "4", "B", "B");
        testObject.addTransition(variables.get(0), "B", "0", "B", "A");
        testObject.addTransition(variables.get(0), "B", "1", "B", "B");
        testObject.addTransition(variables.get(0), "B", "2", "A", "A");
        testObject.addTransition(variables.get(0), "B", "3", "B", "B");
        testObject.addTransition(variables.get(0), "B", "4", "A", "A");
        testObject.addTransition(variables.get(1), "!", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "!", "1", "!", "@");
        testObject.addTransition(variables.get(1), "!", "2", "@", "#");
        testObject.addTransition(variables.get(1), "!", "3", "#", "$");
        testObject.addTransition(variables.get(1), "!", "4", "$", "!");
        testObject.addTransition(variables.get(1), "@", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "@", "1", "@", "#");
        testObject.addTransition(variables.get(1), "@", "2", "#", "$");
        testObject.addTransition(variables.get(1), "@", "3", "$", "!");
        testObject.addTransition(variables.get(1), "@", "4", "!", "@");
        testObject.addTransition(variables.get(1), "#", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "#", "1", "#", "$");
        testObject.addTransition(variables.get(1), "#", "2", "$", "!");
        testObject.addTransition(variables.get(1), "#", "3", "!", "@");
        testObject.addTransition(variables.get(1), "#", "4", "@", "#");
        testObject.addTransition(variables.get(1), "$", "0", Wildcard.SAME_VALUE,
                                 Wildcard.SAME_VALUE);
        testObject.addTransition(variables.get(1), "$", "1", "$", "!");
        testObject.addTransition(variables.get(1), "$", "2", "!", "@");
        testObject.addTransition(variables.get(1), "$", "3", "@", "#");
        testObject.addTransition(variables.get(1), "$", "4", "#", "$");
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
        Assertions.assertThat(result).isEqualTo("Top-down deterministic finite tree automaton");
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
        TreeNode node13 = new StandardNode("2", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("0", 11);
        TreeNode node10 = new StandardNode("4", 10);
        TreeNode node7 = new StandardNode("3", 7);
        TreeNode node6 = new StandardNode("3", 6, node13, node12);
        TreeNode node5 = new StandardNode("2", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("4", 3, node7, node6);
        TreeNode node2 = new StandardNode("1", 2, node5, node4);
        TreeNode node1 = new StandardNode("2", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.run();

        // then
        List<Map<Variable, String>> expectedLeafStates = new ArrayList<>();

        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(0).put(variables.get(0), "A");
        expectedLeafStates.get(0).put(variables.get(1), "#");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(1).put(variables.get(0), "A");
        expectedLeafStates.get(1).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(2).put(variables.get(0), "B");
        expectedLeafStates.get(2).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(3).put(variables.get(0), "A");
        expectedLeafStates.get(3).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(4).put(variables.get(0), "B");
        expectedLeafStates.get(4).put(variables.get(1), "!");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(5).put(variables.get(0), "B");
        expectedLeafStates.get(5).put(variables.get(1), "@");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(6).put(variables.get(0), "A");
        expectedLeafStates.get(6).put(variables.get(1), "!");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(7).put(variables.get(0), "A");
        expectedLeafStates.get(7).put(variables.get(1), "@");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(8).put(variables.get(0), "A");
        expectedLeafStates.get(8).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(9).put(variables.get(0), "B");
        expectedLeafStates.get(9).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(10).put(variables.get(0), "B");
        expectedLeafStates.get(10).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(11).put(variables.get(0), "B");
        expectedLeafStates.get(11).put(variables.get(1), "!");

        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(testObject.leafStates)
                  .containsExactlyInAnyOrderElementsOf(expectedLeafStates);
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
        TreeNode node13 = new StandardNode("2", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("0", 11);
        TreeNode node10 = new StandardNode("4", 10);
        TreeNode node7 = new StandardNode("3", 7);
        TreeNode node6 = new StandardNode("3", 6, node13, node12);
        TreeNode node5 = new StandardNode("2", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("4", 3, node7, node6);
        TreeNode node2 = new StandardNode("1", 2, node5, node4);
        TreeNode node1 = new StandardNode("2", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(testObject.leafStates).isEmpty();
    }

    @Test
    public void makeStepForward_WhenMultipleCalls_ThenConsecutiveSteps()
            throws Exception
    {
        TreeNode node13 = new StandardNode("2", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("0", 11);
        TreeNode node10 = new StandardNode("4", 10);
        TreeNode node7 = new StandardNode("3", 7);
        TreeNode node6 = new StandardNode("3", 6, node13, node12);
        TreeNode node5 = new StandardNode("2", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("4", 3, node7, node6);
        TreeNode node2 = new StandardNode("1", 2, node5, node4);
        TreeNode node1 = new StandardNode("2", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(testObject.leafStates).isEmpty();

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(testObject.leafStates).isEmpty();

        // when
        testObject.makeStepForward();

        // then
        List<Map<Variable, String>> expectedLeafStates1 = new ArrayList<>();

        expectedLeafStates1.add(new HashMap<>());
        expectedLeafStates1.get(0).put(variables.get(0), "A");
        expectedLeafStates1.get(0).put(variables.get(1), "#");
        expectedLeafStates1.add(new HashMap<>());
        expectedLeafStates1.get(1).put(variables.get(0), "A");
        expectedLeafStates1.get(1).put(variables.get(1), "$");
        expectedLeafStates1.add(new HashMap<>());
        expectedLeafStates1.get(2).put(variables.get(0), "B");
        expectedLeafStates1.get(2).put(variables.get(1), "$");
        expectedLeafStates1.add(new HashMap<>());
        expectedLeafStates1.get(3).put(variables.get(0), "A");
        expectedLeafStates1.get(3).put(variables.get(1), "$");

        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(testObject.leafStates)
                  .containsExactlyInAnyOrderElementsOf(expectedLeafStates1);

        // when
        testObject.makeStepForward();

        // then
        List<Map<Variable, String>> expectedLeafStates2 = new ArrayList<>();

        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(0).put(variables.get(0), "A");
        expectedLeafStates2.get(0).put(variables.get(1), "#");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(1).put(variables.get(0), "A");
        expectedLeafStates2.get(1).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(2).put(variables.get(0), "B");
        expectedLeafStates2.get(2).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(3).put(variables.get(0), "A");
        expectedLeafStates2.get(3).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(4).put(variables.get(0), "B");
        expectedLeafStates2.get(4).put(variables.get(1), "!");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(5).put(variables.get(0), "B");
        expectedLeafStates2.get(5).put(variables.get(1), "@");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(6).put(variables.get(0), "A");
        expectedLeafStates2.get(6).put(variables.get(1), "!");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(7).put(variables.get(0), "A");
        expectedLeafStates2.get(7).put(variables.get(1), "@");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(8).put(variables.get(0), "A");
        expectedLeafStates2.get(8).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(9).put(variables.get(0), "B");
        expectedLeafStates2.get(9).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(10).put(variables.get(0), "B");
        expectedLeafStates2.get(10).put(variables.get(1), "$");
        expectedLeafStates2.add(new HashMap<>());
        expectedLeafStates2.get(11).put(variables.get(0), "B");
        expectedLeafStates2.get(11).put(variables.get(1), "!");

        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(testObject.leafStates)
                  .containsExactlyInAnyOrderElementsOf(expectedLeafStates2);
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
        TreeNode node13 = new StandardNode("2", 13);
        TreeNode node12 = new StandardNode("1", 12);
        TreeNode node11 = new StandardNode("0", 11);
        TreeNode node10 = new StandardNode("4", 10);
        TreeNode node7 = new StandardNode("3", 7);
        TreeNode node6 = new StandardNode("3", 6, node13, node12);
        TreeNode node5 = new StandardNode("2", 5, node11, node10);
        TreeNode node4 = new StandardNode("0", 4);
        TreeNode node3 = new StandardNode("4", 3, node7, node6);
        TreeNode node2 = new StandardNode("1", 2, node5, node4);
        TreeNode node1 = new StandardNode("2", 1, node3, node2);

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
        testObject.setTree(node1);

        Assumptions.assumeThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.STOPPED);

        // when
        testObject.makeStepForward();

        // then
        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.RUNNING);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isNull();
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isNull();
        Assertions.assertThat(testObject.leafStates).isEmpty();

        // when
        testObject.run();

        // then
        List<Map<Variable, String>> expectedLeafStates = new ArrayList<>();

        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(0).put(variables.get(0), "A");
        expectedLeafStates.get(0).put(variables.get(1), "#");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(1).put(variables.get(0), "A");
        expectedLeafStates.get(1).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(2).put(variables.get(0), "B");
        expectedLeafStates.get(2).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(3).put(variables.get(0), "A");
        expectedLeafStates.get(3).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(4).put(variables.get(0), "B");
        expectedLeafStates.get(4).put(variables.get(1), "!");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(5).put(variables.get(0), "B");
        expectedLeafStates.get(5).put(variables.get(1), "@");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(6).put(variables.get(0), "A");
        expectedLeafStates.get(6).put(variables.get(1), "!");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(7).put(variables.get(0), "A");
        expectedLeafStates.get(7).put(variables.get(1), "@");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(8).put(variables.get(0), "A");
        expectedLeafStates.get(8).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(9).put(variables.get(0), "B");
        expectedLeafStates.get(9).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(10).put(variables.get(0), "B");
        expectedLeafStates.get(10).put(variables.get(1), "$");
        expectedLeafStates.add(new HashMap<>());
        expectedLeafStates.get(11).put(variables.get(0), "B");
        expectedLeafStates.get(11).put(variables.get(1), "!");

        Assertions.assertThat(testObject.runningMode).isEqualTo(AutomatonRunningMode.FINISHED);
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node1.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node2.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node3.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node4.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(0))).isEqualTo("B");
        Assertions.assertThat(node5.getStateValueOrNull(variables.get(1))).isEqualTo("#");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node6.getStateValueOrNull(variables.get(1))).isEqualTo("@");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node7.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node10.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node11.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node12.getStateValueOrNull(variables.get(1))).isEqualTo("!");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(0))).isEqualTo("A");
        Assertions.assertThat(node13.getStateValueOrNull(variables.get(1))).isEqualTo("$");
        Assertions.assertThat(testObject.leafStates)
                  .containsExactlyInAnyOrderElementsOf(expectedLeafStates);
    }

    @Test
    public void isAccepted_WhenAutomatonHasRunAndAccepts_ThenTrue()
            throws Exception
    {
        // given
        TreeNode node = new StandardNode("0", 1, new StandardNode("3", 3, new StandardNode("0", 7),
                                                                  new StandardNode("4", 6)),
                                         new StandardNode("2", 2));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
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
        TreeNode node = new StandardNode("2", 1, new StandardNode("4", 3, new StandardNode("3", 7),
                                                                  new StandardNode("3", 6,
                                                                                   new StandardNode(
                                                                                           "2", 13,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "1", 12,
                                                                                           null,
                                                                                           null))),
                                         new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                   new StandardNode(
                                                                                           "0", 11,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "4", 10,
                                                                                           null,
                                                                                           null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
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
        TreeNode node = new StandardNode("2", 1, new StandardNode("4", 3, new StandardNode("3", 7),
                                                                  new StandardNode("3", 6,
                                                                                   new StandardNode(
                                                                                           "2", 13,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "1", 12,
                                                                                           null,
                                                                                           null))),
                                         new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                   new StandardNode(
                                                                                           "0", 11,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "4", 10,
                                                                                           null,
                                                                                           null)),
                                                          new StandardNode("0", 4)));

        testObject.setTraversing(TraversingMode.LEVEL);
        testObject.addAcceptanceConditions(accepts.get(0));
        testObject.addAcceptanceConditions(accepts.get(1));
        testObject.addAcceptanceConditions(accepts.get(2));
        testObject.setTree(node);

        // then
        Assertions.assertThatThrownBy(() -> testObject.isAccepted())
                  .isInstanceOf(UndefinedStateValueException.class);
    }

    @Test
    public void isAccepted_WhenAutomatonHasNoAcceptingStates_ThenUndefinedAcceptanceException()
            throws UndefinedAcceptanceException, Exception
    {
        // given
        TreeNode node = new StandardNode("2", 1, new StandardNode("4", 3, new StandardNode("3", 7),
                                                                  new StandardNode("3", 6,
                                                                                   new StandardNode(
                                                                                           "2", 13,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "1", 12,
                                                                                           null,
                                                                                           null))),
                                         new StandardNode("1", 2, new StandardNode("2", 5,
                                                                                   new StandardNode(
                                                                                           "0", 11,
                                                                                           null,
                                                                                           null),
                                                                                   new StandardNode(
                                                                                           "4", 10,
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
        boolean result2 = testObject.isInAlphabet("2");
        boolean result3 = testObject.isInAlphabet("3");
        boolean result4 = testObject.isInAlphabet("4");

        // then
        Assertions.assertThat(result0).isTrue();
        Assertions.assertThat(result1).isTrue();
        Assertions.assertThat(result2).isTrue();
        Assertions.assertThat(result3).isTrue();
        Assertions.assertThat(result4).isTrue();
    }

    @Test
    public void isInAlphabet_WhenValueOutOfAlphabet_ThenFalse()
    {
        // when
        boolean result5 = testObject.isInAlphabet("5");
        boolean resultA = testObject.isInAlphabet("A");

        // then
        Assertions.assertThat(result5).isFalse();
        Assertions.assertThat(resultA).isFalse();
    }

    @Test
    public void getTransitionWithStrings_ThenTransitionsMap()
    {
        // when
        Map<Pair<Variable, String>, String> result = testObject.getTransitionAsStrings();

        // then
        Map<Pair<Variable, String>, String> expected = new HashMap<>();

        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "0"))),
                     testObject.valueToString(Pair.make("A", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "1"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "2"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "3"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("A", "4"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "0"))),
                     testObject.valueToString(Pair.make("B", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "1"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "2"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "3"))),
                     testObject.valueToString(Pair.make("B", "B")));
        expected.put(Pair.make(variables.get(0), testObject.keyToString(Pair.make("B", "4"))),
                     testObject.valueToString(Pair.make("A", "A")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "1"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "2"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "3"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("!", "4"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "1"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "2"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "3"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("@", "4"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "1"))),
                     testObject.valueToString(Pair.make("#", "$")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "2"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "3"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("#", "4"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "0"))),
                     testObject.valueToString(Pair.make(Wildcard.SAME_VALUE, Wildcard.SAME_VALUE)));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "1"))),
                     testObject.valueToString(Pair.make("$", "!")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "2"))),
                     testObject.valueToString(Pair.make("!", "@")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "3"))),
                     testObject.valueToString(Pair.make("@", "#")));
        expected.put(Pair.make(variables.get(1), testObject.keyToString(Pair.make("$", "4"))),
                     testObject.valueToString(Pair.make("#", "$")));

        Assertions.assertThat(result).isNotNull().containsExactlyInAnyOrderEntriesOf(expected);
    }
}
