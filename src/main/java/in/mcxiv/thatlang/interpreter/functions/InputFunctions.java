package in.mcxiv.thatlang.interpreter.functions;

import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.interpreter.AbstractEnvironment;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class InputFunctions extends FunctionEvaluator {

    public static final String SCAN_LINE = "scan";
    public static final String SCAN_INT = "scani";
    public static final String SCAN_FLOAT = "scanf";

    private static final String[] mainNames = new String[]{SCAN_LINE, SCAN_INT, SCAN_FLOAT};
    private static final Scanner scanner = new Scanner(new InputStreamReader(System.in));

    static {
        Arrays.sort(mainNames);
    }

    public InputFunctions(AbstractEnvironment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return Arrays.binarySearch(mainNames, fct.getValue()) >= 0;
    }

    @Override
    public THATObject apply(FunctionCallToken fct) {
        String s = Try.If(scanner::hasNextLine).Then(scanner::nextLine).Else(() -> "").toString();
        THATObject variable = THOSEObjects.createValue(switch (fct.getValue()) {
            case SCAN_LINE -> s;
            case SCAN_INT -> PrimitiveParser.INT.parse(s);
            case SCAN_FLOAT -> PrimitiveParser.DOUBLE.parse(s);
            default -> throw new IllegalStateException("Unexpected value: " + fct.getValue());
        });
        return variable;
    }
}