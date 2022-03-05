package in.mcxiv.thatlang.ui;

import java.awt.*;

public abstract class AbstractDrawable implements Drawable {

    Stroke stroke;
    Color fill;

    public AbstractDrawable() {
        this(new BasicStroke(), Color.BLACK);
    }

    public AbstractDrawable(Stroke stroke, Color fill) {
        this.stroke = stroke;
        this.fill = fill;
    }

    public void draw(Graphics2D g, EasyCanvas canvas) {
        g.setStroke(stroke);
        g.setColor(fill);
        drawShape(g);
    }

    protected abstract void drawShape(Graphics2D g);

}
