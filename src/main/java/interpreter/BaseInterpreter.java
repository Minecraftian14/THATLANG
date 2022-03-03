package interpreter;

public class BaseInterpreter<Environment> {

    public void interpret(Interpretable<Environment, ?> interpretable, Environment environment) {
        interpretable.interpret(environment);
    }
}
