package in.mcxiv.thatlang;

import com.mcxiv.logger.tools.LogLevel;
import in.mcxiv.CentralRepository;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.ThatVM;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class THATRuntime {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            CentralRepository.err("Arguments Problem", "No Arguments were provided to the interpreter.");
            return;
        }

        List<File> files = Arrays.stream(args)
                .map(File::new)
                .filter(File::isFile)
                .filter(file -> file.getName().startsWith("that."))
                .toList();

        if (args.length != files.size()) {
            CentralRepository.err("IO Problem", "Certain files requested seem to be missing.");
            return;
        }

        AbstractVM vm = new ThatVM();

        files.stream().map(THATRuntime::mapToStringSafely)
                .map(ProgramFileToken.ProgramFileParser.programFile::parse)
                .forEach(vm::load);

        vm.run();
    }

    private static String mapToStringSafely(File file) {
        FileInputStream stream = null;
        String value = null;
        try {
            stream = new FileInputStream(file);
            value = new String(stream.readAllBytes());
        } catch (IOException e) {
            LogLevel.DEBUG.act(e::printStackTrace);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    LogLevel.DEBUG.act(e::printStackTrace);
                }
            }
        }
        if (value == null) return null;
        return value.replaceAll("\r\n", "\n");
    }
}