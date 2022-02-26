package in.mcxiv.interp;

import in.mcxiv.thatlang.FunctionToken;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.utils.Pair;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

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

    public static FunctionEvaluator createEvaluatorFromToken(Environment environment, FunctionToken function) {
        return new TokenAdaptor(environment, function);
    }

    private static final class TokenAdaptor extends FunctionEvaluator {

        private final FunctionToken token;
        private final int numberOfParameters;

        public TokenAdaptor(Environment environment, FunctionToken token) {
            super(environment);
            this.token = token;
            this.numberOfParameters = token.getParameterNames().length;
        }

        @Override
        public boolean isApplicable(FunctionCallToken fct) {
            return token.getValue().equals(fct.getValue())
                   && numberOfParameters == fct.getArguments().noOfChildren();
        }

        @Override
        public THATObject apply(FunctionCallToken fct) {
            String[] names = token.getParameterNames();
            THATObject[] values = fct.getArguments().getExpressions().stream().map(environment.vm::eval).toArray(THATObject[]::new);

            for (int i = 0; i < numberOfParameters; i++)
                values[i].name = names[i];

            var that = environment.vm.executionStack.peek();
            environment.vm.executionStack.push(new Pair<>(null, new VariableScope()));
            VariableScope scope = environment.vm.executionStack.peek().getB();
            scope.newVariable(THOSEObjects.create("rt val", "that", that));
            for (int i = 0; i < numberOfParameters; i++)
                scope.newVariable(values[i]);

            token.getStatements().forEach(environment.vm::runStatement);

            environment.vm.executionStack.pop();
            return THOSEObjects.NULL;
        }
    }

}
