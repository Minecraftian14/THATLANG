package in.mcxiv.transpiler.templates;

public class Template {

    protected String formatString;

    public Template(String formatString) {
        this.formatString = formatString;
    }

    public void inject(String name, String value) {
        formatString = formatString
                .replace("%" + name + "%", value);
    }

    public void add(String name, String value) {
        name = "%" + name + "%";
        formatString = formatString
                .replace(name, value + "\n" + name);
    }

    @Override
    public String toString() {
        return formatString.replaceAll("%\\w+%", "");
    }
}
