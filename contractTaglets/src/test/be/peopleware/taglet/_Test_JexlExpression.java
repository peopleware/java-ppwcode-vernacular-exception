/*
 * Created on 5-jan-2005
 *
 */
package be.peopleware.taglet;

import java.io.StringReader;

import org.apache.commons.jexl.parser.ASTJexlScript;
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
        String expr = "result;";
        Parser parser = new Parser(new StringReader(";"));
        ASTJexlScript sn = null;
        try {
            sn = (ASTJexlScript)parser.parse(new StringReader(expr));
        } catch (Exception exc) {
            // TODO why do we get an exception here?
            System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!");
        }
        StringBuffer htmlResult = new StringBuffer("");
        
  	  	HtmlGenerator htmlGenerator = new HtmlGenerator();
        htmlGenerator.visit(sn, htmlResult);

        System.out.println(htmlResult.toString());
        
    }
}
