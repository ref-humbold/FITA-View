package fitaview;

import org.assertj.core.api.Assertions;

public final class TestUtils
{
    public static <T> T failOnException(ExceptionalSupplier<T> supplier)
    {
        try
        {
            return supplier.get();
        }
        catch(Exception ex)
        {
            Assertions.fail("Unexpected exception %s".formatted(ex.getClass().getSimpleName()));
            return null;
        }
    }

    public static void failOnException(ExceptionalRunnable supplier)
    {
        try
        {
            supplier.run();
        }
        catch(Exception ex)
        {
            Assertions.fail("Unexpected exception %s".formatted(ex.getClass().getSimpleName()));
        }
    }

    @FunctionalInterface
    public interface ExceptionalSupplier<T>
    {
        T get()
                throws Exception;
    }

    @FunctionalInterface
    public interface ExceptionalRunnable
    {
        void run()
                throws Exception;
    }
}
