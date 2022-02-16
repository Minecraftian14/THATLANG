package in.mcxiv.utils;

import java.util.Arrays;

public class Strings {

    public static String camelToUpper(String input) {
        return new String(camelToUpper(input.toCharArray()));
    }

    public static char[] camelToUpper(char[] input) {
        char[] output = new char[input.length + countUpperCase(input) - (Character.isUpperCase(input[0]) ? 1 : 0)];
        output[0] = Character.toUpperCase(input[0]);
        for (int i = 1, o = 1; i < input.length; i++, o++) {
            if (Character.isUpperCase(input[i]))
                output[o++] = '_';
            output[o] = Character.toUpperCase(input[i]);
        }
        return output;
    }

    public static int countUpperCase(String input) {
        return countUpperCase(input.toCharArray());
    }

    public static int countUpperCase(char[] input) {
        int output = 0;
        for (int i = 0; i < input.length; i++)
//            if (input[i] >= 'A' && input[i] <= 'Z')        Which if is faster?
            if (Character.isUpperCase(input[i]))
                output++;
        return output;
    }

    public static String getLastWord(String input, char delimiter) {
        return new String(getLastWord(input.toCharArray(), delimiter));
    }

    public static char[] getLastWord(char[] input, char delimiter) {
        int sx = input.length - 1;
        while (sx-- > 0) if (input[sx] == delimiter) break;
        return Arrays.copyOfRange(input, ++sx, input.length);
    }

    public static String exceptLastWord(String input, char delimiter) {
        return new String(exceptLastWord(input.toCharArray(), delimiter));
    }

    public static char[] exceptLastWord(char[] input, char delimiter) {
        int sx = input.length - 1;
        while (sx-- > 0) if (input[sx] == delimiter) break;
        return Arrays.copyOf(input, sx);
    }
}
