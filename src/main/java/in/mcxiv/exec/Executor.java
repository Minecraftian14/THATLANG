package in.mcxiv.exec;

import com.mcxiv.logger.decorations.Format;
import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.exec.results.SuitableSupplier;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.DirUtils;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static in.mcxiv.CentralRepository.*;
import static in.mcxiv.utils.Strings.exceptLastWord;

public class Executor {

    public static final String LOG_HEAD = getLogHead();

    static {
        prt(LOG_HEAD, UNDER_STATIC_INITIALIZER);

        JAVA_HOME = System.getProperty("java.home");
        JDK_HOME = System.getProperty("java.home") + File.separator + "bin";
        JAVA_TEMP_DIR = System.getProperty("java.io.tmpdir");

        String version = __JustRunAFreakingCommandPlease("java", "--version").toLowerCase();
        prt("TESTING JAVA", "%s : %s".formatted(version, version.equals("null") ? "FAILED" : "SUCCESS"));
        prt("TESTING JDK", "has JDK : " + (version.contains("jdk") ? "SUCCESS" : "FAILED"));

        if (!version.contains("jdk"))
            err("NO JDK FOUND", "Executor is useless in this case...");

        prt("Java Name", JAVA_HOME);
        prt("JDK Name", JDK_HOME);
        prt("Temp Name", JAVA_TEMP_DIR);

        LogLevel.DEBUG.act(() -> System.getProperties().forEach(Executor::prt));

        DirUtils.assertDir(JAVA_TEMP_DIR, APP_NAME, LOG_HEAD);
    }

    private static final AtomicInteger TASK_COUNTER = new AtomicInteger(0);

    @Format({":: :@68A629 #F %*40s: ::", ":: :@A8F6A9 #0 n %-100s: ::"})
    public static void prt(Object type, Object msg) {
        log.prt(type, msg);
    }

    /**
     * Usage tip, provide the executable separately...
     * for example __JustRunAFreakingCommandPlease("java", "-version");
     */
    private static String __JustRunAFreakingCommandPlease(String... args) {
        return Try.opt(() -> new ProcessBuilder(args).start())
                .map(Process::getInputStream)
                .map(Scanner::new)
                .map(scanner -> CompletableFuture.supplyAsync(scanner::nextLine))
                .map(cf -> Try.get(() -> cf.get(10000, TimeUnit.MILLISECONDS)))
                .orElse("null");
    }

    public static String runCLCommand(String exec, String argMsg, File startAt) {
        LogLevel.DEBUG.act(() -> prt("CL Command Request", exec + " " + argMsg));
        String result = "null";
        try (
                InputStream stream = new ProcessBuilder(exec, argMsg).directory(startAt).start().getInputStream();
                Scanner scanner = new Scanner(stream);
        ) {
            result = CompletableFuture.supplyAsync(() -> {
                        if (scanner.hasNextLine())
                            return scanner.nextLine();
                        return "null";
                    })
                    .get(10000, TimeUnit.MILLISECONDS);
        } catch (IOException | ExecutionException | InterruptedException | TimeoutException e) {
            LogLevel.DEBUG.act(e::printStackTrace);
        }
        return result;
    }

    private static File createFile() {
        String taskName = getNewTaskName();
        File taskFile = new File(DirUtils.formPath(JAVA_TEMP_DIR, APP_NAME, LOG_HEAD, taskName + ".java"));
        Try.run(taskFile::createNewFile);
        LogLevel.DEBUG.act(() -> prt("TASK FILE WRITTEN AT", taskFile));
        return taskFile;
    }

    private static void writeSoutProgram(File target, String expression) {
        try (
                FileWriter fileWriter = new FileWriter(target);
                BufferedWriter writer = new BufferedWriter(fileWriter);
        ) {
            writer.write("""
                    public class %s {
                        public static void main(String[] args) {
                            System.out.print(%s);
                        }
                    }
                    """.formatted(exceptLastWord(target.getName(), '.'), expression));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File compileProgram(File target) {
        runCLCommand("javac", target.getName(), target.getParentFile());
        String className = target.getName();
        className = exceptLastWord(className, '.') + ".class";
        return new File(DirUtils.formPath(target.getParent(), className));
    }

    public static String runProgram(File target) {
        return runCLCommand("java", exceptLastWord(target.getName(), '.'), target.getParentFile());
    }

    public static String populateExpression(String expression) {
        return expression
                .replaceAll("Math", Math.class.getName()); // Was not really required, just put as an example.
    }

    public static SuitableSupplier get(String expression) {
        File file = createFile();
        writeSoutProgram(file, populateExpression(expression));
        file = compileProgram(file);
        String result = runProgram(file);
        return () -> result;
    }

    public static String getNewTaskName() {
        synchronized (TASK_COUNTER) {
            return "TASK" + TASK_COUNTER.getAndIncrement();
        }
    }

}
