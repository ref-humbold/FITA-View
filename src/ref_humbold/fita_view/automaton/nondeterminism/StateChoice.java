package ref_humbold.fita_view.automaton.nondeterminism;

import java.util.Collection;

public interface StateChoice<T>
{
    /**
     * @return type of non-deterministic choice
     */
    StateChoiceMode getMode();

    /**
     * Non-deterministically choosing variable values.
     * @param states possible state variable values
     * @return variable value chosen
     */
    T chooseState(Collection<T> states);
}
