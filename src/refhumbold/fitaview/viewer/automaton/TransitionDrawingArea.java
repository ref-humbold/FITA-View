package refhumbold.fitaview.viewer.automaton;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import refhumbold.fitaview.Pair;
import refhumbold.fitaview.Pointer;
import refhumbold.fitaview.Triple;
import refhumbold.fitaview.automaton.*;
import refhumbold.fitaview.messaging.Message;
import refhumbold.fitaview.messaging.MessageReceiver;
import refhumbold.fitaview.messaging.SignalReceiver;

public class TransitionDrawingArea
    extends JPanel
    implements MessageReceiver<Triple<NodeInfoSource, String, Map<Variable, String>>>,
               SignalReceiver
{
    private static final long serialVersionUID = -1303489069622584091L;

    Pair<String, Map<Variable, String>> parentInfo;
    Pair<String, Map<Variable, String>> leftSonInfo;
    Pair<String, Map<Variable, String>> rightSonInfo;
    private StateDrawer stateDrawer = new StateDrawer();
    private Pointer<TreeAutomaton> automatonPointer;

    public TransitionDrawingArea(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        automatonPointer.addReceiver(this);
        TransitionSender.getInstance().addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
            resetStates();
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance()
            && !automatonPointer.isEmpty()
            && automatonPointer.get().getRunningMode() == AutomatonRunningMode.STOPPED)
            resetStates();

        revalidate();
        repaint();
    }

    @Override
    public void receiveMessage(
        Message<Triple<NodeInfoSource, String, Map<Variable, String>>> message)
    {
        Triple<NodeInfoSource, String, Map<Variable, String>> param = message.getParam();

        switch(param.getFirst())
        {
            case LEFT_SON:
                leftSonInfo = Pair.make(param.getSecond(), param.getThird());
                break;

            case PARENT:
                parentInfo = Pair.make(param.getSecond(), param.getThird());
                break;

            case RIGHT_SON:
                rightSonInfo = Pair.make(param.getSecond(), param.getThird());
                break;
        }

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        graphics.setColor(Color.BLACK);
        stateDrawer.setRectWidth(2 * getWidth() / 5);
        stateDrawer.setRectHeight(2 * getHeight() / 5);
        stateDrawer.setGraphics(graphics);

        if(!automatonPointer.isEmpty() && parentInfo != null)
        {
            AutomatonDirection direction = automatonPointer.get().getDirection();

            stateDrawer.drawInfo(parentInfo.getFirst(), parentInfo.getSecond(),
                                 Pair.make(getWidth() / 2, getHeight() / 4));
            stateDrawer.drawInfo(leftSonInfo.getFirst(), leftSonInfo.getSecond(),
                                 Pair.make(getWidth() / 4, 3 * getHeight() / 4));
            stateDrawer.drawInfo(rightSonInfo.getFirst(), rightSonInfo.getSecond(),
                                 Pair.make(3 * getWidth() / 4, 3 * getHeight() / 4));

            stateDrawer.drawArrow(Pair.make(getWidth() / 2, getHeight() / 4),
                                  Pair.make(getWidth() / 4, 3 * getHeight() / 4), direction);
            stateDrawer.drawArrow(Pair.make(getWidth() / 2, getHeight() / 4),
                                  Pair.make(3 * getWidth() / 4, 3 * getHeight() / 4), direction);
        }
    }

    private void resetStates()
    {
        parentInfo = null;
        leftSonInfo = null;
        rightSonInfo = null;
    }
}
