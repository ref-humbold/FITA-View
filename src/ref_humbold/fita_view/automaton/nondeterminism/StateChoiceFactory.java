package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.function.Function;
import javax.swing.JFrame;

public class StateChoiceFactory
{
    public static <T> StateChoice<T> createChoice(StateChoiceMode mode, JFrame frame,
                                                  Function<T, String> convert)
    {
        switch(mode)
        {
            case FIRST:
                return new FirstElementChoice<>();

            case RANDOM:
                return new RandomChoice<>();

            case GREATEST:
                return new GreatestHashCodeChoice<>();

            case LEAST:
                return new LeastHashCodeChoice<>();

            case USER:
                return new UserChoice<>(frame, convert);
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
