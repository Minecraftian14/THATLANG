package in.mcxiv.interp.functions;

import in.mcxiv.interp.Environment;
import in.mcxiv.interp.FunctionEvaluator;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.Arrays;

public class ProgramFunctions extends FunctionEvaluator {

    public static final String EXIT = "exit";
    public static final String TIME = "time";

    private static final String[] mainNames = new String[]{EXIT, TIME};

    static {
        Arrays.sort(mainNames);
    }

    public ProgramFunctions(Environment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return Arrays.binarySearch(mainNames, fct.getValue()) >= 0;
    }

    @Override
    public THATObject apply(FunctionCallToken fct) {
        THATObject object = THOSEObjects.NULL;
        switch (fct.getValue()) {
            case EXIT -> System.exit(fct.getArguments().getExpressions().size() == 0 ? 0 : PrimitiveParser.INT.parse(environment.vm.eval(fct.getArguments().getExpressions().get(0)).v()));
            case TIME -> object = THOSEObjects.createValue(System.currentTimeMillis());
            default -> throw new IllegalStateException("Unexpected value: " + fct.getValue());
        }
        return object;
    }
}