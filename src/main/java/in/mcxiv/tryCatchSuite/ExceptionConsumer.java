package in.mcxiv.tryCatchSuite;

public interface ExceptionConsumer {
    public static final ExceptionConsumer NO_ACTION = exception -> {};
    void consume(Exception exception);
}
