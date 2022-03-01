package thatlang;

public class THOSEUtils {

    public static boolean contains(Class<?>[] array, Class<?> item) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == item)
                return true;
        return false;
    }

    public static boolean contains(Iterable<?> i, Object o) {
        for (Object value : i)
            if (o.equals(value))
                return true;
        return false;
    }
}
