package be.peopleware.taglet.contract;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;
import org.apache.commons.jexl.parser.*;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 * 
 * BNF Comments in methods are taken from Velocity's BNF specification:
 * http://jakarta.apache.org/velocity/specification-bnf.html Lets hope for now
 * that these definitions are similar.
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
    StringBuffer acc = (StringBuffer) data;
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      StringBuffer nextChild = new StringBuffer();
      SimpleNode childNode = (SimpleNode) node.jjtGetChild(i);
//      System.out.println("visiting " + childNode.getClass().getName());
      visit(childNode, nextChild);
      acc.append(nextChild);
    }
    return acc;
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

    StringBuffer acc = (StringBuffer)data;
    acc.append(value);
    return acc;
  }

  public static final Set JAVA_KEYWORDS = new HashSet();

  static {
    JAVA_KEYWORDS.add("new");
    JAVA_KEYWORDS.add("final");
    JAVA_KEYWORDS.add("class");
  }

  public static final Set CONTRACT_KEYWORDS = new HashSet();

  static {
    CONTRACT_KEYWORDS.add("result");
    CONTRACT_KEYWORDS.add("forall");
  }

  public Object visit(SimpleNode node, Object data) {

    // Common functionality can be implemented here

    if (node == null) {
        System.err.println("Node Is Null!");
        return data;
    }

    Class clazz = node.getClass();
    System.out.println(clazz.getName());
    node.jjtAccept(this, data);

    return data;
  }

  public Object visit(ASTJexlScript node, Object data) {
    // TODO
    // A script in Jexl is made up of zero or more statements.
    visitChildren(node, data);
    StringBuffer acc = (StringBuffer)data;
    acc.insert(0, "<code>");
    acc.append("</code>");
    return acc;
  }

  public Object visit(ASTBlock node, Object data) {

    // A block is simply multiple statements inside curly braces ({, }).
    
    //TODO identation via CSS
    StringBuffer acc = (StringBuffer) data;
    acc.append("<br />{<br />");
    for (int i = 0; i < node.jjtGetNumChildren(); i++) {
      StringBuffer statement = new StringBuffer();
      SimpleNode childNode = (SimpleNode) node.jjtGetChild(i);
//      assert (childNode instanceof ASTExpression) : "all children of BLOCK must be EXPRESSIONs";
      visit(childNode, statement);
      acc.append(statement);
    }
    acc.append("<br />}");
    return acc;
  }

  public Object visit(ASTEmptyFunction node, Object data) {
    // Returns true if the expression following is either:
    //  1. null
    //  2. An empty string
    //  3. An array of length zero
    //  4. An collection of size zero
    //  5. An empty map
    // empty(var)
    visitChildren(node, data);
    StringBuffer acc = (StringBuffer)data;
    acc.insert(0, "empty(");
    acc.append(")");
    return acc;
  }

  public Object visit(ASTSizeFunction node, Object data) {
    // Returns the information about the expression:
    //  1. Length of an array
    //  2. Size of a List
    //  3. Size of a Map
    //  4. Size of a Set
    //  5. Length of a string
    // e.g., size("Hello") returns 5
    visitChildren(node, data);
    StringBuffer acc = (StringBuffer)data;
    acc.insert(0, "size(");
    acc.append(")");
    return acc;
  }

  public Object visit(ASTIdentifier node, Object data) {

    // Must start with a-z, A-Z, _ or $. 
    //    Can then be followed by 0-9, a-z, A-Z, _ or $. 
    // Jexl also supports ant-style variables, e.g. 
    //    my.dotted.var is a valid variable name.

    // TODO ASTIdentifier can have multiple child nodes in Jexl

    StringBuffer acc = (StringBuffer) data;
    
    String text = node.getIdentifierString();
    String prefix  = EMPTY;
    String postfix = EMPTY;
    
    if (JAVA_KEYWORDS.contains(text)) {
      prefix  = "<span class=\"contract_javaKeyword\">"; //$NON-NLS-1$)
      postfix = "</span>"; //$NON-NLS-1$
    }
    if (CONTRACT_KEYWORDS.contains(text)) {
      prefix  = "<span class=\"contract_contractKeyword\">"; //$NON-NLS-1$)
      postfix = "</span>"; //$NON-NLS-1$
    }
    acc.append(prefix);
    acc.append(text);  
    acc.append(postfix);
    return acc;
  }

  public Object visit(ASTExpression node, Object data) {

    // <expression> ::= <true>
    //                  | <false>
    //                  | <primary-expression> "=" <assignment>
    //                  | <conditional-or-expression>
    //
    // <assignment> ::= <primary-expression> "=" <assignment>
    //              assignment in Velocity is ExpressionExpression in Jexl?
    // <primary-expression> ::= <string-literal>
    //                            | <number-literal>
    //                            | <reference>
    //                            | "(" <expression> ")"

    visitChildren(node, data);
    StringBuffer acc = (StringBuffer)data;
    // TODO output as many brackets as necessary
    acc.insert(0,"(");
    acc.append(")");
    return acc;
  }

  public Object visit(ASTAssignment node, Object data) {
    return visitBinary(node, data, " = ");
  }

  public Object visit(ASTOrNode node, Object data) {
    // The usual || operator can be used as well as the word 'or'
    return visitBinary(node, data, " || ");
  }

  public Object visit(ASTAndNode node, Object data) {
    // The usual && operator can be used as well as the word 'and'
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
    // The usual '==' operator can be used as well as the abbreviation eq.
    return visitBinary(node, data, " == ");
  }

  public Object visit(ASTNENode node, Object data) {
    // The usual '!=' operator can be used as well as the abbreviation ne.
    return visitBinary(node, data, " != ");
  }

  public Object visit(ASTLTNode node, Object data) {
    // The usual '<' operator can be used as well as the abbreviation lt.
    return visitBinary(node, data, " < ");
  }

  public Object visit(ASTGTNode node, Object data) {
    // The usual '>' operator can be used as well as the abbreviation gt.
    return visitBinary(node, data, " > ");
  }

  public Object visit(ASTLENode node, Object data) {
    // The usual '<=' operator can be used as well as the abbreviation le.
    return visitBinary(node, data, " <= ");
  }

  public Object visit(ASTGENode node, Object data) {
    // The usual '>=' operator can be used as well as the abbreviation ge.
    return visitBinary(node, data, " >= ");
  }

  public Object visit(ASTAddNode node, Object data) {
    // The usual + operator is used.
    return visitBinary(node, data, " + ");
  }

  public Object visit(ASTSubtractNode node, Object data) {
    // The usual - operator is used.
    return visitBinary(node, data, " - ");
  }

  public Object visit(ASTMulNode node, Object data) {
    // The usual * operator is used.
    return visitBinary(node, data, " * ");
  }

  public Object visit(ASTDivNode node, Object data) {
    // Division - The usual / operator is used.
    // Integer Division - The operator div is used. E.g., 4 div 3 gives 1
    return visitBinary(node, data, " / ");
  }

  public Object visit(ASTModNode node, Object data) {
    // Modulus (or reminder)
    // The % operator is used. An alternative is the mod operator.
    return visitBinary(node, data, " % ");
  }

  public Object visit(ASTUnaryMinusNode node, Object data) {
    // Negation
    // The unary - operator is used.
    return visitUnary(node, data, "-");
  }

  public Object visit(ASTBitwiseComplNode node, Object data) {
    return visitUnary(node, data, "~");
  }

  public Object visit(ASTNotNode node, Object data) {
    // The usual ! operator can be used as well as the word 'not'
    return visitUnary(node, data, "!");
  }

  public Object visit(ASTNullLiteral node, Object data) {
    // The null value is represented as in Java using the literal null.
    return ((StringBuffer) data).append("null");
  }

  public Object visit(ASTTrueNode node, Object data) {
    // The literal true can be used.
//    return getLiteralValue(node, data);
    return ((StringBuffer) data).append("true");
  }

  public Object visit(ASTFalseNode node, Object data) {
    //The literal false can be used.
//    return getLiteralValue(node, data);
    return ((StringBuffer) data).append("false");
  }

  public Object visit(ASTIntegerLiteral node, Object data) {
    //1 or more digits from 0 to 9
    return getLiteralValue(node, data);
  }

  public Object visit(ASTFloatLiteral node, Object data) {
    //1 or more digits from 0 to 9, followed by a decimal point and 
    //    then one or more digits from 0 to 9. 
    return getLiteralValue(node, data);
  }

  public Object visit(ASTStringLiteral node, Object data) {
    // Can start and end with either ' or ".
    getLiteralValue(node, data);
    StringBuffer acc = (StringBuffer) data;
    // TODO which quotes should we output - double or single?
    acc.insert(0, "&laquo;");
    acc.append("&raquo;");
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
    // A statement can be the empty statement, the semicolon(;), block, 
    //   assignment or an expression. 
    //   Statements are optionally terminated with a semicolon.
    StringBuffer acc = (StringBuffer) data;
    visitUnary(node, acc, EMPTY);
//    acc.insert(0, "<br />");
    acc.append(";");
    return acc;
  }

  public Object visit(ASTReferenceExpression node, Object data) {
    // TODO ReferenceExpression - what does it do?
    return visitUnary(node, data, EMPTY);
  }

  public Object visit(ASTIfStatement node, Object data) {

    // <if-statement> ::=
    //          "#if" "(" <expression> ")" <statement> [ <else-statement> ]
    assert (node.jjtGetNumChildren() >= 2) : 
      "if statement must have at least two children.";
    assert (node.jjtGetNumChildren() <= 3) : 
      "if statement can't contain more than 3 children.";
    StringBuffer acc = (StringBuffer) data;
    acc.append("if (");
    StringBuffer condition = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(0), condition);
    acc.append(condition);
    acc.append(") ");
    StringBuffer statement = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(1), statement);
    acc.append(statement);
//    acc.append("<br />}");
    if (node.jjtGetNumChildren() == 3) {
      acc.append(" else ");
      StringBuffer elseStatement = new StringBuffer();
      visit((SimpleNode) node.jjtGetChild(1), elseStatement);
      acc.append(elseStatement);
//      acc.append("<br />}");
    }
    return acc;
  }

  public Object visit(ASTWhileStatement node, Object data) {
    // while statement is not present in Velocity
    assert (node.jjtGetNumChildren() == 2) : "while statement must have two children.";
    StringBuffer acc = (StringBuffer) data;
    acc.append("while (");
    StringBuffer condition = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(0), condition);
    acc.append(condition);
    acc.append(") ");
    StringBuffer statement = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(1), statement);
    acc.append(statement);
//    acc.append("<br />}");
    return acc;
  }

  public Object visit(ASTForeachStatement node, Object data) {

    // <foreach-statement> ::=
    //          "#foreach" <reference> "in" <reference> <statement>
    assert (node.jjtGetNumChildren() == 3) : 
                  "foreach statement must have exactly three children.";
    StringBuffer acc = (StringBuffer) data;
    acc.append("foreach (");
    StringBuffer var = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(0), var);
    acc.append(var);
    acc.append(" in ");
    StringBuffer collection = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(1), collection);
    acc.append(collection);
    acc.append(") ");
    StringBuffer statement = new StringBuffer();
    visit((SimpleNode) node.jjtGetChild(2), statement);
    acc.append(statement);
//    acc.append("<br />}");
    return acc;
  }

  public Object visit(ASTMethod node, Object data) {
    // <method> ::=
    //       <identifier> "(" [ <parameter> { "," <parameter> } ] ")"
    assert (node.jjtGetNumChildren() >= 1) : 
      "method statement must have at least one child.";
    StringBuffer acc = (StringBuffer) data;

    visit((SimpleNode)node.jjtGetChild(0), acc);
    acc.append("(");
    // from position 1 parameters begin (if they are present)
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      StringBuffer parameter = new StringBuffer();
      visit((SimpleNode)node.jjtGetChild(i), parameter);
      acc.append(parameter);
      //add comma only if there are more parameters to come
      if (i != node.jjtGetNumChildren() - 1) {
        acc.append(",");
      }
    }
    acc.append(")");
    return acc;
  }

  public Object visit(ASTArrayAccess node, Object data) {
    // TODO looks like multidimensional arrays are not supported.
    // TODO is empty index allowed?
    // Array elements may be accessed using either square brackets 
    //   or a dotted numeral 
    //   e.g. arr1[0] and arr1.0 are equivalent 
    assert (node.jjtGetNumChildren() >= 1) : 
          "method statement must have at least one child.";
    StringBuffer acc = (StringBuffer) data;

    // get array name
    visit((SimpleNode) node.jjtGetChild(0), acc);
    acc.append("[");
    // get index-expression from position 1
    StringBuffer index = new StringBuffer(EMPTY);
    visit((SimpleNode)node.jjtGetChild(1), index);
    acc.append(index);
    acc.append("]");
    return acc;
  }

  public Object visit(ASTSizeMethod node, Object data) {
    // comment from source:
    //    generalized size() function for all classes we can think of
    visitChildren(node, data);
    StringBuffer acc = (StringBuffer) data;
    acc.append("size()");
    return acc;
  }

  public Object visit(ASTReference node, Object data) {
    // <reference> ::=
    //      "$" <identifier> { "." <method> | <identifier> } 
    assert (node.jjtGetNumChildren() >= 1) : 
      "reference statement must have at least one child.";
    StringBuffer acc = (StringBuffer) data;

    visit((SimpleNode) node.jjtGetChild(0), acc);
    for (int i = 1; i < node.jjtGetNumChildren(); i++) {
      acc.append(".");
      StringBuffer nextRef = new StringBuffer();
      visit((SimpleNode)node.jjtGetChild(i), nextRef);
      acc.append(nextRef);
    }
    return acc;
  }

}