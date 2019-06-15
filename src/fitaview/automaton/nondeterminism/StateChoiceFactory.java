package fitaview.automaton.nondeterminism;

import java.util.function.Function;

public class StateChoiceFactory
{
    public static <K, R> StateChoice<K, R> createAutomatedChoice(StateChoiceMode mode)
        throws IncorrectStateChoiceModeException
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
                throw new IncorrectStateChoiceModeException(
                    "UserChoice cannot be chosen as an automated choice");
        }

        return null;
    }

    public static <K, R> StateChoice<K, R> createUserChoice(Function<K, String> convertKey,
                                                            Function<R, String> convertResult)
    {
        return new UserChoice<>(convertKey, convertResult);
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
