package in.mcxiv.parser;

import in.mcxiv.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Node {

    Pair<Node, Class<?>> parent = null;
    ArrayList<Pair<Node, Class<?>>> children = new ArrayList<>();

    public Node() {
        this(null);
    }

    public Node(Node parent) {
        if (parent != null) parent.addChild(this);
    }

    public Node(Node parent, Node... children) {
        if (parent != null) parent.addChild(this);
        for (Node child : children) addChild(child);
    }

    public int noOfChildren() {
        return children.size();
    }

    public void addChild(Node node) {
        children.add(new Pair<>(node, node.getClass()));
        node.parent = new Pair<>(this, this.getClass());
    }

    public boolean hasChild(Class<? extends Node> clazz) {
        return children.stream()
                .anyMatch(pair -> pair.getB().equals(clazz));
    }

    public boolean hasChild(Node test) {
        return children.stream()
                .anyMatch(pair -> pair.getA().equals(test));
    }

    public Node get(int index) {
        return children.get(index).getA();
    }

    public Node get(Class<? extends Node> clazz) {
        return children.stream()
                .filter(pair -> clazz.isAssignableFrom(pair.getB()))
                .findFirst()
                .map(Pair::getA)
                .orElse(null);
    }

    public <Element extends Node> Element getExp(Class<Element> clazz) {
        Node node = children.stream()
                .filter(pair -> clazz.isAssignableFrom(pair.getB()))
                .findFirst()
                .map(Pair::getA)
                .orElse(null);
        return clazz.cast(node);
    }

    public void delete() {
        detach();
//        children.forEach(pair -> pair.getA().delete());
    }

    public void detach() {
        if (parent != null) parent.getA().children.removeIf(pair -> pair.getA().equals(this));
    }

    public List<Node> getChildren() {
        return children.stream().map(Pair::getA).toList();
    }

    public <Element> List<Element> getChildren(Class<Element> clazz) {
        return getChildren().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public void forEachChild(Consumer<Node> consumer) {
        children.forEach(pair -> consumer.accept(pair.getA()));
    }

    public int wildSearch(Class<? extends Node> clazz) {
        AtomicInteger count = new AtomicInteger(0);
        count.addAndGet((int) children.stream().filter(pair -> clazz.isInstance(pair.getA())).count());
        forEachChild(node -> count.addAndGet(node.wildSearch(clazz)));
        return count.get();
    }

    @Override
    public String toString() {
        return toExtendedString("children", getChildren());
    }

    public String toExtendedString(Object... fields) {
        StringBuilder builder = new StringBuilder("\"").append(getClass().getSimpleName()).append("\":").append('{');
        for (int i = 0; i + 1 < fields.length; i += 2) {
            builder.append(fields[i]).append(':').append(fields[i + 1].getClass().isArray() ? Arrays.toString((Object[]) fields[i + 1]) : fields[i + 1]);
            if (i < fields.length - 2) builder.append(", ");
        }

        if (fields.length % 2 == 1 && fields[fields.length - 1] == children) {
            if (children.size() == 1) {
                builder.append("\"children_").append(children.get(0).getA());
            } else if (children.size() > 1) {
                builder.append("\"children\":{");
                IntStream.range(0, children.size() - 1)
                        .mapToObj(children::get).forEach(pair -> builder.append(pair.getA()).append(','));
                builder.append(children.get(children.size() - 1).getA());
                builder.append('}');
            } else builder.append("\"children\"=null");
        }

        return builder.append('}').toString();
    }
}
