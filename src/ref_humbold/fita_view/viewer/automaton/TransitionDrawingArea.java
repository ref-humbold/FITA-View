package ref_humbold.fita_view.viewer.automaton;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Pointer;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.automaton.AutomatonRunningModeSender;
import ref_humbold.fita_view.automaton.TransitionSender;
import ref_humbold.fita_view.automaton.TreeAutomaton;
import ref_humbold.fita_view.automaton.Variable;
import ref_humbold.fita_view.messaging.Message;
import ref_humbold.fita_view.messaging.MessageReceiver;
import ref_humbold.fita_view.messaging.SignalReceiver;

public class TransitionDrawingArea
    extends JPanel
    implements
    MessageReceiver<Triple<Map<Variable, String>, Map<Variable, String>, Map<Variable, String>>>,
    SignalReceiver
{
    private static final long serialVersionUID = -1303489069622584091L;

    private Pointer<TreeAutomaton> automatonPointer;
    private Map<Variable, String> parentState;
    private Map<Variable, String> leftSonState;
    private Map<Variable, String> rightSonState;
    private int rectWidth;
    private int rectHeight;

    public TransitionDrawingArea(Pointer<TreeAutomaton> automatonPointer)
    {
        super();

        this.automatonPointer = automatonPointer;
        this.automatonPointer.addReceiver(this);
        TransitionSender.getInstance().addReceiver(this);
        AutomatonRunningModeSender.getInstance().addReceiver(this);

        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public void receiveSignal(Message<Void> signal)
    {
        if(signal.getSource() == automatonPointer)
            resetStates();
        else if(signal.getSource() == AutomatonRunningModeSender.getInstance()
            && !automatonPointer.isEmpty())
            switch(automatonPointer.get().getRunningMode())
            {
                case STOPPED:
                    resetStates();
                    break;

                default:
                    break;
            }

        repaint();
    }

    @Override
    public void receiveMessage(
        Message<Triple<Map<Variable, String>, Map<Variable, String>, Map<Variable, String>>> message)
    {
        leftSonState = message.getParam().getFirst();
        parentState = message.getParam().getSecond();
        rightSonState = message.getParam().getThird();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Pair<Integer, Integer> parentCorner;
        Pair<Integer, Integer> leftSonCorner;
        Pair<Integer, Integer> rightSonCorner;

        rectWidth = 2 * getWidth() / 5;
        rectHeight = 2 * getHeight() / 5;
        graphics.setColor(Color.BLACK);
        parentCorner = drawState(graphics, parentState, getWidth() / 2, getHeight() / 4);
        leftSonCorner = drawState(graphics, leftSonState, getWidth() / 4, 3 * getHeight() / 4);
        rightSonCorner = drawState(graphics, rightSonState, 3 * getWidth() / 4,
                                   3 * getHeight() / 4);
        if(parentCorner != null)
        {
            drawArrow(graphics, parentCorner.getFirst(), parentCorner.getSecond() + rectHeight / 2,
                      leftSonCorner.getFirst() + rectWidth / 2, leftSonCorner.getSecond());
            drawArrow(graphics, parentCorner.getFirst() + rectWidth,
                      parentCorner.getSecond() + rectHeight / 2,
                      rightSonCorner.getFirst() + rectWidth / 2, rightSonCorner.getSecond());
        }
    }

    private void resetStates()
    {
        parentState = null;
        leftSonState = null;
        rightSonState = null;
    }

    private Pair<Integer, Integer> drawState(Graphics graphics, Map<Variable, String> state,
                                             int verticalCentre, int horizontalCentre)
    {
        if(state == null)
            return null;

        int leftAxis = verticalCentre - rectWidth / 2;
        int upperAxis = horizontalCentre - rectHeight / 2;
        List<Map.Entry<Variable, String>> entries = new ArrayList<>(state.entrySet());

        graphics.drawRoundRect(leftAxis, upperAxis, rectWidth, rectHeight, 5, 5);

        for(int i = 0; i < entries.size(); ++i)
            graphics.drawString(getEntryString(entries.get(i)), leftAxis + 10,
                                upperAxis + 15 + 15 * i);

        return Pair.make(leftAxis, upperAxis);
    }

    private void drawArrow(Graphics graphics, int parentX, int parentY, int sonX, int sonY)
    {
        if(automatonPointer.isEmpty())
            return;

        int arrowSize = 8;

        graphics.drawLine(parentX, parentY, sonX, parentY);
        graphics.drawLine(sonX, parentY, sonX, sonY);

        switch(automatonPointer.get().getDirection())
        {
            case BOTTOM_UP:
                if(sonX > parentX)
                    graphics.fillPolygon(
                        new int[]{parentX, parentX + arrowSize, parentX + arrowSize},
                        new int[]{parentY, parentY - arrowSize / 2, parentY + arrowSize / 2}, 3);
                else
                    graphics.fillPolygon(
                        new int[]{parentX, parentX - arrowSize, parentX - arrowSize},
                        new int[]{parentY, parentY - arrowSize / 2, parentY + arrowSize / 2}, 3);
                break;

            case TOP_DOWN:
                graphics.fillPolygon(new int[]{sonX, sonX + 10, sonX - 10},
                                     new int[]{sonY, sonY - 20, sonY - 20}, 3);
                break;
        }
    }

    private String getEntryString(Map.Entry<Variable, String> entry)
    {
        return entry.getKey().getVarName() + " :: " + entry.getValue();
    }
}
