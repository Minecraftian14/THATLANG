package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalCodeToken extends MultilineStringToken {

    private final String language;

    public ExternalCodeToken(String code, String language) {
        this(null, language, code);
    }

    public ExternalCodeToken(Node parent, String language, String code) {
        super(parent, code);
        this.language = language;
    }

    @Override
    public String toString() {
        return toExtendedString("language", language, "code", getValue());
    }

    public String getCode() {
        return getValue();
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject value = THOSEObjects.createValue(getValue());
        value.putObjectData("LANGUAGE_NAME", language);
        value.putMember("langauge", THOSEObjects.createValue(language));
        return value;
    }

    public static class ExternalCodeParser extends MultilineStringParser {

        public static final ExternalCodeParser externalCode = new ExternalCodeParser();

        private static final Pattern rx_language = Pattern.compile("<!DOCTYPE (\\w+)>");

        private ExternalCodeParser() {
            super("<%", "%>");
        }

        @Override
        public ExternalCodeToken __parse__(ParsableString string, Node parent) {
            MultilineStringToken node = super.__parse__(string, null);
            if (node == null) return null;

            String language = "";
            Matcher matcher = rx_language.matcher(node.getValue());
            if (matcher.find())
                language = matcher.group(1);

            return new ExternalCodeToken(parent, language, node.getValue());
        }
    }
}
