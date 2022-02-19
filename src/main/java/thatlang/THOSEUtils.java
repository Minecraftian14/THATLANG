package thatlang;

public class THOSEUtils {

    public static boolean contains(Class<?>[] array, Class<?> item) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == item)
                return true;
        return false;
    }

}
