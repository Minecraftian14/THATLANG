package in.mcxiv;

import com.mcxiv.logger.tools.LogLevel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

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

    public static <Type> Type alsoPrtln(String msg, Type type) {
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

    private static StringBuilder atomicString = null;
    private static int cursor = 0;

    public static void redefineInput(String s) {
        if (atomicString == null) {
            atomicString = new StringBuilder();
            System.setIn(new InputStream() {
                @Override
                public int read() {
                    return cursor >= atomicString.length() ? -1 : atomicString.charAt(cursor++);
                }
            });
        }
        atomicString.append(s);
        if (!s.endsWith("\n")) atomicString.append('\n');
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
