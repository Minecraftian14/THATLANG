package in.mcxiv.thatlang.natives;

import in.mcxiv.external.parsers.json.JSONNode;
import in.mcxiv.external.parsers.json.JSONParser;
import in.mcxiv.external.parsers.yaml.YAMLNode;
import in.mcxiv.external.parsers.yaml.YAMLParser;
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
        String value = getValue();
        THATObject object = null;
        if ("json".equals(language)) {
            JSONNode node = JSONParser.json.parse(value);
            object = node.interpret(vm);
        } else if ("yaml".equals(language)) {
            YAMLNode node = YAMLParser.yaml.parse(value);
            object = node.interpret(vm);
        }
        if (object == null)
            object = THOSEObjects.createValue(value);
        object.putObjectData("LANGUAGE_NAME", language);
        object.putMember("langauge", THOSEObjects.createValue(language));
        return object;
    }

    public static class ExternalCodeParser extends MultilineStringParser {

        public static final ExternalCodeParser externalCode = new ExternalCodeParser();

        private static final Pattern rx_language = Pattern.compile("^<!DOCTYPE[ ]+(\\w+)>");

        private ExternalCodeParser() {
            super("<%", "%>");
        }

        @Override
        public ExternalCodeToken __parse__(ParsableString string, Node parent) {
            MultilineStringToken node = super.__parse__(string, null);
            if (node == null) return null;

            String value = node.getValue().strip();

            String language = "";
            Matcher matcher = rx_language.matcher(value);
            if (matcher.find()) {
                language = matcher.group(1);
                value = value.substring(matcher.group().length());
            }

            return new ExternalCodeToken(parent, language, value);
        }
    }
}
