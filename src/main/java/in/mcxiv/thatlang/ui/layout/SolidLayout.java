package in.mcxiv.thatlang.ui.layout;

import java.awt.*;
import java.util.HashMap;

public class SolidLayout implements LayoutManager2 {

    private HashMap<Component, SolidConstraint> map = new HashMap<>();

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints == null) return;
        if (constraints instanceof SolidConstraint sc)
            map.put(comp, sc);
        else
            throw new IllegalArgumentException("Only SolidConstraint accepted, not a " + constraints.getClass().getSimpleName());
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return .5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return .5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        map.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        // TODO: This layout man arranges the children wrt to parents size.
        //  So how am I supposed to calculate a size for the parent wrt children xD?
        return new Dimension();
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        // TODO: same as the todo in preferredLayoutSize().
        return new Dimension();
    }

    @Override
    public void layoutContainer(Container parent) {
        for (var entry : map.entrySet()) {
            SolidConstraint c = entry.getValue();
            entry.getKey().setBounds(
                    c.getX(parent.getWidth()),
                    c.getY(parent.getHeight()),
                    c.getW(parent.getWidth()),
                    c.getH(parent.getHeight())
            );
        }
    }

    public SolidConstraint getConstraints(Component component) {
        return map.get(component);
    }
}
