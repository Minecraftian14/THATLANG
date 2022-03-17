package in.mcxiv.tryCatchSuite;

public interface DangerousFunction<ArgumentType, ReturnType> {
    ReturnType apply(ArgumentType a) throws Exception;
}
