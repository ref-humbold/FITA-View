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

import fitaview.utils.Pair;

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
    public void actionPerformed_WhenCentre()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("CENTRE");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Pair<Integer, Integer> point = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).centralize();
        Assertions.assertThat(point.getFirst()).isEqualTo(Integer.valueOf(0));
        Assertions.assertThat(point.getSecond()).isEqualTo(Integer.valueOf(0));
    }

    @Test
    public void actionPerformed_WhenUp()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("UP");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Pair<Integer, Integer> point = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, 1);
        Assertions.assertThat(point.getFirst())
                  .isEqualTo(Integer.valueOf(mockDrawingArea.getStepUnit()));
        Assertions.assertThat(point.getSecond()).isEqualTo(Integer.valueOf(0));
    }

    @Test
    public void actionPerformed_WhenDown()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("DOWN");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Pair<Integer, Integer> point = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(0, -1);
        Assertions.assertThat(point.getFirst())
                  .isEqualTo(Integer.valueOf(-mockDrawingArea.getStepUnit()));
        Assertions.assertThat(point.getSecond()).isEqualTo(Integer.valueOf(0));
    }

    @Test
    public void actionPerformed_WhenLeft()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("LEFT");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Pair<Integer, Integer> point = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(1, 0);
        Assertions.assertThat(point.getFirst()).isEqualTo(Integer.valueOf(0));
        Assertions.assertThat(point.getSecond())
                  .isEqualTo(Integer.valueOf(mockDrawingArea.getStepUnit()));
    }

    @Test
    public void actionPerformed_WhenRight()
    {
        // given
        Mockito.when(mockActionEvent.getActionCommand()).thenReturn("RIGHT");
        // when
        testObject.actionPerformed(mockActionEvent);
        // then
        Pair<Integer, Integer> point = mockDrawingArea.getAxisPoint();

        Mockito.verify(mockDrawingArea, Mockito.times(1)).moveArea(-1, 0);
        Assertions.assertThat(point.getFirst()).isEqualTo(Integer.valueOf(0));
        Assertions.assertThat(point.getSecond())
                  .isEqualTo(Integer.valueOf(-mockDrawingArea.getStepUnit()));
    }
}
