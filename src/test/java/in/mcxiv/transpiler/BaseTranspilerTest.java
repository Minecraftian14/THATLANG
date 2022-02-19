package in.mcxiv.transpiler;

import in.mcxiv.thatlang.ProgramFileToken;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;

class BaseTranspilerTest {
    @Test
    void test() {

        ProgramFileToken.ProgramFileParser parser = ProgramFileToken.ProgramFileParser.instance;
        ProgramFileToken token = parser.parse("""
                main:
                    var Value = Hello
                    pln(Value)
                    var Name = scan()
                    pln(Name)
                """);
        token.setProgramFileName("TestProgram");

        BaseTranspiler transpiler = new BaseTranspiler(token);

        String make = transpiler.make(0);
        System.out.println(make);
        assertDoesNotThrow(() -> Files.write(Path.of("D:\\Projects\\JAVA\\THATLANG\\src\\main\\java\\TestProgram.java"), make.getBytes()));
    }
}