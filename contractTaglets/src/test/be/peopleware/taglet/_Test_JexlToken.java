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
import org.apache.commons.jexl.parser.Token;

import be.peopleware.taglet.contract.HtmlGenerator;

/**
 * @author ashoudou
 * @author Peopleware n.v.
 *
 */
public class _Test_JexlToken {

    public static void main(String[] args) {
        String expr = "this.getHeader() != null && result > 0;";

        Parser parser = new Parser(new StringReader(expr));
        
        StringBuffer htmlResult = new StringBuffer("");
        String stringToken = parser.getNextToken().toString();
        
        while (stringToken.length() > 0) {
            if (stringToken.equals("result")) {
                stringToken = "<b>" + stringToken + "</b>";
            }
            htmlResult.append(stringToken);
            stringToken = parser.getNextToken().toString();
        }
        
        System.out.println(htmlResult.toString());
        
    }
}
