package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;

public class FirstElementChoice<T>
    implements StateChoice<T>
{
    @Override
    public StateChoiceMode getMode()
    {
        return StateChoiceMode.FIRST;
    }

    /**
     * Non-deterministically choosing variable values.
     * @param states set of possible state variable values
     * @return variable value chosen as first value return by set iterator
     */
    @Override
    public T chooseState(Collection<T> states)
    {
        return states.iterator().next();
    }
}
