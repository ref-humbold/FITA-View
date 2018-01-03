package ref_humbold.fita_view.automaton.nondeterminism;

public class StateChoiceFactory
{
    public static StateChoice createChoice(StateChoiceMode mode)
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
