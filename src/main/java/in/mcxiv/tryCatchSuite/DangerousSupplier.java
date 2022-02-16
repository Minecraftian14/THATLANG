package in.mcxiv.tryCatchSuite;

public interface DangerousSupplier<ReturnType> {
    ReturnType get() throws Exception;
}
