package ref_humbold.fita_view.automaton.nondeterminism;

import javax.swing.JFrame;

public class StateChoiceFactory
{
    public static StateChoice createChoice(StateChoiceMode mode, JFrame frame)
    {
        switch(mode)
        {
            case FIRST:
                return new FirstElementChoice();

            case RANDOM:
                return new RandomChoice();

            case GREATEST:
                return new GreatestHashCodeChoice();

            case LEAST:
                return new LeastHashCodeChoice();

            case USER:
                return new UserChoice(frame);
        }

        return null;
    }

    public static boolean isCorrectMode(String name)
    {
        try
        {
            StateChoiceMode.valueOf(name);
        }
        catch(IllegalArgumentException e)
        {
            return false;
        }

        return true;
    }
}
