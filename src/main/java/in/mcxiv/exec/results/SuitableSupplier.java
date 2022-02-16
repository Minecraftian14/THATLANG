package in.mcxiv.exec.results;

import java.util.function.Supplier;

public interface SuitableSupplier extends Supplier<Object> {
    Object get();

    @SuppressWarnings("unchecked")
    default <T> T getExp() {
        try {
            return (T) get();
        } catch (ClassCastException cce) {
            return null;
        }
    }
}
