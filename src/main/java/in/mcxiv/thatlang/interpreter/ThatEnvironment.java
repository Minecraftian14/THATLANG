package in.mcxiv.thatlang.interpreter;

import in.mcxiv.thatlang.FunctionToken;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.interpreter.functions.InputFunctions;
import in.mcxiv.thatlang.interpreter.functions.PrintFunctions;
import in.mcxiv.thatlang.interpreter.functions.ProgramFunctions;
import in.mcxiv.thatlang.interpreter.functions.UIFunctions;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.LinkedList;

import java.util.ArrayList;
import java.util.List;

public class ThatEnvironment extends AbstractEnvironment {

    public static final Evaluator<StatementToken> NO_OPERATION = statementToken -> {
    };

    final ArrayList<ProgramFileToken> programFiles;
    final ArrayList<ProgramToken> programs;
//    final LinkedList<Evaluator<StatementToken>, Class<?>> evaluators;
    final ArrayList<FunctionEvaluator> functions;

    public ThatEnvironment(AbstractVM vm) {
        this(new String[0], vm);
    }

    public ThatEnvironment(String[] args, AbstractVM vm) {
        super(args, vm);
        programFiles = new ArrayList<>();
        programs = new ArrayList<>();
//        evaluators = new LinkedList<>(StatementToken.STATEMENT_TYPES, this::evaluateStatement);
        functions = new ArrayList<>(List.of(
                new PrintFunctions(this),
                new InputFunctions(this),
                new ProgramFunctions(this),
                new UIFunctions(this)
        ));
    }

    private Evaluator<StatementToken> evaluateStatement(Class<?> dependency) {
        Evaluator<StatementToken> evaluator = NO_OPERATION;

//        switch (dependency) {
//            case
//        }

        return evaluator;
    }

    @Override
    public List<ProgramFileToken> getProgramFiles() {
        return programFiles;
    }

    @Override
    public void addProgramFile(ProgramFileToken fpt) {
        programFiles.add(fpt);
        for (ProgramToken programToken : fpt.getPrograms())
            addProgram(programToken);
        for (FunctionToken function : fpt.getFunctions())
            functions.add(FunctionEvaluator.createEvaluatorFromToken(this, function));
    }

    @Override
    public boolean hasProgramFile(ProgramFileToken fpt) {
        return programFiles.contains(fpt);
    }

    @Override
    public List<ProgramToken> getPrograms() {
        return programs;
    }

    @Override
    public void addProgram(ProgramToken pt) {
        programs.add(pt);
    }

    @Override
    public boolean hasProgram(ProgramToken pt) {
        return programs.contains(pt);
    }

    @Override
    public List<Evaluator<StatementToken>> getStatementEvaluators() {
        return null;
    }

    @Override
    public List<FunctionEvaluator> getFunctionEvaluators() {
        return functions;
    }
}
