package in.mcxiv;

import com.mcxiv.logger.tools.LogLevel;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestSuite {

    public static void setLevel(LogLevel level) {
        assertDoesNotThrow(() -> Class.forName(CentralRepository.class.getName()));
        level.activate();
    }

    public static <Type> Type alsoPrtln(Type type) {
        System.out.print(">> ");
        System.out.println(type);
        return type;
    }

    public static <Type> Type alsoPrtln(String  msg, Type type) {
        System.out.printf(">> %s >>", msg);
        System.out.println(type);
        return type;
    }

//    public static  <Type> Type alsoPrt(Type type) {
//        System.out.print(type);
//        return type;
//    }

    public static <T> T pj(T obj) {
        pj(String.valueOf(obj));
        return obj;
    }

    private static void pj(String str) {
        char[] chars = str.toCharArray();
        StringBuilder b = new StringBuilder();
        int depth = 0;

        for (char c : chars) {
            if (c == '{' || c == '[') {
                b.append(" {\n");
                depth++;
                for (int i = 0; i < depth; i++, b.append("  ")) ;
            } else if (c == '}' || c == ']') {
                b.append("\n");
                depth--;
                for (int i = 0; i < depth; i++, b.append("  ")) ;
                b.append("}");
            } else if (c == ',') {
                b.append("\n");
                for (int i = 0; i < depth - 1; i++, b.append("  ")) ;
                b.append(" ");
            } else {
                b.append(c);
            }
        }

        System.out.println(b);
    }

    public static void redefineInput(String s) {
    System.setIn(new ByteArrayInputStream(s.getBytes()));
    }

    public static void bindToOutput(StringBuilder builder) {
        OutputStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                builder.append(((char) b));
                out.write(b);
            }
        }));
    }
}
