package in.mcxiv.thatlang.interpreter.functions;

import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.interpreter.AbstractEnvironment;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import in.mcxiv.thatlang.interpreter.VariableScope;
import in.mcxiv.thatlang.ui.EasyCanvas;
import in.mcxiv.thatlang.ui.RootCanvas;
import in.mcxiv.thatlang.ui.components.ShapePanel;
import in.mcxiv.thatlang.ui.components.primitives.ComponentBinder;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import javax.swing.*;
import java.util.List;

public class UIFunctions extends FunctionEvaluator {

    public static final String UIFunctionCall = "UI";
    private static final String UI_CONTEXT = "ui context";
    private static final String ROOT_UI_CONTEXT = "root ui context";

    public UIFunctions(AbstractEnvironment environment) {
        super(environment);
    }

    @Override
    public boolean isApplicable(FunctionCallToken fct) {
        return UIFunctionCall.equals(fct.getValue());
    }

    @Override
    public THATObject apply(FunctionCallToken fct) {
        THATObject canvas = resolveCanvas();
        RootCanvas root = findARootCanvas();
        if (root != null) {
            root.repaint();

            var expressions = fct.getArguments().getExpressions();

            if (expressions.size() >= 1)
                root.setTitle(expressions.get(0).interpret(environment.vm).v());

            if (expressions.size() >= 3)
                root.setSize(
                        (int) expressions.get(1).interpret(environment.vm).value,
                        (int) expressions.get(2).interpret(environment.vm).value
                );
        }
        return canvas;
    }

    private THATObject resolveCanvas() {
        var pair = environment.vm.executionStack.peek();
        assert pair.getA() != null;
        VariableScope scope = pair.getB();

        THATObject object = scope.seek(UI_CONTEXT);
        if (object != null) return object;
//      If this program itself doesnt have an easy canvas, it can definitely
//      not be a root, as a canvas is added along with a root if added.

//      Well, then let's try searching which parent caller has the canvas.
        RootCanvas root = findASuperiorRootCanvas();

        if (root == null)
//      If there is no such patrent with a canvas, it's time ti make this
//      program a root canvas xD
        {
            root = new RootCanvas("THATLANG Program");
            scope.addVariable(THOSEObjects.create("ui", ROOT_UI_CONTEXT, root));
            object = createCanvas(root.canvas);
            scope.addVariable(object);
            return object;
        }

//      Well... If there's already a root out there, we can only make this program a canvas
        EasyCanvas superiorCanvas = getSuperiorCanvas();
        EasyCanvas canvas = new EasyCanvas();
        assert superiorCanvas != null;
        superiorCanvas.add(canvas);
        object = createCanvas(canvas);
        scope.addVariable(object);
        return object;
    }

    private THATObject createCanvas(EasyCanvas canvas) {
        THATObject object = THOSEObjects.create("ui", UI_CONTEXT, canvas);
        object.addFunction(FunctionEvaluator.anonymous(environment, "box", fct -> newRect(canvas)));
        object.addFunction(FunctionEvaluator.anonymous(environment, "button", fct -> newButton(fct, canvas)));
        return object;
    }

    private THATObject newRect(EasyCanvas canvas) {
        ComponentBinder binder = new ComponentBinder(canvas.add(new ShapePanel(ShapePanel.ShapeType.Rectangle)));
        canvas.addDrawable(binder);
        return binder.getObject();
    }

    private THATObject newButton(FunctionCallToken fct, EasyCanvas canvas) {
        List<ExpressionsToken> expressions = fct.getArguments().getExpressions();
        String name = expressions.size() < 1 ? "Button" : expressions.get(0).interpret(environment.vm).v();

        JButton button = new JButton(name);
        if (expressions.size() > 1)
            if (expressions.get(1) instanceof QuantaExpressionToken qet)
                button.addActionListener(e -> qet.interpret(environment.vm));


        ComponentBinder binder = new ComponentBinder(button);
        canvas.add(button);
        canvas.addDrawable(binder);
        return binder.getObject();
    }

    private RootCanvas findASuperiorRootCanvas() {
        var stack = environment.vm.executionStack;
        for (int i = stack.size() - 2; i >= 0; --i) {
            THATObject object = stack.get(i).getB().seek(ROOT_UI_CONTEXT);
            if (object != null) return (RootCanvas) object.value;
        }
        return null;
    }

    private RootCanvas findARootCanvas() {
        var stack = environment.vm.executionStack;
        for (int i = stack.size() - 1; i >= 0; --i) {
            THATObject object = stack.get(i).getB().seek(ROOT_UI_CONTEXT);
            if (object != null) return (RootCanvas) object.value;
        }
        return null;
    }

    private EasyCanvas getSuperiorCanvas() {
        var stack = environment.vm.executionStack;
        for (int i = stack.size() - 2; i >= 0; --i) {
            THATObject object = stack.get(i).getB().seek(UI_CONTEXT);
            if (object != null) return (EasyCanvas) object.value;
        }
        RootCanvas root = findASuperiorRootCanvas();
        if (root != null) return root.canvas;
        return null;
    }
}
