package in.mcxiv.transpiler.templates;

import in.mcxiv.thatlang.ProgramToken;

public class THATProgramTemplate extends Template {
    public THATProgramTemplate() {
        super("""
                public /* non-static */ class %PROGRAM_NAME% extends THATProgram {
                    public void run() {
                        var ctx = new %PROGRAM_NAME%Context();
                        
                        ctx.lim = 10;
                        ctx.ini = 0;
                        for (ctx.i = ini; ctx.i < lim; ctx.i++) {
                            MainProgram.this.loadedFunctions.get("print").run();
                            // or MainProgram.this.runFunction("print"); // runFunction signature is String name, Object...args
                        }
                        MainProgram.this.startProgram("UtilProgram", ctx, MainProgram.this.environment);
                    }
                                
                    public class %PROGRAM_NAME%Context extends THATContext {
                        %LOCAL_VARIABLES%
                    }
                }
                """);
    }

    public void injectProgramName(String name) {
        inject("PROGRAM_NAME", name);
    }

    public void injectProgram(ProgramToken programToken) {

    }

}
