package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.utils.Cursors;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.word;

public class BoxedMultilineLineCommentToken extends CommentToken {

    public BoxedMultilineLineCommentToken(String value) {
        super(value);
    }

    public BoxedMultilineLineCommentToken(Node parent, String value) {
        super(parent, value);
    }

    public static class BoxedMultilineCommentParser implements Parser<BoxedMultilineLineCommentToken> {

        public static final Parser<BoxedMultilineLineCommentToken> boxedMultiLineComment = new BoxedMultilineCommentParser();

        private BoxedMultilineCommentParser() {
        }

        @Override
        public BoxedMultilineLineCommentToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.matches(string, "/**"))
                return null;

            String elevation = Cursors.getElevation(string);
            string.moveCursor(3);

            String everything = Cursors.fetchEverythingUpTo(string, "\n");
            if (everything == null) return null;
            StringBuilder text = new StringBuilder(everything).append('\n');

            Parser<?> stepParser = compound(word("\n" + elevation + " *"), (s, p) -> {
                if(Cursors.matches(s, "*/")) return null;
                String e = Cursors.fetchEverythingUpTo(s, "\n");
                if (e != null) return new StringValueNode(p, e);
                return null;
            });

            Node node;
            while ((node = stepParser.parse(string)) != null)
                text.append(((StringValueNode) node.get(1)).getValue()).append("\n");
            string.moveCursor(1);
            Cursors.skipSpaces(string);

            if (!Cursors.matches(string, "**/"))
                return null;
            string.moveCursor(3);

            return new BoxedMultilineLineCommentToken(parent, text.toString());
        }
    }
}
