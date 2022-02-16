package in.mcxiv.tryCatchSuite;

import java.util.function.Supplier;

public interface SupplierProvider<ReturnType> {
    Supplier<?> NULL = () -> null;

    ReturnType Else(Supplier<ReturnType> returnTypeSupplier);

    default ReturnType ElseNull() {
        return null;
    }

    static <ReturnType> Supplier<ReturnType> NULL() {
        return () -> null;
    }

}
