package in.mcxiv.thatlang.blocks;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.SpacesToken.SpacesParser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.thatlang.statements.StatementToken.StatementParser;
import in.mcxiv.utils.Cursors;

import static in.mcxiv.parser.power.PowerUtils.*;

public class BracedBlockToken extends BlockToken {

    public BracedBlockToken(StatementToken... statements) {
        super(statements);
    }

    public static final class BracedBlockParser implements Parser<BracedBlockToken> {

        public static final BracedBlockParser bracedBlock = new BracedBlockParser();

        private static final Parser<?> stepParser = compound(
                either(SpacesParser.spaces, StatementParser.statement),
                word("\n")
        );

        private static final Parser<?> parser =
                compound(
                        word("{"),
                        repeatable(
                                block(
                                        stepParser
                                )),
                        word("}")
                );

        private BracedBlockParser() {
        }

        @Override
        public BracedBlockToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getChar(string) != '{') return null;

            int start = string.getCursor();

            int depth = 0; // number of braces opened, number of braces remaining to be closed.
//            boolean are_we_in_a_string = false; // Gotta to ignore { and } lying inside ""
//            boolean are_we_in_a_char = false; // Gotta to ignore { and } lying inside ''

            while (Cursors.bound(string)) {
                char c = Cursors.getCharAndNext(string);

                if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) break;
                } else if (c == '"') {
                    while (Cursors.bound(string) && Cursors.getCharAndNext(string) != '"') ;
                } else if (c == '\'') {
                    while (Cursors.bound(string) && Cursors.getCharAndNext(string) != '\'') ;
                }
            }

            string = string.subSequencePS(start, string.getCursor());
            Node node = parser.parse(string);
            if (node == null) return null;

            StatementToken[] array = node // The node returned by CompoundParser
                    .get(1) // The repeatable parser node
                    .getChildren() // Every node returned by CompoundParser inside the repeatable parser
                    .stream() // Each of those compound node contain only one node, returned by stepParser
                    .map(ch -> ch.get(0)) // interreplacing the stepParser node which is also a compound node by the first node in step, ie, whatever was returned by EitherParser
                    .filter(ch -> ch instanceof StatementToken)
                    .map(ch -> ((StatementToken) ch))
                    .toArray(StatementToken[]::new);

            BracedBlockToken token = new BracedBlockToken(array);
            if(parent!=null)
                parent.addChild(token);
            return token;
        }
    }

}
