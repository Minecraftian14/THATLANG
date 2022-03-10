package in.mcxiv.thatlang.interpreter;

import in.mcxiv.interpreter.BaseInterpreter;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractVM extends BaseInterpreter<AbstractVM> {

    public static final String EXECUTION_STOPPED = "EXECUTION_STOPPED";

    public final AbstractEnvironment executionEnvironment;

    public ExecutorService executor;

    private final ThreadLocal<Stack<Pair<ProgramToken, VariableScope>>> stackHolder;
    private final ThreadLocal<ArrayList<Future<?>>> launchedPrograms;

    public AbstractVM(AbstractEnvironment environment) {
        this();
        executionEnvironment.assertiveMerge(environment);
    }

    public AbstractVM() {
        executionEnvironment = new ThatEnvironment(this);
        stackHolder = ThreadLocal.withInitial(Stack::new);
        launchedPrograms = ThreadLocal.withInitial(ArrayList::new);
    }

    public Stack<Pair<ProgramToken, VariableScope>> getExecutionStack() {
        return stackHolder.get();
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
        executor = Executors.newFixedThreadPool(4/*Runtime.getRuntime().availableProcessors()*/);
        interpret(file, this);
        executor.shutdown();
    }

    public void run(String programName) {
        ProgramToken program = executionEnvironment.getPrograms().stream().filter(pt -> programName.equals(pt.getProgramName())).findFirst().orElseThrow(() -> new RuntimeException("No such program called %s defined".formatted(programName)));
        run(program);
    }

    public void run(ProgramToken program) {
        program.interpret(this);
        launchedPrograms.get().forEach(future -> Try.Run(future::get));
    }

    public void __STOP_THIS__() {
        throw new RuntimeException(EXECUTION_STOPPED);
    }

    public void launch(ProgramToken program) {
        launchedPrograms.get().add(executor.submit(() -> run(program)));
    }
}
