package in.mcxiv.interp.functions;

import in.mcxiv.interp.Environment;
import in.mcxiv.interp.FunctionEvaluator;
import in.mcxiv.interp.VariableScope.Variable;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.PrimitiveParser;

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

    public InputFunctions(Environment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return Arrays.binarySearch(mainNames, fct.getValue()) >= 0;
    }

    @Override
    public Variable apply(FunctionCallToken fct) {
        String s = Try.If(scanner::hasNextLine).Then(scanner::nextLine).Else(() -> "").toString();
        Variable variable = new Variable("val", "null", switch (fct.getValue()) {
            case SCAN_LINE -> s;
            case SCAN_INT -> PrimitiveParser.LONG.parse(s).toString();
            case SCAN_FLOAT -> PrimitiveParser.DOUBLE.parse(s).toString();
            default -> throw new IllegalStateException("Unexpected value: " + fct.getValue());
        });
        return variable;
    }
}