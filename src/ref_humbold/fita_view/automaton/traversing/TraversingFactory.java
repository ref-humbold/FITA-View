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

    public TreeTraversing getTraversing(TraversingMode mode, TraversingDirection direction)
        throws IncorrectTraversingException
    {
        switch(mode)
        {
            case LEVEL:
                switch(direction)
                {
                    case TOP_DOWN:
                        return new TopDownLevel();

                    case BOTTOM_UP:
                        return new BottomUpLevel();
                }
                break;

            case BFS:
                switch(direction)
                {
                    case TOP_DOWN:
                        return new TopDownBFS();

                    case BOTTOM_UP:
                        return new BottomUpBFS();
                }
                break;

            case DFS:
                switch(direction)
                {
                    case TOP_DOWN:
                        return new TopDownDFS();

                    case BOTTOM_UP:
                        throw new IncorrectTraversingException(
                            "Cannot perform DFS traversing on bottom-up automaton.");
                }
                break;
        }

        return null;
    }
}
