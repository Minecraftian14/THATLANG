package in.mcxiv.interp;

import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import thatlang.core.THATObject;

import java.util.function.Function;

public abstract class FunctionEvaluator implements Function<FunctionCallToken, THATObject> {

    final protected Environment environment;

    protected FunctionEvaluator(Environment environment) {
        this.environment = environment;
    }

    /**
     * Returns if the given FCT must be parsed by this FE.
     */
    public abstract boolean isApplicable(FunctionCallToken fct);

    /**
     * Returns if the given FCT can be parsed by this FE.
     */
    public boolean isDigestible(FunctionCallToken fct) {
        return false;
    }

}
