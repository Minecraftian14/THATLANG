package in.mcxiv.thatlang.interpreter.functions;

import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.interpreter.AbstractEnvironment;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.Arrays;
import java.util.List;

public class ProgramFunctions extends FunctionEvaluator {

    public static final String EXIT = "exit";
    public static final String TIME = "time";
    public static final String SLEEP = "sleep";

    private static final String[] mainNames = new String[]{EXIT, TIME, SLEEP};

    static {
        Arrays.sort(mainNames);
    }

    public ProgramFunctions(AbstractEnvironment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return Arrays.binarySearch(mainNames, fct.getValue()) >= 0;
    }

    @Override
    public THATObject apply(FunctionCallToken fct) {
        THATObject object = THOSEObjects.NULL;
        List<ExpressionsToken> expressions = fct.getArguments().getExpressions();
        switch (fct.getValue()) {
            case EXIT -> System.exit(expressions.size() == 0 ? 0
                    : PrimitiveParser.INT.parse(expressions.get(0).interpret(environment.vm).value));
            case TIME -> object = THOSEObjects.createValue(System.currentTimeMillis());
            case SLEEP -> {
                try {
                    int i = PrimitiveParser.INT.parse(expressions.size() < 1 ? 100 : expressions.get(0).interpret(environment.vm).value);
                    if (i > 0)
                        Thread.sleep(i);
                    else throw new IllegalArgumentException("WTH? i = " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + fct.getValue());
        }
        return object;
    }
}