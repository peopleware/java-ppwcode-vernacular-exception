/*
 * Created on 5-jan-2005
 *
 */
package be.peopleware.taglet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jexl.parser.Parser;
import org.apache.commons.jexl.parser.SimpleNode;

import be.peopleware.taglet.contract.HtmlGenerator;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 *
 */
public class _Test_JexlExpression {

  private static List generateTestCases() {
    List expressions = new ArrayList();
//    public Object visit(ASTJexlScript node, Object data) {
      // A script in Jexl is made up of zero or more statements.
    
    // comment-statement doesn't work if it is not terminated by \n
    expressions.add("## This is a 'comment' in an Jexl expression\n");
    
//    public Object visit(ASTBlock node, Object data) {
      // A block is simply multiple statements inside curly braces ({, }).
    expressions.add("{\na = 1; b = 2; c = a + b;}");

//    public Object visit(ASTEmptyFunction node, Object data) {
      // Returns true if the expression following is either:
      //  1. null
      //  2. An empty string
      //  3. An array of length zero
      //  4. An collection of size zero
      //  5. An empty map
      // empty(var)
    expressions.add("if (empty(x)) {x = something;}");
    expressions.add("empty x;");

//    public Object visit(ASTSizeFunction node, Object data) {
    // Returns the information about the expression:
    //  1. Length of an array
    //  2. Size of a List
    //  3. Size of a Map
    //  4. Size of a Set
    //  5. Length of a string
    // e.g., size("Hello") returns 5
//    expressions.add("x = size('Hello');"); - doesn't work
    expressions.add("size(someVariable);");

//    public Object visit(ASTIdentifier node, Object data) {

      // Must start with a-z, A-Z, _ or $. 
      //    Can then be followed by 0-9, a-z, A-Z, _ or $. 
      // Jexl also supports ant-style variables, e.g. 
      //    my.dotted.var is a valid variable name.
    expressions.add("var1$_ = 0;");
    expressions.add("_var2_$ = $_var1;");

//    public Object visit(ASTExpression node, Object data) {

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
    expressions.add("var8 = null;");
    expressions.add("a = (b + c);");
    expressions.add("x = -x;");
    expressions.add("x = x > 0;");
 
//    public Object visit(ASTAssignment node, Object data) {
    expressions.add("x = size(y);");

//    public Object visit(ASTOrNode node, Object data) {
      // The usual || operator can be used as well as the word 'or'
    expressions.add("x || y;");
    expressions.add("x or y;");
//    expressions.add("x OR y;"); - doesn't work


//    public Object visit(ASTAndNode node, Object data) {
      // The usual && operator can be used as well as the word 'and'
    expressions.add("x && y;");
    expressions.add("x and y;");

//    public Object visit(ASTBitwiseOrNode node, Object data) {
    expressions.add("x | y;");

//    public Object visit(ASTBitwiseXorNode node, Object data) {
    expressions.add("x ^ y | z || x;");

//    public Object visit(ASTBitwiseAndNode node, Object data) {
    expressions.add("x & y & z;");

//    public Object visit(ASTEQNode node, Object data) {
      // The usual '==' operator can be used as well as the abbreviation eq.
    expressions.add("x == y;");
    expressions.add("x eq y;");

//    public Object visit(ASTNENode node, Object data) {
      // The usual '!=' operator can be used as well as the abbreviation ne.
    expressions.add("x != y;");
    expressions.add("x ne y;");

//    public Object visit(ASTLTNode node, Object data) {
      // The usual '<' operator can be used as well as the abbreviation lt.
    expressions.add("x < y;");
    expressions.add("x lt y;");

//    public Object visit(ASTGTNode node, Object data) {
      // The usual '>' operator can be used as well as the abbreviation gt.
    expressions.add("x > y;");
    expressions.add("x gt y;");

//    public Object visit(ASTLENode node, Object data) {
      // The usual '<=' operator can be used as well as the abbreviation le.
    expressions.add("x <= y;");
    expressions.add("x le y;");

//    public Object visit(ASTGENode node, Object data) {
      // The usual '>=' operator can be used as well as the abbreviation ge.
    expressions.add("x >= y;");
    expressions.add("x ge y;");

//    public Object visit(ASTAddNode node, Object data) {
      // The usual + operator is used.
    expressions.add("x + size(y);");
    expressions.add("empty(x) + y.size();");

//    public Object visit(ASTSubtractNode node, Object data) {
      // The usual - operator is used.
    expressions.add("3 - (-2);");
    expressions.add("x - f[i];");


//    public Object visit(ASTMulNode node, Object data) {
      // The usual * operator is used.
    expressions.add("3 * (-2);");
    expressions.add("x * this.f(i);");


//    public Object visit(ASTDivNode node, Object data) {
      // Division - The usual / operator is used.
      // Integer Division - The operator div is used. E.g., 4 div 3 gives 1
    expressions.add("3 / 2.2;");
    expressions.add("3 / 2;");
    expressions.add("4 div 3;");

//    public Object visit(ASTModNode node, Object data) {
      // Modulus (or reminder)
      // The % operator is used. An alternative is the mod operator.
    expressions.add("3 % 2.2;");
    expressions.add("3 mod 2.2;");
    expressions.add("x mod y / 2 + (7 % 2);");
    expressions.add("3.1 % 0;");

    //    public Object visit(ASTUnaryMinusNode node, Object data) {
      // Negation
      // The unary - operator is used.
    expressions.add("-x;");
    expressions.add("-2.2;");
    expressions.add("-3 / 2;");
    expressions.add("-this.getNumber();");

//    public Object visit(ASTBitwiseComplNode node, Object data) {
    expressions.add("~x;");

//    public Object visit(ASTNotNode node, Object data) {
      // The usual ! operator can be used as well as the word 'not'
    expressions.add("!x;");
    expressions.add("not x;");

//    public Object visit(ASTNullLiteral node, Object data) {
      // The null value is represented as in Java using the literal null.
    expressions.add("null = null;");
    expressions.add("if (x == null) y = 2;");

//    public Object visit(ASTTrueNode node, Object data) {
      // The literal true can be used.
    expressions.add("x = true;");

//    public Object visit(ASTFalseNode node, Object data) {
      //The literal false can be used.
    expressions.add("x = false;");

//    public Object visit(ASTIntegerLiteral node, Object data) {
      //1 or more digits from 0 to 9
    expressions.add("x = 1234567890;");

//    public Object visit(ASTFloatLiteral node, Object data) {
      //1 or more digits from 0 to 9, followed by a decimal point and 
      //    then one or more digits from 0 to 9. 
    expressions.add("x = 1234567.890;");

//    public Object visit(ASTStringLiteral node, Object data) {
      // Can start and end with either ' or ".
    expressions.add("x = 'some string literal';");

//    public Object visit(ASTExpressionExpression node, Object data) {

      // TODO format .equals() for strings
      //comment in class ASTExpressionExpression:
      //    represents equality between integers - use .equals() for strings
    expressions.add("2 == 2;");
    
//    public Object visit(ASTStatementExpression node, Object data) {
      // A statement can be the empty statement, the semicolon(;), block, 
      //   assignment or an expression. 
      //   Statements are optionally terminated with a semicolon.
    expressions.add("(x + y)*(x-y);");

//    public Object visit(ASTReferenceExpression node, Object data) {
    expressions.add("x.equals('a string' + 'an another string');");

//    public Object visit(ASTIfStatement node, Object data) {

      // <if-statement> ::=
      //          "#if" "(" <expression> ")" <statement> [ <else-statement> ]
//      assert (node.jjtGetNumChildren() >= 2) : 
//        "if statement must have at least two children.";
//      assert (node.jjtGetNumChildren() <= 3) : 
//        "if statement can't contain more than 3 children.";
    expressions.add("if (x lt 0) {x = 0;} else {x = 1;};");
    expressions.add("if (x lt 10) {x = 100;} else {x = 1001;};");
    //this expression doesn't work - looks like bug in Jexl
    expressions.add("if (x lt 10) {x = 100;} else {if (x eq 1000) {x = 1100;}};");

//    public Object visit(ASTWhileStatement node, Object data) {
      // while statement is not present in Velocity
//      assert (node.jjtGetNumChildren() == 2) : "while statement must have two children.";
    expressions.add("while (a > b) {a = b;}");
//    public Object visit(ASTForeachStatement node, Object data) {

      // <foreach-statement> ::=
      //          "#foreach" <reference> "in" <reference> <statement>
//      assert (node.jjtGetNumChildren() == 3) : 
//                    "foreach statement must have exactly three children.";
    expressions.add("foreach (x in y) {x.apply();}");
    
//    public Object visit(ASTMethod node, Object data) {
      // <method> ::=
      //       <identifier> "(" [ <parameter> { "," <parameter> } ] ")"
//      assert (node.jjtGetNumChildren() >= 1) : 
//        "method statement must have at least one child.";
    expressions.add("this.create(1, x, 'string');");
    
//    public Object visit(ASTArrayAccess node, Object data) {
      // multidimensional arrays are not supported.
      // empty index is not allowed.
      // Array elements may be accessed using either square brackets 
      //   or a dotted numeral 
      //   e.g. arr1[0] and arr1.0 are equivalent 
//      assert (node.jjtGetNumChildren() >= 1) : 
//            "method statement must have at least one child.";
    expressions.add("a[0] + a[1];");
//    expressions.add("a[1,1] > 0;"); - doesn't work
//    expressions.add("a[];");        - doesn't work
    
//    public Object visit(ASTSizeMethod node, Object data) {
      // comment from source:
      //    generalized size() function for all classes we can think of
    expressions.add("a.size();");

//    public Object visit(ASTReference node, Object data) {
      // <reference> ::=
      //      "$" <identifier> { "." <method> | <identifier> } 
//      assert (node.jjtGetNumChildren() >= 1) : 
//        "reference statement must have at least one child.";
    expressions.add("x.y.z = 2;");
    expressions.add("this.getName().equals(this.getName().pre);");
  
    return expressions;
  }
  public static void main(String[] args) throws IOException {
    
    List expressions = generateTestCases();
    
    FileWriter fw = new FileWriter(
    "c:\\Program files\\eclipse\\workspace\\contractTaglets\\expr.html");
    fw.write("<html>");
    fw.write("\n<style type='text/css'>");
    fw.write("\n.contract_contractKeyword {color: red}");
    fw.write("\n.contract_javaKeyword {color: blue}");
    fw.write("\n</style>");
    fw.write("\n<body>");
    Parser parser = new Parser(new StringReader(";"));
    Iterator iter = expressions.iterator();
    while (iter.hasNext()) {
      SimpleNode sn = null;
      String expr = "";
      try {
        expr = (String)iter.next();
        sn = parser.parse(new StringReader(expr));
        StringBuffer htmlResult = new StringBuffer("");
        
        HtmlGenerator htmlGenerator = new HtmlGenerator();
        
        
        htmlGenerator.visit(sn, htmlResult);
        
        System.out.println(htmlResult.toString());
        fw.write("\n<br />------------------------------------------------");
        fw.write("\n<br />" + expr);
        fw.write("\n<br />" + htmlResult.toString());
      } catch (Exception exc) {
        System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!");
        exc.printStackTrace();
        System.out.println(expr);
        System.out.println();
      }
    }
    fw.write("\n<br /></body></html>");
    fw.close();
  }
}
