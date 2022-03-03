package interpreter;

public interface Interpretable<Environment, Returnable> {
    Returnable interpret(Environment environment);
}
