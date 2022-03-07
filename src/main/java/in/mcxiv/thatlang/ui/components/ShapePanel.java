package in.mcxiv.thatlang.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class ShapePanel extends JPanel {

    public enum ShapeType {
        Rectangle, Oval, Polygon
    }

    ShapeType type;
    Shape shape;

    public ShapePanel(ShapeType type) {
        this(type, null);
    }

    public ShapePanel(ShapeType type, Shape shape) {
        this.type = type;
        this.shape = shape;
        setOpaque(false);
        setBackground(new Color(0x0, true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getForeground());
        switch (type) {
            case Rectangle -> g.fillRect(0, 0, getWidth(), getHeight());
            case Oval -> g.fillOval(0, 0, getWidth(), getHeight());
            case Polygon -> ((Graphics2D) g).fill(AffineTransform.getScaleInstance(getWidth(), getHeight()).createTransformedShape(shape));
        }
    }
}