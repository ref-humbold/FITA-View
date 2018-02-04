package ref_humbold.fita_view.automaton.nondeterminism;

public class UserChoiceVisibility
{
    private static UserChoiceVisibility instance = null;

    boolean isVisible = false;

    private UserChoiceVisibility()
    {
        super();
    }

    public static UserChoiceVisibility getInstance()
    {
        if(instance == null)
            instance = new UserChoiceVisibility();

        return instance;
    }

    public boolean getVisible()
    {
        return this.isVisible;
    }
}
