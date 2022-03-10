package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.external.parsers.yaml.primitive.DocumentToken.DocumentParser;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.PowerUtils;

import static in.mcxiv.parser.power.PowerUtils.*;

public class FileToken extends SimpleNestableToken {

    public FileToken(Node parent, Node... children) {
        super(parent, children);
    }

    public static final class FileParser implements Parser<FileToken> {

        public static final Parser<FileToken> file = new FileParser();

        private static final Parser<?> parser = block(compound(
                optional(word("---")),
                repeatable(block(DocumentParser.document)),
                optional(word("..."))
        ));

        private FileParser() {
        }

        @Override
        public FileToken __parse__(ParsableString string, Node parent) {
            return null;
        }
    }

}
