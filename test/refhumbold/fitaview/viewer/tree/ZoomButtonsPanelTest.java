package refhumbold.fitaview.viewer.tree;

import java.awt.event.ActionEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZoomButtonsPanelTest
{
    @Mock
    private TreeDrawingArea mockDrawingArea;

    @Mock
    private ActionEvent mockActionEvent;

    @InjectMocks
    private ZoomButtonsPanel testObject;

    @Before
    public void setUp()
    {
        Mockito.doNothing().when(mockDrawingArea).revalidate();
        Mockito.doNothing().when(mockDrawingArea).repaint();
        Mockito.doCallRealMethod().when(mockDrawingArea).zeroZoom();
        Mockito.doCallRealMethod().when(mockDrawingArea).zoom(ArgumentMatchers.anyInt());
        Mockito.doCallRealMethod().when(mockDrawingArea).getZoomLevel();
    }

    @After
    public void tearDown()
    {
        testObject = null;
    }

    @Test
    public void testActionPerformedWhenZeroZoom()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_ZERO");

        testObject.actionPerformed(mockActionEvent);

        int result = mockDrawingArea.getZoomLevel();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).zeroZoom();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testActionPerformedWhenZoomIn()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");

        testObject.actionPerformed(mockActionEvent);
        testObject.actionPerformed(mockActionEvent);

        int result = mockDrawingArea.getZoomLevel();

        Mockito.verify(mockDrawingArea, Mockito.times(2)).zoom(1);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testActionPerformedWhenZoomOut()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");

        testObject.actionPerformed(mockActionEvent);
        testObject.actionPerformed(mockActionEvent);

        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_OUT");

        testObject.actionPerformed(mockActionEvent);

        int result = mockDrawingArea.getZoomLevel();
        InOrder order = Mockito.inOrder(mockDrawingArea);

        order.verify(mockDrawingArea, Mockito.times(2)).zoom(1);
        order.verify(mockDrawingArea, Mockito.times(1)).zoom(-1);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testActionPerformedWhenZoomOutOnMinimum()
    {
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_OUT");

        testObject.actionPerformed(mockActionEvent);

        int result = mockDrawingArea.getZoomLevel();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).zoom(-1);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testActionPerformedWhenZoomInOnMaximum()
    {
        int invoking = TreeDrawingArea.MAX_ZOOM + 1;

        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");

        for(int i = 0; i < invoking; ++i)
            testObject.actionPerformed(mockActionEvent);

        int result = mockDrawingArea.getZoomLevel();

        Mockito.verify(mockDrawingArea, Mockito.times(invoking)).zoom(1);
        Assert.assertEquals(TreeDrawingArea.MAX_ZOOM, result);
    }
}
