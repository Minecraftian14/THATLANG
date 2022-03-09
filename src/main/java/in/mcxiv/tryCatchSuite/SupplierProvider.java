package in.mcxiv.tryCatchSuite;

import java.util.function.Supplier;

public interface SupplierProvider<ReturnType> {
    Supplier<?> NULL = () -> null;

    ReturnType Else(Supplier<ReturnType> returnTypeSupplier);

    default ReturnType ElseNull() {
        return Else(() -> null);
    }

    default ReturnType ElseThrow() {
        return Else(() -> {
            throw new RuntimeException();
        });
    }

    static <ReturnType> Supplier<ReturnType> NULL() {
        return () -> null;
    }

}
