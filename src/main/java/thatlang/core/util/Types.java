package thatlang.core.util;

import thatlang.THOSEUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public final class Types {

    private static final Class<?>[] primitiveTypes = {
            boolean.class,
            byte.class,
            char.class,
            short.class,
            int.class,
            float.class,
            long.class,
            double.class,
            Boolean.class,
            Byte.class,
            Character.class,
            Short.class,
            Integer.class,
            Float.class,
            Long.class,
            Double.class
    };

    private static final Class<?>[] nativeTypes = {
            String.class,
            ArrayList.class,
            LinkedList.class, // Any other list structs worth including?
            HashSet.class, // TODO RESEARCH: What are java.util.LinkedHashSet and java.util.TreeSet?
            HashMap.class // TODO RESEARCH: What are java.util.Hashtable and java.util.WeakHashMap?
    };

    private Types() {
    }

    public static boolean isPrimitiveType(Class<?> clazz) {
        return THOSEUtils.contains(primitiveTypes, clazz);
    }

    public static boolean isNativeType(Class<?> clazz) {
        if (isPrimitiveType(clazz)) return true;
        return THOSEUtils.contains(nativeTypes, clazz);
    }

}
