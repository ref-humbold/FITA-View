package fitaview.viewer.tree;

import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import fitaview.Pair;

@RunWith(MockitoJUnitRunner.class)
public class MovingButtonsPanelTest
{
    @Mock private TreeDrawingArea mockDrawingArea;

    @Mock private ActionEvent mockActionEvent;

    @InjectMocks private MovingButtonsPanel testObject;

    @Before
    public void setUp()
    {
        Mockito.doNothing().when(mockDrawingArea).repaint();
        Mockito.doCallRealMethod()
               .when(mockDrawingArea)
               .moveArea(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        Mockito.doCallRealMethod().when(mockDrawingArea).centralize();
        Mockito.doCallRealMethod().when(mockDrawingArea).getAxisPoint();
        Mockito.doCallRealMethod().when(mockDrawingArea).getStepUnit();
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

        Mockito.verify(mockDrawingArea, Mockito.times(1)).centralize();
        Assert.assertEquals(Integer.valueOf(0), result.getFirst());
        Assert.assertEquals(Integer.valueOf(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenUp()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("UP");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, 1);
        Assert.assertEquals(Integer.valueOf(mockDrawingArea.getStepUnit()), result.getFirst());
        Assert.assertEquals(Integer.valueOf(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenDown()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DOWN");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, -1);
        Assert.assertEquals(Integer.valueOf(-mockDrawingArea.getStepUnit()), result.getFirst());
        Assert.assertEquals(Integer.valueOf(0), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenLeft()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEFT");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(1, 0);
        Assert.assertEquals(Integer.valueOf(0), result.getFirst());
        Assert.assertEquals(Integer.valueOf(mockDrawingArea.getStepUnit()), result.getSecond());
    }

    @Test
    public void testActionPerformedWhenRight()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RIGHT");

        testObject.actionPerformed(mockActionEvent);

        Pair<Integer, Integer> result = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(-1, 0);
        Assert.assertEquals(Integer.valueOf(0), result.getFirst());
        Assert.assertEquals(Integer.valueOf(-mockDrawingArea.getStepUnit()), result.getSecond());
    }
}
