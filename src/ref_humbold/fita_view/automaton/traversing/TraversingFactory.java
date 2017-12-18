package ref_humbold.fita_view.automaton.traversing;

public class TraversingFactory
{
    private static TraversingFactory instance = null;

    private TraversingFactory()
    {
    }

    public static TraversingFactory getInstance()
    {
        if(instance == null)
            instance = new TraversingFactory();

        return instance;
    }

    public BottomUpTraversing getBottomUpTraversing(Mode mode)
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
                    "Cannot perform DFS traversing on bottom-up automaton.");
        }

        return null;
    }

    public TopDownTraversing getTopDownTraversing(Mode mode)
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

    public enum Mode
    {
        DFS, BFS, LEVEL
    }
}
