package fitaview.viewer.automaton;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fitaview.automaton.AutomatonDirection;
import fitaview.automaton.Variable;
import fitaview.utils.Pair;

public class StateDrawer
{
    private Graphics graphics;
    private int rectWidth;
    private int rectHeight;

    public void setGraphics(Graphics graphics)
    {
        this.graphics = graphics;
    }

    public void setRectWidth(int rectWidth)
    {
        this.rectWidth = rectWidth;
    }

    public void setRectHeight(int rectHeight)
    {
        this.rectHeight = rectHeight;
    }

    void drawInfo(String label, Map<Variable, String> state, Pair<Integer, Integer> centralPoint)
    {
        if(state == null)
            return;

        int leftAxis = centralPoint.getFirst() - rectWidth / 2;
        int upperAxis = centralPoint.getSecond() - rectHeight / 2;
        List<Map.Entry<Variable, String>> entries = new ArrayList<>(state.entrySet());

        graphics.drawRoundRect(leftAxis, upperAxis, rectWidth, rectHeight, 5, 5);
        graphics.drawString(String.format("'%s'", label), leftAxis + rectWidth / 4, upperAxis + 15);

        for(int i = 0; i < entries.size(); ++i)
            graphics.drawString(getEntryString(entries.get(i)), leftAxis + 10,
                                upperAxis + 30 + 15 * i);
    }

    void drawArrow(Pair<Integer, Integer> parentCentral, Pair<Integer, Integer> sonCentral,
                   AutomatonDirection direction)
    {
        int arrowSize = 8;
        int parentX = sonCentral.getFirst() < parentCentral.getFirst() ? parentCentral.getFirst()
                - rectWidth / 2 : parentCentral.getFirst() + rectWidth / 2;
        int parentY = parentCentral.getSecond();
        int sonX = sonCentral.getFirst();
        int sonY = sonCentral.getSecond() - rectHeight / 2;

        graphics.drawLine(parentX, parentY, sonX, parentY);
        graphics.drawLine(sonX, parentY, sonX, sonY);

        switch(direction)
        {
            case BOTTOM_UP:
                if(sonX > parentX)
                    graphics.fillPolygon(
                            new int[]{parentX, parentX + arrowSize, parentX + arrowSize},
                            new int[]{parentY, parentY - arrowSize / 2, parentY + arrowSize / 2},
                            3);
                else
                    graphics.fillPolygon(
                            new int[]{parentX, parentX - arrowSize, parentX - arrowSize},
                            new int[]{parentY, parentY - arrowSize / 2, parentY + arrowSize / 2},
                            3);
                break;

            case TOP_DOWN:
                graphics.fillPolygon(new int[]{sonX, sonX + 10, sonX - 10},
                                     new int[]{sonY, sonY - 20, sonY - 20}, 3);
                break;
        }
    }

    private String getEntryString(Map.Entry<Variable, String> entry)
    {
        return String.format("%s :: %s", entry.getKey().getVarName(), entry.getValue());
    }
}
