package be.peopleware.taglet.contract;


import java.util.ArrayList;
import java.util.Collection;

import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Abstract superclass for common code in contract taglets.
 * Fill in the list of the allowed keywords in the constructor of the subclass.
 * For example, the taglet <code>ResultTaglet</code> would have 
 * following settings:
 * <pre>
 *   $allowedKeywords.add(AbstractContractTaglet.KEYWORD_RESULT);
 *   $allowedKeywords.add(AbstractContractTaglet.KEYWORD_NEW);
 *   $allowedKeywords.add(AbstractContractTaglet.KEYWORD_FORALL);
 * </pre>
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractContractTaglet extends AbstractStandaloneTaglet {

  /**
   * The name of <code>result</code> keyword in taglet expressions.
   * 
   * <strong>value = {@value}</strong>
   */
  public final static String KEYWORD_RESULT = "result"; //$NON-NLS-1$

  /**
   * The name of <code>new</code> keyword in taglet expressions.
   * 
   * <strong>value = {@value}</strong>
   */
  public final static String KEYWORD_NEW = "new"; //$NON-NLS-1$

  /**
   * The name of <code>forall</code> keyword in taglet expressions.
   * 
   * <strong>value = {@value}</strong>
   */
  public final static String KEYWORD_FORALL = "forall"; //$NON-NLS-1$

  /**
   * The name of <code>result</code> taglet.
   * 
   * <strong>value = {@value}</strong>
   */
  public final static String TAGLET_INVAR = "invar"; //$NON-NLS-1$

	/**
	 * Array of all contract keywords.
	 */
	public final String[] $allKeywords = new String[] {KEYWORD_RESULT, 
																										KEYWORD_NEW, 
																										KEYWORD_FORALL};
	
	/**
	 * List of allowed keywords in the body of this taglet.
	 */
	public final Collection $allowedKeywords = new ArrayList();


  /**
   * Makes some additional formatting of the content of the taglet.
   * 
   * Error messages are printed when we detect problems.
   * The text of this tag is a boolean Java expression, that ends in a semi-column (;),
   * optionally followed by explanatory text. The semi-column is mandatory.
   * The keywords in this expression are 
   * <code><b>result</b></code>, 
   * <code><b>new</b></code>
   * and <code><b>forall</b></code>. 
   * These keywords are printed bold and in a color.
   * - "result" can only be used in the result tag.;
   * - "new" can only be used in post, result, and invar, not in pre
   * - forall can be in all contract tags 
   *
   * @param     text
   *            content of the taglet
   * @return    text - formatted content
   * 
   * @pre				text.indexOf('\u003B') > 0; semi-column is mandatory
   * 
   * @todo 	make separate class for keywords 
   * 						to incorporate parsing and formatting functionality.
   */
  public String parse(String text) {
  	String expr = null;
  	String description = null;
  	int posSemiColumn = text.indexOf(';');
  	if (posSemiColumn >= 0) {
  		expr = text.substring(0, posSemiColumn);
  		description = text.substring(posSemiColumn);
  	}
  	else {
  		signalParseError("The semi-column is mandatory."); //$NON-NLS-1$
			expr = text;
			description = "";
  	}

  	String result = expr;
  	for (int i = 0; i < $allKeywords.length; i++) {
  		result = processKeyword(result, $allKeywords[i]);
  	}
  	
  	result += description;
    
  	return result;
  }

  /**
   * Prints error messages to error console
   * 
   * @param message
   * 						error message
   * 
   * @question should we do i18n of error messages? 
   */
  private void signalParseError(String message) {
  	System.err.println(message);
  }
  
  /**
   * Makes inline html formatting of the token, 
   * namely makes it bold and colours background.
   * 
   * @param token
   * @param color
   * @return formatted token
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
  private String processKeyword(String origExpr, String keyword) {
  	
  	if (origExpr == null) {
  		return null;
  	}
  	if (keyword == null) {
  		return origExpr;
  	}

  	//Add leading and trailing spaces. We can now safely 
  	// 	retrieve chars from the left and from the right side of the keyword.
  	String expr = " " + origExpr + " ";
  	StringBuffer result = new StringBuffer();
  	int notParsedFrom = 0;
		int keywordBegin = expr.indexOf(keyword);
		int keywordEnd   = keywordBegin + keyword.length();
		while	(keywordBegin >= 0) {
			//if the chars at the left and at the right are not 
			//  part of the identifier, then the keyword is found.
			char charLeft  = expr.charAt(keywordBegin - 1);
			char charRight = expr.charAt(keywordEnd);
			if (! Character.isLetterOrDigit(charLeft) 
							&& ! Character.isLetterOrDigit(charRight)) {
				// the keyword is found
				result.append(expr.substring(notParsedFrom, keywordBegin));

				if (! canContainKeyword(keyword)) {
					signalParseError(getName() 
															+ " taglet can not contain "  //$NON-NLS-1$
															+ keyword + " keyword");  //$NON-NLS-1$
				}
				
	  		result.append(makeupKeyword(keyword));
	  		
				notParsedFrom = keywordEnd;
			}
			keywordBegin = expr.indexOf(keyword, keywordEnd);
			keywordEnd   = keywordBegin + keyword.length();
		}
		result.append(expr.substring(notParsedFrom, expr.length()));
		
		//strip out leading and trailing spaces, added in the begin of this method.
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
   * @param keyword
   * 						keyword to be checked.
   * @return
   * 						true, if list of allowed keywords contains <code>keyword</code>,
   * 						<br>false otherwise.
   * 
   * @result result == listOfKeywords.contains(keyword);

   * @todo throw an exception
   * @question should we color keyword if it's not allowed in the taglet?
   */
  public boolean canContainKeyword(String keyword) {
  	return $allowedKeywords.contains(keyword);
  }
}
