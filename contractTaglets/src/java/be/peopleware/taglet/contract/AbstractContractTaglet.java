package be.peopleware.taglet.contract;


import be.peopleware.taglet.AbstractStandaloneTaglet;


/**
 * Abstract superclass for common code in contract taglets.
 *
 * @author    Jan Dockx
 * @author    David Van Keer
 * @author    René Clerckx
 * @author    Abdulvakhid Shoudouev
 * @author    Peopleware n.v.
 */
public abstract class AbstractContractTaglet extends AbstractStandaloneTaglet {

  /**
   * Makes some additional formatting of the content of the taglet.
   * 
   * Error messages are printed when we detect problems.
   * The text of this tag is a boolean Java expression, that ends in a semi-column (;),
   * optionally followed by explanatory text. The semi-column is mandatory.
   * The keywords in this expression are <code><b>result</b></code>, <code><b>new</b></code>
   * and <code><b>forall</b></code>. These keywords are printed bold and in a color.
   * - "result" can only be used in the result tag.;
   * - "new" can only be used in post, result, and invar, not in pre
   * - forall can be in all contract tags 
   *
   * @param     text
   *            content of the taglet
   * @return    text - formatted content
   */
  public String parse(String text) {
    return text;
  }

  
}
