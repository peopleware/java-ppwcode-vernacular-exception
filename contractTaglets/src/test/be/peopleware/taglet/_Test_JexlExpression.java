/*
 * Created on 5-jan-2005
 *
 */
package be.peopleware.taglet;

import java.io.StringReader;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.parser.Parser;
import org.apache.commons.jexl.parser.SimpleNode;

import be.peopleware.taglet.contract.HtmlGenerator;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 *
 */
public class _Test_JexlExpression {

    public static void main(String[] args) {
        String expr = "this.getHeader() != null;";

        Parser parser = new Parser(new StringReader(";"));
        SimpleNode sn = null;
        try {
            sn = (SimpleNode)parser.parse(new StringReader(expr));
        } catch (Exception exc) {
            System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!");
        }
        StringBuffer htmlResult = new StringBuffer("");
        
  	  	HtmlGenerator htmlGenerator = new HtmlGenerator();

  	  	try {
            //  	  	sn.jjtAccept(htmlGenerator, htmlResult);
            Expression exprExpr = ExpressionFactory.createExpression(expr);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        htmlGenerator.visit(sn, htmlResult);

        System.out.println(htmlResult.toString());
        
    }
}
