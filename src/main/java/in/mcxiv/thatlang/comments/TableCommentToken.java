package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.word;

public class TableCommentToken extends CommentToken {

    String[] columnsRowHeads;
    ArrayList<String[]> rowsContent = new ArrayList<>();
    String[] columnsRowTails;
    boolean isPerfect = true;

    public TableCommentToken(String everything, String[] columnsRowHeads, ArrayList<String[]> rowsContent, String[] columnsRowTails) {
        this(null, everything, columnsRowHeads, rowsContent, columnsRowTails);
    }

    public TableCommentToken(Node parent, String everything, String[] columnsRowHeads, ArrayList<String[]> rowsContent, String[] columnsRowTails) {
        super(parent, everything);
        this.columnsRowHeads = columnsRowHeads;
        this.rowsContent = rowsContent;
        this.columnsRowTails = columnsRowTails;

        if (columnsRowHeads.length != columnsRowTails.length)
            isPerfect = false;
        if (isPerfect) for (String[] strings : rowsContent)
            if (strings.length != columnsRowHeads.length) {
                isPerfect = false;
                break;
            }
    }

    public static class TableCommentParser implements Parser<TableCommentToken> {

        public static final Parser<TableCommentToken> tableComment = new TableCommentParser();

        private static final Pattern rx_topRow = Pattern.compile("/([=]+(?:[#][=]+)*)+\\\\");
        private static final Pattern rx_midRow = Pattern.compile("\\|([^|\n\r]+(?:[|][^|\n\r]+)*)+\\|");
        private static final Pattern rx_botRow = Pattern.compile("\\\\([=]+(?:[\\^][=]+)*)+/");

        private TableCommentParser() {
        }

        @Override
        public TableCommentToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.matches(string, "/="))
                return null;

            int start = string.getCursor();

            String elevation = Cursors.getElevation(string);
            Parser<?> stepParser = compound(word("\n" + elevation + "|"));

            // Parsing the first row of characters
            Matcher matcher = rx_topRow.matcher(string.splitAtCursor());
            if (!matcher.find()) return null;
            string.moveCursor(matcher.group().length());

            String[] columnsRowHeads = matcher.group(1).split("#");

            ArrayList<String[]> rows = new ArrayList<>();
            Node step;
            while ((step = stepParser.parse(string)) != null) {
                string.moveCursor(-1);
                matcher = rx_midRow.matcher(string.splitAtCursor());
                if (!matcher.find()) return null;
                string.moveCursor(matcher.group().length());
                rows.add(matcher.group(1).split("\\|"));
            }
            string.moveCursor(1);
            Cursors.skipSpaces(string);

            matcher = rx_botRow.matcher(string.splitAtCursor());
            if (!matcher.find()) return null;

            string.moveCursor(matcher.group().length());
            String[] columnsRowTails = matcher.group(1).split("\\^");

            return new TableCommentToken(parent, (String) string.subSequence(start, string.getCursor() - start), columnsRowHeads, rows, columnsRowTails);
        }
    }
}
