package be.peopleware.taglet.contract;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.jexl.parser.*;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 */
public class HtmlGenerator implements ParserVisitor {

    JexlContext jc = JexlHelper.createContext();

    public Object visitChildren(SimpleNode node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode childNode = (SimpleNode) node.jjtGetChild(i);
            System.out.println("........visiting "
                    + childNode.getClass().getName());
            if (node != null) {
                visit(childNode, data);
            }
        }
        return data;
    }

    public Object visit(SimpleNode node, Object data) {

        StringBuffer acc = (StringBuffer) data;
        if (node == null) {
            System.out.println("Node Is Null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return data;
        }
        System.out.println("...........SimpleNode..." + acc.toString());
        return visitChildren(node, data);
    }

    public Object visit(ASTJexlScript node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        System.out.println("...........JexlScript..." + acc.toString());
        visit((ASTReferenceExpression)node.jjtGetChild(0), acc);
        return acc;
    }

    public Object visit(ASTBlock node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        System.out.println("...........Block..." + acc.toString());
        acc.append("<br />{<br />");
        visitChildren(node, data);
        acc.append("<br />}<br />");
        return data;
    }

    public Object visit(ASTEmptyFunction node, Object data) {
        // comment from source:
        // 		function to see if reference doesn't exist in context
        return data;
    }

    public Object visit(ASTSizeFunction node, Object data) {
        // comment from source:
        //		generalized size() function for all classes we can think of
        StringBuffer acc = (StringBuffer) data;
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(".size()");
        return acc;
    }

    public Object visit(ASTIdentifier node, Object data) {
        StringBuffer acc = (StringBuffer) data;

        System.out.println("Identifier!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        String text = ((ASTIdentifier) node).getIdentifierString();
        if (text.equals("result") || text.equals("forall")
                || text.equals("new")) {
            acc.append("<span style='font: bold; background-color: yellow'>"); //$NON-NLS-1$)
            acc.append(text);
            acc.append("</span>"); //$NON-NLS-1$
        }
        return acc;
    }

    public Object visit(ASTExpression node, Object data) {
        return data;
    }

    public Object visit(ASTAssignment node, Object data) {
        return data;
    }

    public Object visit(ASTOrNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTOrNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" || ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTAndNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTAndNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" && ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTBitwiseOrNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTBitwiseOrNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" | ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTBitwiseXorNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTBitwiseXorNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" ^ ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTBitwiseAndNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTBitwiseAndNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" & ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTEQNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTEQNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" == ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTNENode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTNENNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" != ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTLTNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTLTNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" < ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTGTNode node, Object data) {
        StringBuffer acc = (StringBuffer) data;
        assert (node.jjtGetNumChildren() == 2) : "ASTGTNode must have two children";
        visit((SimpleNode) node.jjtGetChild(0), acc);
        acc.append(" > ");
        visit((SimpleNode) node.jjtGetChild(1), acc);
        return acc;
    }

    public Object visit(ASTLENode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTLENode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" <= ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTGENode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTGENode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" >= ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTAddNode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTAddNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" + ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTSubtractNode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTSubtractNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" - ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTMulNode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTMulNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" * ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTDivNode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTDivNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" / ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTModNode node, Object data) {
  	  	StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTModNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" % ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTUnaryMinusNode node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 1):
						"ASTUnaryMinusNode must have one child";
  	  	StringBuffer acc = (StringBuffer)data;
        visit((SimpleNode)node.jjtGetChild(0), acc);
        acc.insert(0, "-");
        return acc;
    }

    public Object visit(ASTBitwiseComplNode node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 1):
						"ASTBitwiseComplNode must have one child";
  	  	StringBuffer acc = (StringBuffer)data;
        visit((SimpleNode)node.jjtGetChild(0), acc);
        acc.insert(0, "~");
        return acc;
    }

    public Object visit(ASTNotNode node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 1):
						"ASTNotNode must have one child";
  	  	StringBuffer acc = (StringBuffer)data;
        visit((SimpleNode)node.jjtGetChild(0), acc);
        acc.insert(0, "!");
        return acc;
    }

    public Object visit(ASTNullLiteral node, Object data) {
        return data;
    }

    public Object visit(ASTTrueNode node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTTrueNode must not have any children";
  	  	StringBuffer acc = (StringBuffer)data;
  	  	acc.append("true");
  	  	return acc;
    }

    public Object visit(ASTFalseNode node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTFalseNode must not have any children";
  	  	StringBuffer acc = (StringBuffer)data;
  	  	acc.append("false");
  	  	return acc;
    }

    public Object visit(ASTIntegerLiteral node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTIntegerLiteralNode must not have any children";
  	  	StringBuffer acc = (StringBuffer)data;
  	  	try {
            acc.append(node.value(jc));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  	  	return acc;
    }

    public Object visit(ASTFloatLiteral node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTFloatLiteralNode must not have any children";
  	  	StringBuffer acc = (StringBuffer)data;
  	  	try {
            acc.append(node.value(jc));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  	  	return acc;
    }

    public Object visit(ASTStringLiteral node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTStringLiteralNode must not have any children";
  	  	StringBuffer acc = (StringBuffer)data;
  	  	acc.append("\"");
  	  	try {
            acc.append(node.value(jc));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  	  	acc.append("\"");
        return acc;
    }

    public Object visit(ASTExpressionExpression node, Object data) {
  	  	
        //comment in class ASTExpressionExpression:
        //		represents equality between integers - use .equals() for strings
        StringBuffer acc = (StringBuffer)data;
  	  	assert(node.jjtGetNumChildren() == 2):
  	  								"ASTExpressionExpressionNode must have two children";
  	  	visit((SimpleNode)node.jjtGetChild(0), acc);
  	  	acc.append(" == ");
  	  	visit((SimpleNode)node.jjtGetChild(1), acc);
  	  	return acc;
    }

    public Object visit(ASTStatementExpression node, Object data) {
        // TODO visit StatementExpression
        return visitChildren(node, data);
    }

    public Object visit(ASTReferenceExpression node, Object data) {
        // TODO visit ReferenceExpression
        StringBuffer acc = (StringBuffer)data;
        visit((ASTReference)node.jjtGetChild(0), acc);
        return acc;
    }

    public Object visit(ASTIfStatement node, Object data) {
        // TODO visit ASTIfStatement
        return data;
    }

    public Object visit(ASTWhileStatement node, Object data) {
        // TODO visit ASTWhileStatement
        return data;
    }

    public Object visit(ASTForeachStatement node, Object data) {
        // TODO visit ASTForeachStatement
        return data;
    }

    public Object visit(ASTMethod node, Object data) {
        // TODO visit ASTMethod
        return data;
    }

    public Object visit(ASTArrayAccess node, Object data) {
        // TODO visit ASTArrayAccess
        return data;
    }

    public Object visit(ASTSizeMethod node, Object data) {
        // TODO visit ASTSizeMethod
        return data;
    }

    public Object visit(ASTReference node, Object data) {
  	  	assert(node.jjtGetNumChildren() == 0):
						"ASTReferenceNode must not have any children";
        StringBuffer acc = (StringBuffer)data;
        visit((ASTIdentifier)node.jjtGetChild(0), acc);
        return acc;
    }

}