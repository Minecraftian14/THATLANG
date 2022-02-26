package in.mcxiv.interp;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.parser.expression.BinaryOperatorToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.parser.natives.StringToken;
import in.mcxiv.thatlang.statements.*;
import in.mcxiv.thatlang.universe.Operators;
import in.mcxiv.utils.Pair;
import in.mcxiv.utils.PrimitiveParser;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static thatlang.core.THOSEObjects.DATA_KEY_CONSTRUCTION_TYPE;

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
        return Arrays.stream(file.getPrograms())
                .filter(program -> program.getProgramName().equals("main"))
                .findFirst().orElse(file.getPrograms()[0]);
    }

    public void load(ProgramFileToken file) {
        executionEnvironment.addProgramFile(file);
        Arrays.stream(file.getPrograms())
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

        if (node instanceof VariableDefinitionToken vdt) {
            THATObject object = eval(vdt.getExpression());
            object.putObjectData(DATA_KEY_CONSTRUCTION_TYPE, vdt.getType());
            object.name = vdt.getName();
            executionStack.peek().getB().newVariable(object);
        }
//
        else if (node instanceof AssignmentToken at)
            THOSEObjects.mutateValue(evalQuanta(at.getField()), eval(at.getExpression()).value);
//
        else if (node instanceof MultiAssignmentToken mat) {
            THATObject field = evalQuanta(mat.getField());
            for (int i = 0; i < mat.getSubFields().length; i++) {
                THATObject value = THOSEObjects.createAfterReducing(mat.getValues()[i].getValue());
                field.putMember(mat.getSubFields()[i].getValue(), value);
            }
        }
//
        else if (node instanceof ForStatementToken fst)
            for (
                    runStatement(fst.getInitializer());
                    PrimitiveParser.BOOLEAN.parse(eval(fst.getCondition()).v());
                    runStatement(fst.getIncremental())
            )
                fst.getStatements().forEach(this::runStatement);
//
        else if (node instanceof IfStatementToken ist)
            runIf(ist);
//
        else if (node instanceof QuantaStatement qs)
            evalQuanta(qs.getToken());

    }

    private void runIf(IfStatementToken ist) {
        if (PrimitiveParser.BOOLEAN.parse(eval(ist.getCondition()).v()))
            ist.getStatements().forEach(this::runStatement);
        else {
            boolean if_captured = false;
            for (ElseIfStatementToken eist : ist.getElseIfSts()) {
                if (PrimitiveParser.BOOLEAN.parse(eval(eist.getCondition()).v())) {
                    if_captured = true;
                    eist.getStatements().forEach(this::runStatement);
                    break;
                }
            }
            if (!if_captured && ist.getElseSt() != null)
                ist.getElseSt().getStatements().forEach(this::runStatement);
        }
    }

    public THATObject eval(ExpressionsToken expression) {
        if (expression instanceof QuantaExpressionToken qet)
            return evalQuanta(qet);
        else if (expression instanceof BinaryOperatorToken bot)
            return evalBinary(bot);
        else if (expression instanceof StringToken st)
            return THOSEObjects.createValue(st.getValue());

        return null;
    }

    public THATObject evalQuanta(QuantaExpressionToken qet) {
        var iterator = qet.quantaIterator();

//        executionStack.peek().getB().
        THATObject variable = null;

        while (iterator.hasNext()) {

            if (iterator.isString()) {
                String value = iterator.nextString().getValue();
                assert variable == null : "Cant access a string as if it's a member of some variable...";
                variable = THOSEObjects.createValue(value);

            } else if (iterator.isMember()) {
                String value = iterator.nextMember().getValue();
                if (variable == null) /* ie we're probably accessing a local field */ {
                    variable = executionStack.peek().getB().seek(value);
                    if (variable == null) /* ie there is no such variable created by that name */
                        variable = THOSEObjects.createAfterReducing(value); // so we treat it as if it's a value
                } else /* ie we're accessing a field in variable */ {
                    var member = variable.getMember(value);
                    if (member == null) /* ie we are probably creating something? */ {
                        member = THOSEObjects.createValue(null);
                        variable.putMember(value, member);
                    }
                    variable = member;
                }

            } else if (iterator.isFunction()) {
                FunctionCallToken function = iterator.nextFunction();
                if (variable == null)
                    variable = evalFunction(function);
                else
                    variable = variable.seekFunction(function);
            }
        }
        return variable == null ? THOSEObjects.NULL : variable;
    }

    public THATObject evalBinary(BinaryOperatorToken bot) {
        return Operators.operate(eval(bot.getLeft()), bot.getOperator(), eval((bot.getRight())));
    }
    public THATObject evalFunction(FunctionCallToken fct) {
        List<FunctionEvaluator> list = executionEnvironment.getFunctionEvaluators();

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isApplicable(fct))
                return evaluator.apply(fct);

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isDigestible(fct))
                return evaluator.apply(fct);

        return THOSEObjects.NULL;
    }

}
