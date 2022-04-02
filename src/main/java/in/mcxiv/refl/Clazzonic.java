package in.mcxiv.refl;

import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.function.Consumer;

public class Clazzonic {

    @SuppressWarnings("unchecked")
    public static <Type> Class<Type> getClazz() {
        try {
            return (Class<Type>) (
                    (ParameterizedType) Clazzonic.class
                            .getMethod("getClazz")
                            .getGenericReturnType()
            ).getRawType();
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static void forEachField(Class<?> clazz, Consumer<Field> o) {
        Arrays.stream(clazz.getFields())
                .forEach(o);
    }

    public static void forEachAccessibleFieldName(Object object, Consumer<Pair<String, Object>> consumer) {
        Arrays.stream(object.getClass().getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getName().startsWith("get"))
                .map(method -> new Pair<>(method.getName().substring(3), method))
                .map(pair -> new Pair<>(pair.getA(), Try.getAnd(() -> pair.getB().invoke(object)).elseNull()))
                .forEach(consumer);
    }
}
