package be.peopleware.taglet.contract;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.parser.Parser;
import org.apache.commons.jexl.parser.SimpleNode;

import com.sun.javadoc.Tag;

import be.peopleware.taglet.AbstractStandaloneTaglet;

/**
 * Abstract superclass for common code in contract taglets. Fill in the list of
 * the allowed keywords in the constructor of the subclass. For example, the
 * taglet <code>ResultTaglet</code> would have following settings:
 * 
 * <pre>
 * $allowedKeywords.add(AbstractContractTaglet.KEYWORD_RESULT);
 * $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
 * $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
 * </pre>
 * 
 * @author Jan Dockx
 * @author David Van Keer
 * @author Ren&eacute; Clerckx
 * @author Abdulvakhid Shoudouev
 * @author Peopleware n.v.
 * 
 * 'At' sign before 'invar' is removed for the purpose of testing 
 * invar for (int i = 0; i < $allKeywords.length; i++) { 
 * 					for (int j = 0; j < $allKeywords[i].length(); j++) {
 *        		Character.isLetterOfDigit($allKeywords[i].charAt(j)) == true; 
 * 					} 
 * 				} ;
 *        all characters in all keywords are letters or digits. 
 * 				(forall int i; (i >= 0) && (i < $allKeywords.length); 
 * 					$allKeywords[i].indexOf(i) != null);
 */
public abstract class AbstractContractTaglet extends AbstractStandaloneTaglet {

    /**
     * The name of <code>result</code> keyword in taglet expressions.
     * 
     * <strong>value ={@value}</strong>
     */
    public final static String KEYWORD_RESULT = "result"; //$NON-NLS-1$

    /**
     * The name of <code>new</code> keyword in taglet expressions.
     * 
     * <strong>value ={@value}</strong>
     */
    public final static String KEYWORD_NEW = "new"; //$NON-NLS-1$

    /**
     * The name of <code>forall</code> keyword in taglet expressions.
     * 
     * <strong>value ={@value}</strong>
     */
    public final static String KEYWORD_FORALL = "forall"; //$NON-NLS-1$

    /**
     * Array of all contract keywords.
     */
    public final String[] $allKeywords = new String[] { KEYWORD_RESULT,
            KEYWORD_NEW, KEYWORD_FORALL };

    /**
     * List of allowed keywords in the body of this taglet.
     */
    public final Collection $allowedKeywords = new ArrayList();

    /**
     * Makes some additional formatting of the content of the taglet.
     * 
     * @mudo clean up documentation below.
     * 
     * Error messages are printed when we detect problems. The text of this tag
     * is a boolean Java expression, that ends in a semi-column (;), optionally
     * followed by explanatory text. The semi-column is mandatory. The keywords
     * in this expression are <code><b>result</b></code>,
     * <code><b>new</b></code> and <code><b>forall</b></code>. These
     * keywords are printed bold and in a color.
     * <ul>
     * <li><code><b>result</b></code> can only be used in the result tag.;
     * </li>
     * <li><code><b>new</b></code> can only be used in post, result, and
     * invar, not in pre;</li>
     * <li><code><b>forall</b></code> can be in all contract tags.</li>
     * </ul>
     * 
     * @param tag
     *            taglet to be parsed.
     * @pre tag.text() != null;
     * 
     * @return text; formatted content
     * 
     * 'At' sign before 'post' is removed for the purpose of testing 
 		 * post (text.indexOf('\u003B') < 0) ==> error message on System.err;
     * 
     * @todo make separate class for keywords to incorporate parsing and
     *       formatting functionality.
     */
    public String parse(Tag tag) {
        String text = tag.text();
        String expr = EMPTY;
        String description = EMPTY;
        int posSemiColumn = text.indexOf(';');
        
//-------------experiments with jexl---------------------------
        if (posSemiColumn >= 0) {
            expr = text.substring(0, posSemiColumn + 1);
        }
        
        StringBuffer htmlResult = new StringBuffer(EMPTY);
        // When parsing expressions, ExpressionFactory synchronizes on Parser.
        // 	Since we don not evaluate expressions using this Parser, 
        //  it looks like it doesn't matter how we instantiate it.
        Parser parser = new Parser(new StringReader(";"));
        SimpleNode sn = null;
        try {
            sn = parser.parse(new StringReader(expr));
            htmlResult = new StringBuffer(EMPTY);
            
            HtmlGenerator htmlGenerator = new HtmlGenerator();
//            sn.jjtAccept(htmlGenerator, htmlResult);
            
            htmlGenerator.visit(sn, htmlResult);

            System.out.println("++++++++++ " + htmlResult.toString());
            
            htmlResult.append(description);
        } catch (Exception exc) {
            // TODO why do we get an exception here?
            System.out.println("Exception!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(expr);
            System.out.println();
        }
        return htmlResult.toString();

//---------------------------------------------------------------
        
//        if (posSemiColumn >= 0) {
//            expr = "<code>" //$NON-NLS-1$
//                    + text.substring(0, posSemiColumn + 1) + "</code>"; //$NON-NLS-1$
//            description = text.substring(posSemiColumn + 1);
//        } else {
//            signalParseError(tag, text
//                    + " -- The semi-column is mandatory in a contract text."); //$NON-NLS-1$
//            // MUDO give line number in error message
//            expr = EMPTY;
//            description = text;
//        }
//
//        String result = expr;
//        for (int i = 0; i < $allKeywords.length; i++) 
//        {
//            result = processKeyword(tag, result, $allKeywords[i]);
//        }
//        result = processKeyword(tag, result, $allKeywords[0]);
//
//        result += description;
//
//        return result;
    }

    /**
     * Prints error messages to error console.
     * 
     * @param message
     *            error message
     */
    private void signalParseError(Tag tag, String message) {
        System.err.println(tag.position());
        System.err.println(message);
    }

    /**
     * Makes inline html formatting of the keyword, namely makes it bold and
     * colours background.
     * 
     * @param keyword
     * @return formatted keyword
     */
    private String makeupKeyword(String keyword) {
        StringBuffer result = new StringBuffer();
        result.append("<span style='font: bold; background-color:"); //$NON-NLS-1$
        result.append(getKeywordColor(keyword)).append("'>"); //$NON-NLS-1$
        result.append(keyword);
        result.append("</span>"); //$NON-NLS-1$
        return result.toString();
    }

    /**
     * Formats all occurrences of keyword in expr.
     */
    private String processKeyword(Tag tag, String origExpr, String keyword) {

        if (origExpr == null) {
            return null;
        }
        if (keyword == null) {
            return origExpr;
        }

          	//Add leading and trailing spaces. We can now safely
          	// retrieve chars from the left and from the right side 
        		// 			of the keyword.
          	String expr = " " + origExpr + " "; //$NON-NLS-1$ //$NON-NLS-2$
          	StringBuffer result = new StringBuffer();
          	int notParsedFrom = 0;
        		int keywordBegin = expr.indexOf(keyword);
        		int keywordEnd = keywordBegin + keyword.length();
        		while (keywordBegin >= 0) {
        			//if the chars at the left and at the right are not
        			// part of the identifier, then the keyword is found.
        			char charLeft = expr.charAt(keywordBegin - 1);
        			char charRight = expr.charAt(keywordEnd);
        			if (! Character.isLetterOrDigit(charLeft)
        							&& ! Character.isLetterOrDigit(charRight)) {
        				// the keyword is found
        				result.append(expr.substring(notParsedFrom, keywordBegin));
        
        				if (! canContainKeyword(keyword)) {
        					signalParseError(tag, getName()
        															+ " taglet can not contain " //$NON-NLS-1$
        															+ keyword + " keyword"); //$NON-NLS-1$
        				}
        				
        	  		result.append(makeupKeyword(keyword));
        	  		
        				notParsedFrom = keywordEnd;
        			}
        			keywordBegin = expr.indexOf(keyword, keywordEnd);
        			keywordEnd = keywordBegin + keyword.length();
        		}
        		result.append(expr.substring(notParsedFrom, expr.length()));
        		
        		//strip out leading and trailing spaces, 
        		//		added in the begin of this method.
        		result.deleteCharAt(0);
        		result.deleteCharAt(result.length() - 1);
        
          	return result.toString();
    }

    private String getKeywordColor(String keyword) {
        //make them all yellow for the time being
        return "yellow"; //$NON-NLS-1$
    }

    /**
     * Checks if the text of this taglet can contanain given keyword.
     * 
     * @param keyword
     *            keyword to be checked.
     * @return <code>true</code>, if list of allowed keywords contains
     *         <code>keyword</code>,<br>
     *         <code>false</code> otherwise.
     * 
     * @result result == $allowedKeywords.contains(keyword);
     * 
     * @question should we color keyword if it's not allowed in the taglet?
     */
    public boolean canContainKeyword(String keyword) {
        return $allowedKeywords.contains(keyword);
    }

}