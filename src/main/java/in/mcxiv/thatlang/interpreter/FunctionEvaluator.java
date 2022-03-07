package in.mcxiv.thatlang.interpreter;

import in.mcxiv.thatlang.FunctionToken;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.expression.MappedArgumentsToken;
import in.mcxiv.utils.Pair;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class FunctionEvaluator implements Function<FunctionCallToken, THATObject> {

    public static final FunctionEvaluator anonymous(final AbstractEnvironment environment, final String name, final Function<FunctionCallToken, THATObject> function) {
        return new FunctionEvaluator(environment) {
            @Override
            public boolean isApplicable(FunctionCallToken fct) {
                return name.equals(fct.getValue());
            }

            @Override
            public THATObject apply(FunctionCallToken fct) {
                return function.apply(fct);
            }
        };
    }

    final protected AbstractEnvironment environment;

    protected FunctionEvaluator(AbstractEnvironment environment) {
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

    public static FunctionEvaluator createEvaluatorFromToken(AbstractEnvironment environment, FunctionToken function) {
        return new TokenAdaptor(environment, function);
    }

    private static final class TokenAdaptor extends FunctionEvaluator {

        private final FunctionToken token;
        private final int numberOfParameters;

        public TokenAdaptor(AbstractEnvironment environment, FunctionToken token) {
            super(environment);
            this.token = token;
            this.numberOfParameters = token.getParameterNames().length;
        }

        @Override
        public boolean isApplicable(FunctionCallToken fct) {
            return token.getValue().equals(fct.getValue())
                   && numberOfParameters == fct.getArguments().getArgs().length + fct.getArguments().getMappings().length;
        }

        @Override
        public THATObject apply(FunctionCallToken call) {
            // p-parameters
            ArrayList<String> pNames = new ArrayList<>(Arrays.asList(token.getParameterNames()));
            ArrayList<THATObject> pValues = new ArrayList<>();

            for (MappedArgumentsToken.MappingsToken mapping : call.getArguments().getMappings()) {
                if(pNames.remove(mapping.getName())){
                    THATObject object = mapping.getExpression().interpret(environment.vm);
                    object.name = mapping.getName();
                    pValues.add(object);
                }
            }
            assert call.getArguments().getArgs().length == pNames.size(): "The total number of given args should be equal...";
            for (ExpressionsToken arg : call.getArguments().getArgs()) {
                THATObject object = arg.interpret(environment.vm);
                object.name = pNames.remove(0);
                pValues.add(object);
            }

            var that = environment.vm.executionStack.peek();
            var tScope = that.getB();

            // r-returnables
            String[] rNames = token.getReturnArgNames();
            THATObject[] rValues = new THATObject[rNames.length];
            for (int i = 0; i < rNames.length; i++) {
                THATObject object = tScope.seek(rNames[i]);
                if (object != null) rValues[i] = object;
                else rValues[i] = THOSEObjects.createEmptyVariable(rNames[i]);
            }

            for (int i = 0; i < rValues.length; i++)
                tScope.addVariable(rValues[i]);

            VariableScope fScope;
            environment.vm.executionStack.push(new Pair<>(null, fScope = new VariableScope()));

            fScope.addVariable(THOSEObjects.create("rt val", "that", that));
            for (int i = 0; i < numberOfParameters; i++)
                fScope.addVariable(pValues.get(i));
            for (int i = 0; i < rValues.length; i++)
                fScope.addVariable(rValues[i]);

            token.getStatements().forEach(stmt -> stmt.interpret(environment.vm));

            environment.vm.executionStack.pop();

            if (rValues.length > 1)
                return rValues[0];
            return THOSEObjects.NULL;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        // It's supposed to be a singleton.
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this);
    }

}
