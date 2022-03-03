package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.LooseInlineParser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.Node;
import in.mcxiv.utils.LinkedList;
import thatlang.core.THOSEOperatorsPrototype;

import java.util.ArrayList;

import static in.mcxiv.parser.power.PowerUtils.*;

/**
 * things like 1+2/3+4 will convert to (1+(2/3))+4
 */
class MultiOperandParser implements Parser<BinaryOperatorToken> {

    public static final MultiOperandParser multiOperand = new MultiOperandParser();

    private static final Parser parser = compound(
            SimpleSafeNonRecursiveExpressionParser.safeExpression,
            repeatable(compound(
                    either(new LinkedList<>(THOSEOperatorsPrototype.KNOWN_OPERATORS, LooseInlineParser::new)),
                    SimpleSafeNonRecursiveExpressionParser.safeExpression
            ))
    );

    private MultiOperandParser() {
    }

    @Override
    public BinaryOperatorToken __parse__(ParsableString string, Node parent) {
        Node node = parser.parse(string);
        if (node == null) return null;
        /*
         * So.... we gotta convert
         * {
         *   exp
         *   [{-, exp},{*, exp},{+, exp}]
         * }
         * // I'll form an intermediate first,
         * // { exp - exp * exp + exp }
         * // and then splice BOTs out of it
         * to
         * {
         *   exp
         *   -
         *   {
         *       {
         *           exp
         *           *
         *           exp
         *       }
         *       +
         *       exp
         *   }
         * }
         * */

        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(node.get(0));

        // node.get(2) is the container Node created by MinimumRepeatableParser
        // node.get(2).forEachChild iterates the container Node created by CompoundParser
        // compound -> compound.forEachChild iterates the TWO Nodes, one is a LooseSpaceBoundedParser and the other ExpressionsToken
        node.get(1).forEachChild(compound -> compound.forEachChild(nodes::add));

        for (String operator : THOSEOperatorsPrototype.KNOWN_OPERATORS) {
            for (int i = 1, s = nodes.size(); i < s; i += 2) /*iterating only the ops*/ {
                StringValueNode svn = (StringValueNode) nodes.get(i);
                if (operator.equals(svn.getValue())) {

                    ExpressionsToken left = (ExpressionsToken) nodes.remove(i - 1);
                    nodes.remove(i - 1); // this operand
                    ExpressionsToken right = (ExpressionsToken) nodes.remove(i - 1);
                    nodes.add(i - 1, new BinaryOperatorToken(left, svn.getValue(), right));

                    // Reset counters so that we start from start!
                    i = -1; // the i+=2 in last will make it 1
                    s = nodes.size();
                }
            }
        }

        assert nodes.size() == 1;

        BinaryOperatorToken token = (BinaryOperatorToken) nodes.get(0);
        if (parent != null) parent.addChild(token);
        return token;
    }
}