package in.mcxiv.utils;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LinkedList<Type, Dependency> extends AbstractList<Type> {

    private final Reference<Dependency> reference;
    private final Function<Dependency, Type> constructor;
    private final HashMap<Dependency, Type> cache;

    public LinkedList(List<Dependency> reference, Function<Dependency, Type> constructor) {
        this(Reference.of(reference), constructor);
    }

    public LinkedList(Map<Dependency, ?> reference, Function<Dependency, Type> constructor) {
        this(Reference.of(reference), constructor);
    }

    public LinkedList(Reference<Dependency> reference, Function<Dependency, Type> constructor) {
        this.reference = reference;
        this.constructor = constructor;
        this.cache = new HashMap<>();
        this.reference.forEach(dependency -> cache.put(dependency, this.constructor.apply(dependency)));
    }

    @Override
    public Type get(int index) {
        Dependency dependency = reference.get(index);
        Type type = cache.get(dependency);
        if (type != null) return type;
        type = constructor.apply(dependency);
        cache.put(dependency, type);
        return type;
    }

    @Override
    public int size() {
        return reference.size();
    }

    private abstract static class Reference<Dependency> extends AbstractList<Dependency> {

        public static <Dependency> Reference<Dependency> of(List<Dependency> reference) {
            return new ReferenceByList<>(reference);
        }

        public static <Dependency> List<Dependency> of(Map<Dependency, ?> reference) {
            return new ReferenceByMap<>(reference);
        }

        private static class ReferenceByList<Dependency> extends Reference<Dependency> {

            private final List<Dependency> list;

            public ReferenceByList(List<Dependency> list) {
                this.list = list;
            }

            @Override
            public Dependency get(int index) {
                return list.get(index);
            }

            @Override
            public int size() {
                return list.size();
            }
        }

        private static class ReferenceByMap<Dependency> extends Reference<Dependency> {

            private final Map<Dependency, ?> map;

            public ReferenceByMap(Map<Dependency, ?> map) {
                this.map = map;
            }

            @Override
            @SuppressWarnings("unchecked")
            public Dependency get(int index) {
                return (Dependency) map.keySet().toArray()[index];
            }

            @Override
            public int size() {
                return map.size();
            }
        }
    }
}
