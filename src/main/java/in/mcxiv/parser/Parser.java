package in.mcxiv.parser;

import in.mcxiv.utils.Cursors;

public interface Parser<Element extends Node> {

    default Element parse(String string) {
        return parse(new ParsableString(string), null);
    }

    default Element parse(ParsableString string) {
        return parse(string, null);
    }

    default Element parse(ParsableString string, Node parent) {
        if(!Cursors.bound(string)) return null;
        int backUp = string.getCursor();
        Element element = __parse__(string, parent);
        if(element == null) string.setCursor(backUp);
        return element;
    }

    Element __parse__(ParsableString string, Node parent);

}
