package be.peopleware.taglet.contract;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.jexl.parser.*;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 */
public class HtmlGenerator implements ParserVisitor {

    /**
     * <p>
     * The empty String.
     * </p>
     * <p>
     * <strong>value ={@value}</strong>
     * </p>
     */
    public final static String EMPTY = ""; //$NON-NLS-1$

    JexlContext jc = JexlHelper.createContext();

    public Object visitChildren(SimpleNode node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode childNode = (SimpleNode) node.jjtGetChild(i);
            System.out.println("visiting " + childNode.getClass().getName());
            visit(childNode, data);
        }
        return data;
    }

    private Object visitBinary(SimpleNode node, Object data, String operator) {
        assert (node.jjtGetNumChildren() == 2) : node.getClass().getName()
                + " must have two children";
        StringBuffer lhs = new StringBuffer();
        visit((SimpleNode) node.jjtGetChild(0), lhs);
        StringBuffer rhs = new StringBuffer();
        visit((SimpleNode) node.jjtGetChild(1), rhs);
        StringBuffer acc = (StringBuffer) data;
        return acc.append(lhs).append(operator).append(rhs);
    }

    private Object visitUnary(SimpleNode node, Object data, String operator) {
        assert (node.jjtGetNumChildren() == 1) : node.getClass().getName()
                + " must have one child";
        visit((SimpleNode) node.jjtGetChild(0), data);
        StringBuffer acc = (StringBuffer) data;
        acc.insert(0, operator);
        return acc;
    }

    private Object getLiteralValue(SimpleNode node, Object data) {
        assert (node.jjtGetNumChildren() == 0) : node.getClass().getName()
                + " must not have any children";
        String value = EMPTY;
        try {
            value = node.value(jc).toString();
        } catch (Exception e) {
            System.err.println("Can't evaluate the value of the node "
                    + node.getClass().getName());
        }

        StringBuffer acc = (StringBuffer) data;
        acc.append(value);
        return acc;
    }

    public Object visit(SimpleNode node, Object data) {

        // Common functionality can be implemented here

        //        if (node == null) {
        //            System.err.println("Node Is Null!");
        //            return data;
        //        }

        assert (node != null) : "node is null";

        node.jjtAccept(this, data);

        return data;
    }

    public Object visit(ASTJexlScript node, Object data) {
        // TODO
        return visitChildren(node, data);
    }

    public Object visit(ASTBlock node, Object data) {
        // TODO
        visitChildren(node, data);
        StringBuffer acc = (StringBuffer) data;
        acc.insert(0, "<br />{<br />");
        acc.append("<br />}<br />");
        return data;
    }

    public Object visit(ASTEmptyFunction node, Object data) {
        // TODO
        // comment from source:
        // 		function to see if reference doesn't exist in context
        return visitChildren(node, data);
    }

    public Object visit(ASTSizeFunction node, Object data) {
        // TODO
        // comment from source:
        //		generalized size() function for all classes we can think of
        visitChildren(node, data);
        StringBuffer acc = (StringBuffer) data;
        acc.append(".size()");
        return acc;
    }

    public Object visit(ASTIdentifier node, Object data) {

        StringBuffer acc = (StringBuffer) data;

        // TODO ASTIdentifier can have multiple child nodes, 
        // 	e.g. this.getHeader() is identifier
        // TODO take keyword names from somewhere else
        
        visitChildren(node, data);
        String text = node.getIdentifierString();
        if (text.equals("result") || text.equals("forall")
                || text.equals("new")) {
            acc.append("<span style='font: bold; background-color: yellow'>"); //$NON-NLS-1$)
            acc.append(text);
            acc.append("</span>"); //$NON-NLS-1$
        }
        return acc;
    }

    public Object visit(ASTExpression node, Object data) {
        // TODO how many children may it have? Any formatting?
        return visitUnary(node, data, EMPTY);
    }

    public Object visit(ASTAssignment node, Object data) {
        return visitBinary(node, data, " = ");
    }

    public Object visit(ASTOrNode node, Object data) {
        return visitBinary(node, data, " || ");
    }

    public Object visit(ASTAndNode node, Object data) {
        return visitBinary(node, data, " && ");
    }

    public Object visit(ASTBitwiseOrNode node, Object data) {
        return visitBinary(node, data, " | ");
    }

    public Object visit(ASTBitwiseXorNode node, Object data) {
        return visitBinary(node, data, " ^ ");
    }

    public Object visit(ASTBitwiseAndNode node, Object data) {
        return visitBinary(node, data, " & ");
    }

    public Object visit(ASTEQNode node, Object data) {
        return visitBinary(node, data, " == ");
    }

    public Object visit(ASTNENode node, Object data) {
        return visitBinary(node, data, " != ");
    }

    public Object visit(ASTLTNode node, Object data) {
        return visitBinary(node, data, " < ");
    }

    public Object visit(ASTGTNode node, Object data) {
        return visitBinary(node, data, " > ");
    }

    public Object visit(ASTLENode node, Object data) {
        return visitBinary(node, data, " <= ");
    }

    public Object visit(ASTGENode node, Object data) {
        return visitBinary(node, data, " >= ");
    }

    public Object visit(ASTAddNode node, Object data) {
        return visitBinary(node, data, " + ");
    }

    public Object visit(ASTSubtractNode node, Object data) {
        return visitBinary(node, data, " - ");
    }

    public Object visit(ASTMulNode node, Object data) {
        return visitBinary(node, data, " * ");
    }

    public Object visit(ASTDivNode node, Object data) {
        return visitBinary(node, data, " / ");
    }

    public Object visit(ASTModNode node, Object data) {
        return visitBinary(node, data, " % ");
    }

    public Object visit(ASTUnaryMinusNode node, Object data) {
        return visitUnary(node, data, "-");
    }

    public Object visit(ASTBitwiseComplNode node, Object data) {
        return visitUnary(node, data, "~");
    }

    public Object visit(ASTNotNode node, Object data) {
        return visitUnary(node, data, "!");
    }

    public Object visit(ASTNullLiteral node, Object data) {
        // TODO return acc.append("null")?
        return ((StringBuffer)data).append("null");
    }

    public Object visit(ASTTrueNode node, Object data) {
        return getLiteralValue(node, data);
    }

    public Object visit(ASTFalseNode node, Object data) {
        return getLiteralValue(node, data);
    }

    public Object visit(ASTIntegerLiteral node, Object data) {
        return getLiteralValue(node, data);
    }

    public Object visit(ASTFloatLiteral node, Object data) {
        return getLiteralValue(node, data);
    }

    public Object visit(ASTStringLiteral node, Object data) {
        getLiteralValue(node, data);
        StringBuffer acc = (StringBuffer) data;
        // TODO check if it is necessary to add quotes
        acc.insert(0, "\"");
        acc.append("\"");
        return acc;
    }

    public Object visit(ASTExpressionExpression node, Object data) {

        // TODO format .equals() for strings
        //comment in class ASTExpressionExpression:
        //		represents equality between integers - use .equals() for strings
        return visitUnary(node, data, EMPTY);
    }

    public Object visit(ASTStatementExpression node, Object data) {
        // TODO how many children may StatementExpression have?
        visitUnary(node, data, EMPTY);
        StringBuffer acc = (StringBuffer) data;
        acc.append(";");
        return acc;
    }

    public Object visit(ASTReferenceExpression node, Object data) {
        // TODO ReferenceExpression - what does it do?
        return visitUnary(node, data, EMPTY);
    }

    public Object visit(ASTIfStatement node, Object data) {
        // TODO visit ASTIfStatement
        return visitChildren(node, data);
    }

    public Object visit(ASTWhileStatement node, Object data) {
        // TODO visit ASTWhileStatement
        return visitChildren(node, data);
    }

    public Object visit(ASTForeachStatement node, Object data) {
        // TODO visit ASTForeachStatement
        return visitChildren(node, data);
    }

    public Object visit(ASTMethod node, Object data) {
        // TODO visit ASTMethod
        return visitChildren(node, data);
    }

    public Object visit(ASTArrayAccess node, Object data) {
        // TODO visit ASTArrayAccess
        return visitChildren(node, data);
    }

    public Object visit(ASTSizeMethod node, Object data) {
        // TODO visit ASTSizeMethod
        return visitChildren(node, data);
    }

    public Object visit(ASTReference node, Object data) {
        // TODO Reference - what does it do?
        return visitUnary(node, data, EMPTY);
    }

}