package in.mcxiv.utils;

import java.io.File;

public class DirUtils {

    public static void assertDir(String... strings) {
        File directory = new File(DirUtils.formPath(strings));
        if (!directory.exists())
            directory.mkdirs();
    }

    public static String formPath(String... strings) {
        for (int i = 0; i < strings.length; i++)
            strings[i] = trimSeperator(strings[i].strip());
        StringBuilder builder = new StringBuilder();
        for (int i = 0, s = strings.length - 1; i < s; i++)
            builder.append(strings[i]).append(File.separator);
        return builder.append(strings[strings.length - 1]).toString();
    }

    public static String trimSeperator(String string) {
        if (string.startsWith("\\") || string.startsWith("/") || string.startsWith(File.separator))
            string = string.substring(1);
        if (string.endsWith("\\") || string.endsWith("/") || string.endsWith(File.separator))
            string = string.substring(0, string.length() - 1);
        return string;
    }

}
