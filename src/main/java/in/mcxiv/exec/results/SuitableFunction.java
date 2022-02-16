package in.mcxiv.exec.results;

import java.util.function.Function;

public interface SuitableFunction extends Function<Object, Object> {
    Object apply(Object o);

    @SuppressWarnings("unchecked")
    default <T> T applyExp(Object o) {
        try {
            return (T) apply(o);
        } catch (ClassCastException cce) {
            return null;
        }
    }
}