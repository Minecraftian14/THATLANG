package in.mcxiv.interp.functions;

import in.mcxiv.interp.Environment;
import in.mcxiv.interp.FunctionEvaluator;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintFunctions extends FunctionEvaluator {

    public static final String PRINT = "print";
    public static final String PRINT_S = "prt";
    public static final String PRINT_SS = "p";
    public static final String PRINTLN = "println";
    public static final String PRINTLN_S = "prtln";
    public static final String PRINTLN_SS = "pln";
    public static final String PRINTF = "printf";
    public static final String PRINTF_S = "prtf";
    public static final String PRINTF_SS = "pf";

    private static final String[] mainNames = new String[]{PRINT, PRINTLN, PRINTF};
    private static final String[] otherNames = new String[]{PRINT_S, PRINT_SS, PRINTLN_S, PRINTLN_SS, PRINTF_S, PRINTF_SS};

    static {
        Arrays.sort(mainNames);
        Arrays.sort(otherNames);
    }

    public PrintFunctions(Environment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return Arrays.binarySearch(mainNames, fct.getValue()) >= 0;
    }

    @Override
    public boolean isDigestible(FunctionCallToken fct) {
        return Arrays.binarySearch(otherNames, fct.getValue()) >= 0;
    }

    @Override
    public THATObject apply(FunctionCallToken fct) {

        List<ExpressionsToken> list;

        switch (fct.getValue()) {
            case PRINT, PRINT_S, PRINT_SS -> {
                // TODO what to do if there are more than one arguments?
                list = fct.getArguments().getExpressions();
                if (list.size() == 0) break;
//              if (list.size() >= 2) break; 
                environment.out.print(environment.vm.eval(list.get(0)).v());
            }
            case PRINTLN, PRINTLN_S, PRINTLN_SS -> {
                list = fct.getArguments().getExpressions();
                if (list.size() == 0) break;
                environment.out.println(environment.vm.eval(list.get(0)).v());
            }
            case PRINTF, PRINTF_S, PRINTF_SS -> {
                list = new ArrayList<>(fct.getArguments().getExpressions());
                if (list.size() == 0) break;
                String str = environment.vm.eval(list.remove(0)).v();
                environment.out.printf(str, list.stream().map(environment.vm::eval).map(THATObject::v).toArray());
            }
        }

        return THOSEObjects.NULL;
    }
}
