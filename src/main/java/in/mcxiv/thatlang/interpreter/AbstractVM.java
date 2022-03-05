package in.mcxiv.thatlang.interpreter;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.utils.Pair;
import in.mcxiv.interpreter.BaseInterpreter;

import java.util.Arrays;
import java.util.Stack;

public abstract class AbstractVM extends BaseInterpreter<AbstractVM> {

    public final AbstractEnvironment executionEnvironment;

    public final Stack<Pair<ProgramToken, VariableScope>> executionStack = new Stack<>();

    public AbstractVM(AbstractEnvironment environment) {
        this();
        executionEnvironment.assertiveMerge(environment);
    }

    public AbstractVM() {
        executionEnvironment = new ThatEnvironment(this);
    }

    public void load(ProgramFileToken file) {
        executionEnvironment.addProgramFile(file);
        Arrays.stream(file.getPrograms())
                .filter(program -> program.getProgramName().equals("init"))
                .forEach(programToken -> programToken.interpret(this));
    }

    public void run() {
        run(executionEnvironment.getProgramFiles().get(0));
    }

    public void run(ProgramFileToken file) {
        interpret(file, this);
    }
}
