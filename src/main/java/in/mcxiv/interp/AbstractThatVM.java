package in.mcxiv.interp;

import in.mcxiv.interp.VariableScope.Variable;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.parser.expression.BinaryOperatorToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.statements.*;
import in.mcxiv.thatlang.universe.Operators;
import in.mcxiv.utils.Pair;
import in.mcxiv.utils.PrimitiveParser;

import java.util.List;
import java.util.Stack;

public abstract class AbstractThatVM {

    final Environment executionEnvironment;

    final Stack<Pair<ProgramToken, VariableScope>> executionStack = new Stack<>();

    public AbstractThatVM(Environment environment) {
        this();
        executionEnvironment.assertiveMerge(environment);
    }

    public AbstractThatVM() {
        executionEnvironment = new THATLANGEnvironment(this);
    }

    public ProgramToken getMain(ProgramFileToken file) {
        return file.getProgramTokens().stream()
                .filter(program -> program.getProgramName().equals("main"))
                .findFirst().orElse((ProgramToken) file.getChildren().get(0));
    }

    public void load(ProgramFileToken file) {
        executionEnvironment.addProgramFile(file);
        file.getProgramTokens().stream()
                .filter(program -> program.getProgramName().equals("init"))
                .forEach(this::run);
    }

    public void run() {
        run(executionEnvironment.getProgramFiles().get(0));
    }

    public void run(ProgramFileToken file) {
        run(getMain(file));
    }

    public void run(ProgramToken programToken) {
        executionStack.push(new Pair<>(programToken, new VariableScope()));
        programToken.getStatements().forEach(this::runStatement);
        executionStack.pop();
    }

    public void runStatement(StatementToken node) {
//        executionEnvironment.getPrograms(); may have optionally defined statements :evil_laugh:

        if (node instanceof VariableDefinitionToken vdt)
            executionStack.peek().getB().newVariable(vdt.getType(), vdt.getName(), eval(vdt.getExpression()).v());
//
        else if (node instanceof AssignmentToken at)
            executionStack.peek().getB().variables.stream().filter(variable -> at.getName().equals(variable.name)).findFirst().ifPresent(variable -> variable.value = eval(at.getExpression()).value);
//
        else if (node instanceof ForStatementToken fst)
            for (
                    runStatement(fst.getInitializer());
                    PrimitiveParser.BOOLEAN.parse(eval(fst.getCondition()).v());
                    runStatement(fst.getIncremental())
            )
                fst.getStatements().forEach(this::runStatement);
//
        else if (node instanceof IfStatementToken ist) {
            if (PrimitiveParser.BOOLEAN.parse(eval(ist.getCondition()).v()))
                ist.getStatements().forEach(this::runStatement);
        }
//
        else if (node instanceof QuantaStatement qs)
            evalQuanta(qs.getToken());

    }

    public Variable eval(ExpressionsToken expression) {
        if (expression instanceof QuantaExpressionToken qet)
            return evalQuanta(qet);
        else if (expression instanceof BinaryOperatorToken bot)
            return evalBinary(bot);
        return null;
    }

    public Variable evalQuanta(QuantaExpressionToken qet) {
        var iterator = qet.quantaIterator();

//        executionStack.peek().getB().
        Variable variable = null;

        while (iterator.hasNext()) {

            if (iterator.isString()) {
                String value = iterator.nextString().getValue();
                assert variable == null: "Cant access a string as if it's a member of some variable...";
                variable = Variable.of(value);

            } else if (iterator.isMember()) {
                String value = iterator.nextMember().getValue();
                if (variable == null) {
                    variable = executionStack.peek().getB().seek(value);
                    if (variable == null) variable = Variable.of(value);
                } else variable.seekMember(value);

            } else if (iterator.isFunction()) {
                FunctionCallToken function = iterator.nextFunction();
                if (variable == null) {
                    variable = evalFunction(function);
                } else {
                    variable.seekFunction(function);
                }
            }
        }
        return variable == null ? Variable.NULL : variable;
    }

    public Variable evalBinary(BinaryOperatorToken bot) {
        return Operators.operate(eval(bot.getLeft()), bot.getOperator(), eval((bot.getRight())));
    }

    public Variable evalFunction(FunctionCallToken fct) {
        List<FunctionEvaluator> list = executionEnvironment.getFunctionEvaluators();

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isApplicable(fct))
                return evaluator.apply(fct);

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isDigestible(fct))
                return evaluator.apply(fct);

        return Variable.NULL;
    }

}
