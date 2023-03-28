package fitaview.automaton.traversing;

public final class TraversingFactory
{
    public static BottomUpTraversing getBottomUpTraversing(TraversingMode mode)
            throws IncorrectTraversingException
    {
        switch(mode)
        {
            case LEVEL:
                return new BottomUpLevel();

            case BFS:
                return new BottomUpBFS();

            case DFS:
                throw new IncorrectTraversingException(
                        "Cannot perform DFS traversing on bottom-up automaton");
        }

        return null;
    }

    public static TopDownTraversing getTopDownTraversing(TraversingMode mode)
    {
        switch(mode)
        {
            case LEVEL:
                return new TopDownLevel();

            case BFS:
                return new TopDownBFS();

            case DFS:
                return new TopDownDFS();
        }

        return null;
    }

    public static boolean isCorrectMode(String name)
    {
        try
        {
            TraversingMode.valueOf(name);
        }
        catch(IllegalArgumentException e)
        {
            return false;
        }

        return true;
    }
}
