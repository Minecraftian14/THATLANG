package in.mcxiv.interp;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.statements.StatementToken;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public abstract class Environment {

    final String[] args;
    public final AbstractThatVM vm;

    public PrintStream out = System.out;
    public PrintStream err = System.err;
    public InputStream in = System.in;

    protected Environment(String[] args, AbstractThatVM vm) {
        this.args = Objects.requireNonNull(args);
        this.vm = vm;
    }

    /**
     * Merges the given environment into the existing one,
     * replaces all duplicates with the new ones.
     */
    public void assertiveMerge(Environment environment) {
        for (ProgramFileToken programFile : environment.getProgramFiles()) {
            if (hasProgramFile(programFile))
                getProgramFiles().remove(programFile);
            addProgramFile(programFile);
        }
        for (ProgramToken program : environment.getPrograms()) {
            if (hasProgram(program))
                getPrograms().remove(program);
            addProgram(program);
        }
    }

    /**
     * Merges the given environment into the existing one,
     * ignores all duplicates with the new ones.
     */
    public void submissiveMerge(Environment environment) {
        for (ProgramFileToken programFile : environment.getProgramFiles())
            if (!hasProgramFile(programFile))
                addProgramFile(programFile);
        for (ProgramToken program : environment.getPrograms())
            if (!hasProgram(program))
                addProgram(program);
    }

    public abstract List<ProgramFileToken> getProgramFiles();

    public abstract void addProgramFile(ProgramFileToken fpt);

    public abstract boolean hasProgramFile(ProgramFileToken fpt);

    public abstract List<ProgramToken> getPrograms();

    public abstract void addProgram(ProgramToken fpt);

    public abstract boolean hasProgram(ProgramToken fpt);

    public abstract List<Evaluator<StatementToken>> getStatementEvaluators();

    public abstract List<FunctionEvaluator> getFunctionEvaluators();

}
