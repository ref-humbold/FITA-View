package ref_humbold.fita_view.automaton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ref_humbold.fita_view.Pair;
import ref_humbold.fita_view.Quadruple;
import ref_humbold.fita_view.Triple;
import ref_humbold.fita_view.Tuple;

public class Transitions<K extends Tuple, V>
{
    static final String EVERY_VALUE = "(*)";
    static final String SAME_VALUE = "(=)";
    static final String LEFT_VALUE = "(<)";
    static final String RIGHT_VALUE = "(>)";

    private Map<Pair<Variable, Tuple>, V> map = new HashMap<>();

    public Transitions()
    {
    }

    public boolean containsKey(Variable var, K key)
    {
        return map.containsKey(Pair.make(var, (Tuple)key));
    }

    public void add(Variable var, K key, V value)
    {
        map.put(Pair.make(var, key), value);
    }

    public V get(Variable var, K key)
    {
        for(int i = 0; i < 1 << key.getArity(); ++i)
        {
            Tuple t = setWildcard(i, key);
            V value = map.get(Pair.make(var, t));

            if(value != null)
                return value;
        }

        return null;
    }

    private Tuple setWildcard(int mask, Tuple t)
    {
        Object[] objects = t.toArray();

        switch(t.getArity())
        {
            case 2:
                if((mask & 1) == 1)
                    objects[0] = EVERY_VALUE;

                if((mask & 2) == 2)
                    objects[1] = EVERY_VALUE;

                return Pair.make(objects[0], objects[1]);

            case 3:
                if((mask & 1) == 1)
                    objects[0] = EVERY_VALUE;

                if((mask & 2) == 2)
                    objects[1] = EVERY_VALUE;

                if((mask & 4) == 4)
                    objects[2] = EVERY_VALUE;

                return Triple.make(objects[0], objects[1], objects[2]);

            case 4:
                if((mask & 1) == 1)
                    objects[0] = EVERY_VALUE;

                if((mask & 2) == 2)
                    objects[1] = EVERY_VALUE;

                if((mask & 4) == 4)
                    objects[2] = EVERY_VALUE;

                if((mask & 8) == 8)
                    objects[3] = EVERY_VALUE;

                return Quadruple.make(objects[0], objects[1], objects[2], objects[3]);

            default:
                return null;
        }
    }

    @Override
    public String toString()
    {
        return "Transitions::" + map.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj == null || !(obj instanceof Transitions))
            return false;

        Transitions<?, ?> other = (Transitions<?, ?>)obj;

        return Objects.equals(this.map, other.map);
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }
}
