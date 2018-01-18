package ref_humbold.fita_view.viewer.tree;

import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import ref_humbold.fita_view.Pair;

@RunWith(MockitoJUnitRunner.class)
public class MovingButtonsPanelTest
{
    @Mock
    private TreeDrawingArea mockDrawingArea;

    @Mock
    private ActionEvent mockActionEvent;

    @InjectMocks
    private MovingButtonsPanel testObject;

    @Before
    public void setUp()
    {
        Mockito.doNothing().when(mockDrawingArea).repaint();
        Mockito.doCallRealMethod()
               .when(mockDrawingArea)
               .moveArea(Matchers.anyInt(), Matchers.anyInt());
        Mockito.doCallRealMethod().when(mockDrawingArea).centralize();
        Mockito.doCallRealMethod().when(mockDrawingArea).getAxisPoint();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testActionPerformedWhenCentre()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CENTRE");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(1)).centralize();
        order.verify(mockDrawingArea, Mockito.times(1)).repaint();
        Assert.assertEquals(new Integer(0), result.getFirst());
        Assert.assertEquals(new Integer(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenUp()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("UP");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, MovingButtonsPanel.STEP);
        order.verify(mockDrawingArea, Mockito.times(1)).repaint();
        Assert.assertEquals(new Integer(MovingButtonsPanel.STEP), result.getFirst());
        Assert.assertEquals(new Integer(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenDown()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DOWN");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, -MovingButtonsPanel.STEP);
        order.verify(mockDrawingArea, Mockito.times(1)).repaint();
        Assert.assertEquals(new Integer(-MovingButtonsPanel.STEP), result.getFirst());
        Assert.assertEquals(new Integer(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenLeft()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEFT");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(1)).moveArea(MovingButtonsPanel.STEP, 0);
        order.verify(mockDrawingArea, Mockito.times(1)).repaint();
        Assert.assertEquals(new Integer(0), result.getFirst());
        Assert.assertEquals(new Integer(MovingButtonsPanel.STEP), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenRight()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RIGHT");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(1)).moveArea(-MovingButtonsPanel.STEP, 0);
        order.verify(mockDrawingArea, Mockito.times(1)).repaint();
        Assert.assertEquals(new Integer(0), result.getFirst());
        Assert.assertEquals(new Integer(-MovingButtonsPanel.STEP), result.getSecond());
    }
}
