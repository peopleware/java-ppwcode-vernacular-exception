/*
 * Created on 5-jan-2005
 *
 */
package be.peopleware.taglet;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        
        List expressions = new ArrayList();
        expressions.add("result != null;");
        expressions.add("new.getName() == name;");
        expressions.add("new.getName().equals(name);"); //see ASTExpressionExpression
        expressions.add("x.size() == 2;");
        expressions.add("$someArray[1] > 0;");  //make two indexes
//        expressions.add("$f(x) > 0;");      //doesn't work
        expressions.add("this.getHeader() != null;");
        expressions.add("this.getHeader().length() > 0;");
        expressions.add("new.getHeader() != null && new.getHeader().length() > 0;");
//        expressions.add("result == this.toString(new Tag[] {taglet});"); // doesn't work
        expressions.add("tag.text();");
        expressions.add("this.getName() != null;");
        expressions.add("this.getName().getLength() > 0;");
        expressions.add("foreach (taglet in taglets) {taglet != null;};");
        expressions.add("if (x > 0) x = -1;");
        expressions.add("while (y>0) y =-1;");
        expressions.add("s = 'some string';");
        
        Parser parser = new Parser(new StringReader(";"));
        Iterator iter = expressions.iterator();
        while (iter.hasNext()) {
	        SimpleNode sn = null;
	        try {
              String expr = (String)iter.next();
	            sn = (SimpleNode)parser.parse(new StringReader(expr));
	        } catch (Exception exc) {
	            System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!");
              exc.printStackTrace();
	        }
	        StringBuffer htmlResult = new StringBuffer("");
	        
	  	  	HtmlGenerator htmlGenerator = new HtmlGenerator();
	
	
	        htmlGenerator.visit(sn, htmlResult);
	
	        System.out.println(htmlResult.toString());
        }
        
    }
}
