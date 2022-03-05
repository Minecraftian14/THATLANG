package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class MultilineLineCommentToken extends CommentToken {

    public MultilineLineCommentToken(String value) {
        super(value);
    }

    public MultilineLineCommentToken(Node parent, String value) {
        super(parent, value);
    }

    public static class MultilineCommentParser implements Parser<MultilineLineCommentToken> {

        public static final Parser<MultilineLineCommentToken> multiLineComment = new MultilineCommentParser();

        private MultilineCommentParser() {
        }

        @Override
        public MultilineLineCommentToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '/')
                if (Cursors.bound(string) && Cursors.getCharAndNext(string) != '*')
                    return null;
            String everything = Cursors.fetchEverythingUpTo(string, "*/");
            if(everything == null) return null;
            string.moveCursor(2);
            return new MultilineLineCommentToken(parent, everything);
        }
    }
}
