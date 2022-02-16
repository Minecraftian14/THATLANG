package in.mcxiv.tryCatchSuite;

public interface SupplierProviderStep<ReturnType> {
    SupplierProvider<ReturnType> Then(DangerousSupplier<ReturnType> supplier);
}
