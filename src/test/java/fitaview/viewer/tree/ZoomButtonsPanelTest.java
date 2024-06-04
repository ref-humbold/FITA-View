package fitaview.viewer.tree;

import java.awt.event.ActionEvent;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZoomButtonsPanelTest
{
    @Mock private TreeDrawingArea mockDrawingArea;

    @Mock private ActionEvent mockActionEvent;

    @InjectMocks private ZoomButtonsPanel testObject;

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
    public void actionPerformed_WhenZeroZoom()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_ZERO");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Mockito.verify(mockDrawingArea, Mockito.times(1)).zeroZoom();
        Assertions.assertThat(mockDrawingArea.getZoomLevel()).isEqualTo(0);
    }

    @Test
    public void actionPerformed_WhenZoomIn()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");
        // when
        testObject.actionPerformed(mockActionEvent);
        testObject.actionPerformed(mockActionEvent);
        // then
        Mockito.verify(mockDrawingArea, Mockito.times(2)).zoom(1);
        Assertions.assertThat(mockDrawingArea.getZoomLevel()).isEqualTo(2);
    }

    @Test
    public void actionPerformed_WhenZoomOut()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");

        testObject.actionPerformed(mockActionEvent);
        testObject.actionPerformed(mockActionEvent);

        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_OUT");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Mockito.inOrder(mockDrawingArea).verify(mockDrawingArea, Mockito.times(2)).zoom(1);
        Mockito.inOrder(mockDrawingArea).verify(mockDrawingArea, Mockito.times(1)).zoom(-1);
        Assertions.assertThat(mockDrawingArea.getZoomLevel()).isEqualTo(1);
    }

    @Test
    public void actionPerformed_WhenZoomOutOnMinimum()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_OUT");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Mockito.verify(mockDrawingArea, Mockito.times(1)).zoom(-1);
        Assertions.assertThat(mockDrawingArea.getZoomLevel()).isEqualTo(0);
    }

    @Test
    public void actionPerformed_WhenZoomInOnMaximum()
    {
        // given
        int invoking = TreeDrawingArea.MAX_ZOOM + 1;

        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("ZOOM_IN");
        // when
        for(int i = 0; i < invoking; ++i)
            testObject.actionPerformed(mockActionEvent);
        // then
        Mockito.verify(mockDrawingArea, Mockito.times(invoking)).zoom(1);
        Assertions.assertThat(mockDrawingArea.getZoomLevel()).isEqualTo(TreeDrawingArea.MAX_ZOOM);
    }
}
