package in.mcxiv.interp.functions;

import in.mcxiv.interp.Environment;
import in.mcxiv.interp.FunctionEvaluator;
import in.mcxiv.interp.VariableScope.Variable;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.tryCatchSuite.Try;

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
        return new Variable("val", "null", switch (fct.getValue()) {
            case SCAN_LINE -> Try.If(scanner::hasNextLine).Then(scanner::nextLine).Else(() -> "").toString();
            case SCAN_INT -> Try.If(scanner::hasNextInt).Then(scanner::nextInt).Else(() -> -1).toString();
            case SCAN_FLOAT -> Try.If(scanner::hasNextFloat).Then(scanner::nextFloat).Else(() -> -1f).toString();
            default -> throw new IllegalStateException("Unexpected value: " + fct.getValue());
        });
    }
}